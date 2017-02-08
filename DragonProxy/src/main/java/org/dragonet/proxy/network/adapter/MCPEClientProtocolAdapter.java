/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.adapter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import net.marfgamer.jraknet.RakNetPacket;
import net.marfgamer.jraknet.identifier.MCPEIdentifier;
import net.marfgamer.jraknet.protocol.Reliability;
import net.marfgamer.jraknet.protocol.message.acknowledge.Record;
import net.marfgamer.jraknet.server.RakNetServer;
import net.marfgamer.jraknet.server.RakNetServerListener;
import net.marfgamer.jraknet.server.ServerPing;
import net.marfgamer.jraknet.session.RakNetClientSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.ConnectionStatus;
import org.dragonet.proxy.network.PacketTranslatorRegister;
import org.dragonet.proxy.utilities.LoginPacketPayload;
import org.dragonet.proxy.utilities.Versioning;
import sul.protocol.pocket100.play.ChunkRadiusUpdated;
import sul.protocol.pocket100.play.Login;
import sul.protocol.pocket100.play.PlayStatus;
import sul.protocol.pocket100.play.ResourcePacksInfo;
import sul.protocol.pocket100.play.Respawn;
import sul.protocol.pocket100.play.SetSpawnPosition;
import sul.protocol.pocket100.play.StartGame;
import sul.protocol.pocket100.types.BlockPosition;
import sul.utils.Tuples;

/**
 * This is named "client" because it handles the protocol packets from our
 * clients
 *
 * @author robotman3000
 */
public class MCPEClientProtocolAdapter implements ClientProtocolAdapter<RakNetPacket>, RakNetServerListener {

    @Getter
    private RakNetServer server;

    //private static final MinecraftPEPacketTranslator translator = new MinecraftPEPacketTranslator();
    private Map<Long, UUID> sessionList = new HashMap<>();

    private MCPEIdentifier identifier;
    private final String sender = "[PE Clientside] ";

    @Override
    public void handlePacket(RakNetPacket packet, ClientConnection session) {
        if (session == null) {
            DragonProxy.getLogger().debug(sender + "Session was null");
            return;
        }
        DragonProxy.getLogger().debug(sender + "Handling Packet: " + session.getSessionID() + ": " + Integer.toHexString(packet.buffer().getByte(1)));

        if (session.getStatus() == ConnectionStatus.UNCONNECTED || session.getStatus() == ConnectionStatus.AWAITING_CLIENT_LOGIN) {
            if (packet.buffer().getByte(1) == Login.ID) {
                try {
                    Login pk = Login.fromBuffer(Arrays.copyOfRange(packet.array(), 1, packet.array().length));
                    onClientLoginRequest(pk, session);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        /*else if (session.getStatus() == ConnectionStatus.AWAITING_CLIENT_AUTHENTICATION) {
            if (packet.pid() == ProtocolInfo.TEXT_PACKET && getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null) {
                TextPacket pack = (TextPacket) packet;
                if (getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("email")) {
                    if (!PatternChecker.matchEmail(pack.message.trim())) {

                        sendChat(DragonProxy.getSelf().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                        disconnect(getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                        return;
                    }
                    getDataCache().put(CacheKey.AUTHENTICATION_EMAIL, pack.message.trim());
                    getDataCache().put(CacheKey.AUTHENTICATION_STATE, "password");
                    sendChat(getProxy().getLang().get(Lang.MESSAGE_ONLINE_PASSWORD));
                } else if (getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("password")) {
                    if (getDataCache().get(CacheKey.AUTHENTICATION_EMAIL) == null || pack.message.equals(" ")) {
                        sendChat(getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                        disconnect(getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                        return;
                    }
                    sendChat(getProxy().getLang().get(Lang.MESSAGE_ONLINE_LOGGIN_IN));
                    getDataCache().remove(CacheKey.AUTHENTICATION_STATE);
                    authenticateOnlineMode(pack.message); //We NEVER cache password for better security. 
                }
                return;
            }
        }*/

        //if (session.getStatus() == ConnectionStatus.CONNECTED) {
            Object[] packets = {new RakNetPacket(packet.buffer())};
            if (session.getDownstreamProtocol().getSupportedPacketType() != getSupportedPacketType()) {
                packets = PacketTranslatorRegister.translateToPC(session, packet);
            }

            for (Object pack : packets) {
                session.getDownstreamProtocol().sendPacket(pack);
            }
        //} else {
        //    DragonProxy.getLogger().warning(sender + "Ignoring packet from unconnected client " + session.getSessionID());
        //}
    }

    @Override
    public void handlePacket(RakNetClientSession session, RakNetPacket packet, int channel) {
        if (!sessionList.containsKey(session.getGloballyUniqueId())) {
            DragonProxy.getLogger().warning(sender + "Session " + session.getAddress() + " didn't exist");
            sessionList.put(session.getGloballyUniqueId(), getSessionUUID(session.getGloballyUniqueId()));
        }
        DragonProxy.getLogger().debug(sender + "Handling Packet from Channel: " + channel + ": " + Integer.toHexString(packet.buffer().getByte(1)));
        handlePacket(packet, DragonProxy.getSelf().getNetwork().getSessionRegister().getSession(getSessionUUID(session.getGloballyUniqueId())));
    }

    @Override
    public void sendPacket(RakNetPacket packet, ClientConnection session) {
        if (session == null) {
            return;
        }
        DragonProxy.getLogger().debug(sender + "Sending Packet: " + session.getSessionID() + ": " + Integer.toHexString(packet.buffer().getByte(1)));
        packet.encode();
        server.sendMessage(getSessionID(session.getSessionID()), Reliability.UNRELIABLE, packet);
    }

    public Long getSessionID(UUID id) {
        if (sessionList.containsValue(id)) {
            for (Map.Entry<Long, UUID> entry : sessionList.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        System.out.println("Ret null");
        return null;
    }

    public UUID getSessionUUID(long id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes());
    }

    @Override
    public void onAcknowledge(RakNetClientSession session, Record record, Reliability reliability, int channel, RakNetPacket packet) {
        DragonProxy.getLogger().debug(sender + "Recieved ACK for packet " + packet.getId());
    }

    @Override
    public void onAddressBlocked(InetAddress address, long time) {
        DragonProxy.getLogger().info(sender + "Proxy Blocked Client: " + address + " for " + time);
    }

    @Override
    public void onAddressUnblocked(InetAddress address) {
        DragonProxy.getLogger().info(sender + "Proxy Unblocked Client: " + address);
    }

    @Override
    public void onClientConnect(RakNetClientSession session) {
        DragonProxy.getLogger().info(sender + "Client Connected: " + session.getAddress());
        UUID sessionID = getSessionUUID(session.getGloballyUniqueId());
        ClientConnection clientSession = new ClientConnection(this, sessionID);
        if (!DragonProxy.getSelf().getNetwork().getSessionRegister().acceptConnection(clientSession)) {
            server.removeSession(session);
        } else {
            sessionList.put(session.getGloballyUniqueId(), sessionID);
            clientSession.onConnected();
            clientSession.setStatus(ConnectionStatus.AWAITING_CLIENT_LOGIN);
        }
    }

    @Override
    public void onClientDisconnect(RakNetClientSession session, String reason) {
        DragonProxy.getLogger().info(sender + "Client Disconnected: " + session.getAddress() + "; Reason: " + reason);
    }

    @Override
    public void onClientPreConnect(InetSocketAddress address) {
        DragonProxy.getLogger().info(sender + "Client " + address + " about to connect");
    }

    @Override
    public void onClientPreDisconnect(InetSocketAddress address, String reason) {
        DragonProxy.getLogger().info(sender + "Client " + address + " about to disconnect for reason: " + reason);
    }

    @Override
    public void onHandlerException(InetSocketAddress address, Throwable throwable) {
        DragonProxy.getLogger().severe(sender + "An unhandled exception has occured with the session: " + address);
        throwable.printStackTrace();
    }

    @Override
    public void onNotAcknowledge(RakNetClientSession session, Record record, Reliability reliability, int channel, RakNetPacket packet) {
        DragonProxy.getLogger().debug(sender + "Did not recieve ACK for packet " + packet.getId());
    }

    @Override
    public void onServerShutdown() {
        DragonProxy.getLogger().info(sender + "Server shutting down");
    }

    @Override
    public void onServerStart() {
        DragonProxy.getLogger().info(sender + "Listening for PE connections");
    }

    @Override
    public void onThreadException(Throwable throwable) {
        DragonProxy.getLogger().severe(sender + "An unhandled thread exception has occured");
        throwable.printStackTrace();
    }

    @Override
    public void clientDisconectRequest(ClientConnection id, String reason) {
        DragonProxy.getLogger().info(sender + "Proxy has commanded " + id + " to disconnect for the reason " + reason);
        Long sessionID = getSessionID(id.getSessionID());
        if (sessionID != null) {
            server.removeSession(server.getSession(sessionID));
        } else {
            DragonProxy.getLogger().warning(sender + "Could not disconnect client: " + id + "; The session ID was null");
        }
    }

    @Override
    public void handlePing(ServerPing ping) {
        DragonProxy.getLogger().debug(sender + "Ping From Client: " + ping.getSender());
    }

    @Override
    public void onTick() {
        if (server == null) {
            identifier = new MCPEIdentifier(DragonProxy.getSelf().getNetwork().getMotd(),
                    Versioning.MINECRAFT_PE_PROTOCOL,
                    Versioning.MINECRAFT_PE_VERSION,
                    DragonProxy.getSelf().getNetwork().getSessionRegister().getOnlineCount(),
                    DragonProxy.getSelf().getConfig().getMax_players(),
                    0,
                    "DragonProxyWorld",
                    "creative");

            server = new RakNetServer(DragonProxy.getSelf().getConfig().getUdp_bind_port(),
                    DragonProxy.getSelf().getConfig().getMax_players(),
                    1464,
                    identifier);
            server.setListener(this);
            server.startThreaded();
        }
    }

    public void onClientLoginRequest(Login packet, ClientConnection session) {
        session.setStatus(ConnectionStatus.CONNECTING_CLIENT);
        if (session.getUsername() != null) {
            clientDisconectRequest(session, "Already logged in, this must be an error! ");
            return;
        }

        PlayStatus status = new PlayStatus(); // Required; Tells the client that his connection was accepted or denied
        if (packet.protocol != Versioning.MINECRAFT_PE_PROTOCOL) {
            status.status = (packet.protocol < Versioning.MINECRAFT_PE_PROTOCOL ? PlayStatus.OUTDATED_CLIENT : PlayStatus.OUTDATED_SERVER);
            sendPacket(new RakNetPacket(prep(status.encode())), session);
            clientDisconectRequest(session, DragonProxy.getSelf().getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }
        
        LoginPacketPayload data = LoginPacketPayload.decode(packet.body);
        session.setUsername(data.getUsername());

        DragonProxy.getLogger().info(DragonProxy.getSelf().getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, session.getUsername(), ""));

        session.setStatus(ConnectionStatus.CONNECTED);
        switch (DragonProxy.getSelf().getAuthMode()) {
            /*case "online":
                // We must send enough packets to make chat accessable on the client
                minimalClientHandshake(false);
                this.status = ConnectionStatus.AWAITING_CLIENT_AUTHENTICATION;
                dataCache.put(CacheKey.AUTHENTICATION_STATE, "email");

                sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_NOTICE, username));
                sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_EMAIL));
                break;
            case "cls":
                this.status = ConnectionStatus.AWAITING_CLIENT_AUTHENTICATION;
                authenticateCLSMode();
                break;*/
            case "offline":
                // We translate everything we are sent without regard for what it is
                //minimalClientHandshake(false, session);

                DragonProxy.getLogger().debug(sender + "Initially joining [" + DragonProxy.getSelf().getConfig().getDefault_server() + "]... ");
                session.connectToServer(DragonProxy.getSelf().getConfig().getRemote_servers().get(DragonProxy.getSelf().getConfig().getDefault_server()));
                break;
            default:
                String error = "Unsupported authentication mode " + DragonProxy.getSelf().getAuthMode();
                DragonProxy.getLogger().warning(sender + error);
                clientDisconectRequest(session, error);
        }
    }

    @Override
    public Class<RakNetPacket> getSupportedPacketType() {
        return RakNetPacket.class;
    }

    /*public void authenticateCLSMode() {
        //CLS LOGIN! 
        if ((username.length() < 6 + 1 + 1) || (!username.contains("_"))) {
            // Disconnect the player if their username can't possibly be a valid cls mode name
            sendStartGameAndDisconnect(DragonProxy.getSelf().getLang().get(Lang.MESSAGE_CLS_NOTICE));
            return;
        }
        String name = username.substring(0, username.length() - 7);
        String keyCode = username.substring(username.length() - 6);
        String resp = HTTP.performGetRequest("http://api.dragonet.org/cls/query_token.php?" + String.format("username=%s&keycode=%s", name, keyCode));
        if (resp == null) {
            sendStartGameAndDisconnect(DragonProxy.getSelf().getLang().get(Lang.MESSAGE_SERVER_ERROR, DragonProxy.getSelf().getLang().get(Lang.ERROR_CLS_UNREACHABLE)));
            proxy.getLogger().severe(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)).replace("§c", "").replace("§0", ""));
            return;
        }
        JsonElement json = null;
        try {
            JsonParser jsonParser = new JsonParser();
            json = jsonParser.parse(resp);
        } catch (Exception e) {
            sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
            proxy.getLogger().severe(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)).replace("§c", "").replace("§0", ""));
            //Json parse error! 
            return;
        }
        JsonObject obj = json.getAsJsonObject();
        if (!obj.get("status").getAsString().equals("success")) {
            sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_CLS_NOTICE));
            return;
        }
        AuthenticationService authSvc = new AuthenticationService(obj.get("client").getAsString());
        authSvc.setUsername(obj.get("ign").getAsString());
        authSvc.setAccessToken(obj.get("token").getAsString());
        try {
            authSvc.login();
        } catch (RequestException ex) {
            ex.printStackTrace();
            sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
            return;
        }
        username = authSvc.getSelectedProfile().getName();
        HTTP.performGetRequest("http://api.dragonet.org/cls/update_token.php?" + String.format("username=%s&oldtoken=%s&newtoken=%s", name, obj.get("token").getAsString(), authSvc.getAccessToken()));
        protocol = new MinecraftProtocol(authSvc.getSelectedProfile(), authSvc.getAccessToken());

        proxy.getLogger().debug("Initially joining [" + proxy.getConfig().getDefault_server() + "]... ");
        connectToServer(proxy.getConfig().getRemote_servers().get(proxy.getConfig().getDefault_server()));
    }

    public void authenticateOnlineMode(String password) {
        proxy.getGeneralThreadPool().execute(() -> {
            try {
                protocol = new MinecraftProtocol((String) dataCache.get(CacheKey.AUTHENTICATION_EMAIL), password, false);
            } catch (RequestException ex) {
                if (ex.getMessage().toLowerCase().contains("invalid")) {
                    sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_FAILD));
                    disconnect(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_FAILD));
                    return;
                } else {
                    sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    disconnect(proxy.getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    return;
                }
            }

            if (!username.equals(protocol.getProfile().getName())) {
                username = protocol.getProfile().getName();
                sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_USERNAME, username));
            }

            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_SUCCESS, username));

            proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_SUCCESS_CONSOLE, username, remoteAddress, username));
            connectToServer(proxy.getConfig().getRemote_servers().get(proxy.getConfig().getDefault_server()));
        });
    }

    public void sendStartGameAndDisconnect(String reason) {
        //Login error so player in nether (Red screen)
        minimalClientHandshake(true);
        sendChat(reason);
        disconnect(reason);
    }*/
    
    private byte[] prep(byte[] buff) {
        byte[] buff2 = new byte[buff.length + 1];
        int index = 0;
        buff2[index++] = (byte) 0xFE;
        for (byte b : buff) {
            buff2[index++] = b;
        }
        return buff2;
    }

    private void minimalClientHandshake(boolean errorMode, ClientConnection session) {
        PlayStatus status = new PlayStatus(); // Required; TODO: Find out why
        status.status = PlayStatus.OK;
        //sendPacket(new RakNetPacket(prep(status.encode())), session);
        //sendPacket(status, session.getSessionID());

        //sendPacket(new RakNetPacket(prep(new ResourcePacksInfo(false, new sul.protocol.pocket100.types.Pack[0], new sul.protocol.pocket100.types.Pack[0]).encode())), session);
        //sendPacket(new ResourcePacksInfo(), session.getSessionID());  // Causes the client to switch to the "locating server" screen

        StartGame startGamePacket = new StartGame(); // Required; Makes the client switch to the "generating world" screen
        startGamePacket.entityId = 1;
        startGamePacket.runtimeId = 1;
        //startGamePacket.entityRuntimeId = 52;
        //startGamePacket.x = (float) 0.0;
        //startGamePacket.y = (float) 72F;
        //startGamePacket.z = (float) 0.0;
        //startGamePacket.seed = (int)41568156263L;
        startGamePacket.dimension = (byte) ((errorMode ? 1 : 0) & 0xff);
        startGamePacket.worldGamemode = 1;
        startGamePacket.difficulty = 1;
        //startGamePacket.spawnX = (int) 0.0;
        //startGamePacket.spawnY = (int) 72;
        //startGamePacket.spawnZ = (int) 0.0;
        //startGamePacket.hasAchievementsDisabled = true;
        //startGamePacket.dayCycleStopTime = -1;
        //startGamePacket.eduMode = false;
        startGamePacket.rainLevel = 0;
        //startGamePacket.lightningLevel = 0;
        //startGamePacket.commandsEnabled = true;
        startGamePacket.levelId = Base64.getEncoder().encodeToString("world".getBytes());
        startGamePacket.worldName = "world"; // Must not be null or a NullPointerException will occur
        startGamePacket.generator = 1; //0 old, 1 infinite, 2 flat
        startGamePacket.position = new Tuples.FloatXYZ();
        startGamePacket.spawnPosition = new Tuples.IntXYZ();
        //sendPacket(new RakNetPacket(prep(startGamePacket.encode())), session);

        SetSpawnPosition pkSpawn = new SetSpawnPosition();
        pkSpawn.position = new BlockPosition();
        //sendPacket(new RakNetPacket(prep(pkSpawn.encode())), session);

        /*MovePlayer pkMovePlayer = new MovePlayer();
        pkMovePlayer.eid = 52;
        pkMovePlayer.x = (float) 0;
        pkMovePlayer.y = (float) 72;
        pkMovePlayer.z = (float) 0;
        pkMovePlayer.headYaw = 0.0f;
        pkMovePlayer.yaw = 0.0f;
        pkMovePlayer.pitch = 0.0f;
        pkMovePlayer.onGround = false;
        pkMovePlayer.mode = MovePlayer.MODE_RESET;
        sendPacket(pkMovePlayer, session.getSessionID());

        SetTime setTimePacket = new SetTime();
        setTimePacket.time = 1000;
        setTimePacket.started = true;
        sendPacket(setTimePacket, session.getSessionID());

        SetDifficulty pkSetDiff = new SetDifficulty();
        pkSetDiff.difficulty = 1;
        sendPacket(pkSetDiff, session.getSessionID());

        AdventureSettings pkAdventureSettings = new AdventureSettings();
        pkAdventureSettings.allowFlight = true;
        pkAdventureSettings.isFlying = false;
        pkAdventureSettings.flags = 4;
        sendPacket(pkAdventureSettings, session.getSessionID());

        UpdateAttributes pkUpdateAttr = new UpdateAttributes();
        Attribute.init();
        pkUpdateAttr.entries = new Attribute[]{
            Attribute.getAttribute(Attribute.ABSORPTION),
            Attribute.getAttribute(Attribute.SATURATION),
            Attribute.getAttribute(Attribute.EXHAUSTION),
            Attribute.getAttribute(Attribute.KNOCKBACK_RESISTANCE),
            Attribute.getAttribute(Attribute.MAX_HEALTH),
            Attribute.getAttribute(Attribute.MOVEMENT_SPEED),
            Attribute.getAttribute(Attribute.FOLLOW_RANGE),
            Attribute.getAttribute(Attribute.MAX_HUNGER),
            Attribute.getAttribute(Attribute.ATTACK_DAMAGE),
            Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL),
            Attribute.getAttribute(Attribute.EXPERIENCE)
        };
        //sendPacket(pkUpdateAttr, true);

        CraftingData pkCraftData = new CraftingData();
        pkCraftData.entries = Collections.EMPTY_LIST;
        sendPacket(pkCraftData, session.getSessionID());

        SetEntityData pkEntityData = new SetEntityData();
        pkEntityData.eid = 52;
        pkEntityData.metadata = new EntityMetadata();
        sendPacket(pkEntityData, session.getSessionID());

        ContainerSetContent containerSetContentPacket = new ContainerSetContent();
        containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
        containerSetContentPacket.slots = Item.getCreativeItems().stream().toArray(Item[]::new);
        sendPacket(containerSetContentPacket, session.getSessionID());

        //sendFlatChunks(0, 0, 10, false, session);
        //sendFlatChunks(0, 0, 17, false, session);*/
 /*ChunkRadiusUpdated pkChunkRadius = new ChunkRadiusUpdated();
        pkChunkRadius.radius = 3;
        sendPacket(new RakNetPacket(pkChunkRadius.encode()), session);

        /*SetCommandsEnabled pk = new SetCommandsEnabled();
        pk.enabled = true;
        sendPacket(pk, true);*/

 /*Respawn pkResp = new Respawn();
        pkResp.position = new Tuples.FloatXYZ();
        sendPacket(new RakNetPacket(pkResp.encode()), session);
         */
        /*PlayStatus pkStat = new PlayStatus(); //Required; Spawns the client in the world and closes the loading screen
        pkStat.status = PlayStatus.SPAWNED;
        sendPacket(new RakNetPacket(prep(pkStat.encode())), session);*/
    }

    /*private void sendFlatChunks(int playerX, int playerZ, int circleRadius, boolean sendAir, ClientConnection session) {
        int blocksX = 1;
        int blocksZ = 1;

        boolean cx = false; // Centered?
        // float circleRadius = player.getRenderDistance(); // Circle
        // Radius
        int maxBlocksX, maxBlocksZ;

        if (!cx) {
            maxBlocksX = (int) (Math.ceil((circleRadius - blocksX / 2) / blocksX) * 2 + 1);
            maxBlocksZ = (int) (Math.ceil((circleRadius - blocksZ / 2) / blocksZ) * 2 + 1);
        } else {
            maxBlocksX = (int) (Math.ceil(circleRadius / blocksX) * 2);
            maxBlocksZ = (int) (Math.ceil(circleRadius / blocksZ) * 2);
        }

        // TODO: Cache the chunk circle
        // Calculate the chunk ring
        //ArrayList<Vector3D> loadChunksList = new ArrayList<>();
        for (int z = -maxBlocksZ / 2; z <= maxBlocksZ / 2; z++) {
            for (int x = -maxBlocksX / 2; x <= maxBlocksX / 2; x++) {
                double distance = Math.sqrt(Math.pow(z * blocksZ, 2) + Math.pow(x * blocksX, 2));
                boolean shouldSendChunk = (distance < circleRadius);
                if (shouldSendChunk) {
                    sendPacket(getFlatChunkPacket(x, z, (sendAir ? 0 : 1)), session.getSessionID());
                    //loadChunksList.add(new Vector3D(nx - x, 0, nz - z));
                }
            }
        }
    }

    private DataPacket getFlatChunkPacket(int chunkX, int chunkZ, int blockId) {
        FullChunkDataPacket pePacket = new FullChunkDataPacket();

        cn.nukkit.level.format.anvil.Chunk chunk = cn.nukkit.level.format.anvil.Chunk.getEmptyChunk(chunkX, chunkZ);

        pePacket.chunkX = chunkX;
        pePacket.chunkZ = chunkZ;

        for (int yPC = 0; yPC < 128; yPC++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunk.setBlockId(x, yPC, z, blockId);
                    chunk.setBlockData(x, yPC, z, 0);
                    chunk.setBlockLight(x, yPC, z, 15);
                    chunk.setBlockSkyLight(x, yPC, z, 15);
                    chunk.setBiomeId(x, z, 0);
                }
            }
        }

        pePacket.data = chunk.toFastBinary();
        return pePacket;
    }

    /*public void onClientLoginRequest(LoginPacket packet, String identifier) {
        ClientConnection session = getSession(identifier);
        if (session == null) {
            return;
        }

        session.setStatus(ConnectionStatus.CONNECTING_CLIENT);
        if (session.getUsername() != null) {
            handler.closeSession(identifier, "Already logged in, this must be an error! ");
            return;
        }

        PlayStatusPacket status = new PlayStatusPacket(); // Required; Tells the client that his connection was accepted or denied
        if (packet.protocol != Versioning.MINECRAFT_PE_PROTOCOL) {
            status.status = (packet.protocol < Versioning.MINECRAFT_PE_PROTOCOL ? PlayStatusPacket.LOGIN_FAILED_CLIENT : PlayStatusPacket.LOGIN_FAILED_SERVER);
            sendPacket(status, session.getSessionID());
            handler.closeSession(identifier, DragonProxy.getSelf().getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }

        session.setUsername(packet.username);
        DragonProxy.getLogger().info(DragonProxy.getSelf().getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, packet.username, identifier));

        switch (DragonProxy.getSelf().getAuthMode()) {
            /*case "online":
                // We must send enough packets to make chat accessable on the client
                minimalClientHandshake(false);
                this.status = ConnectionStatus.AWAITING_CLIENT_AUTHENTICATION;
                dataCache.put(CacheKey.AUTHENTICATION_STATE, "email");

                sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_NOTICE, username));
                sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_EMAIL));
                break;
            case "cls":
                this.status = ConnectionStatus.AWAITING_CLIENT_AUTHENTICATION;
                authenticateCLSMode();
                break;*//*
            case "offline":
                // We translate everything we are sent without regard for what it is
                minimalClientHandshake(false, session);
                DragonProxy.getLogger().debug("Initially joining [" + DragonProxy.getSelf().getConfig().getDefault_server() + "]... ");
                session.connectToServer(DragonProxy.getSelf().getConfig().getRemote_servers().get(DragonProxy.getSelf().getConfig().getDefault_server()));
                break;
        }
    }*/
}
