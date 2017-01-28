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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
import org.dragonet.proxy.api.network.DragonPacket;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.PacketTranslatorRegister;
import org.dragonet.proxy.utilities.Versioning;

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

    @Override
    public void handlePacket(RakNetPacket packet, UUID identifier) {
        ClientConnection session = getSession(getSessionID(identifier));
        if (session == null) {
            DragonProxy.getLogger().debug("Session: " + identifier + " was null");
            return;
        }
        DragonProxy.getLogger().debug("[PE Clientside] Handling Packet" + session.getSessionID() + ": " + packet.getId());
        /*DragonProxy.getLogger().info(packet.getClass().getCanonicalName());
        if (session.getStatus() == ConnectionStatus.UNCONNECTED || session.getStatus() == ConnectionStatus.AWAITING_CLIENT_LOGIN) {
            if (packet.pid() == ProtocolInfo.LOGIN_PACKET) {
                try {
                    onClientLoginRequest((LoginPacket) packet, identifier);
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

        Object[] packets = {packet};
        if (session.getDownstreamProtocol().getSupportedPacketType() != getSupportedPacketType()) {
            packets = PacketTranslatorRegister.translateToPC(session, packet);
        }

        for (Object pack : packets) {
            session.getDownstreamProtocol().sendPacket(pack);
        }
    }

    @Override
    public void handlePacket(RakNetClientSession session, RakNetPacket packet, int channel) {
        if (!sessionList.containsKey(session.getGloballyUniqueId())) {
            DragonProxy.getLogger().warning("Session " + session.getAddress() + " didn't exist");
            sessionList.put(session.getGloballyUniqueId(), DragonProxy.getSelf().getNetwork().getSessionRegister().getNextSessionID());
        }
        handlePacket(packet, getSession(session.getGloballyUniqueId()).getSessionID());
    }

    @Override
    public void sendPacket(RakNetPacket packet, UUID id) {
        ClientConnection session = DragonProxy.getSelf().getNetwork().getSessionRegister().getSession(id);
        if (session == null) {
            return;
        }
        DragonProxy.getLogger().debug("[PE Clientside] Sending Packet " + session.getSessionID() + ": " + packet.getId());
        server.sendMessage(getSessionID(id), Reliability.RELIABLE, packet);
    }

    public Long getSessionID(UUID id) {
        if (sessionList.containsValue(id)) {
            for (Map.Entry<Long, UUID> entry : sessionList.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public ClientConnection getSession(Long identifier) {
        UUID id = sessionList.get(identifier);
        if (id != null) {
            ClientConnection session = DragonProxy.getSelf().getNetwork().getSessionRegister().getSession(id);
            if (session != null) {
                return session;
            }
        }
        return null;
    }

    @Override
    public void onAcknowledge(RakNetClientSession session, Record record, Reliability reliability, int channel, RakNetPacket packet) {
    }

    @Override
    public void onAddressBlocked(InetAddress address, long time) {
        DragonProxy.getLogger().info("Proxy Blocked Client: " + address + " for " + time);
    }

    @Override
    public void onAddressUnblocked(InetAddress address) {
        DragonProxy.getLogger().info("Proxy Unblocked Client: " + address);
    }

    @Override
    public void onClientConnect(RakNetClientSession session) {
        DragonProxy.getLogger().info("Client Connected: " + session.getAddress());
        UUID sessionID = DragonProxy.getSelf().getNetwork().getSessionRegister().getNextSessionID();
        ClientConnection clientSession = new ClientConnection(this, sessionID);
        if (!DragonProxy.getSelf().getNetwork().getSessionRegister().acceptConnection(clientSession)) {
            server.removeSession(session);
        } else {
            sessionList.put(session.getGloballyUniqueId(), sessionID);
            clientSession.onConnected();
            clientSession.connectToServer(DragonProxy.getSelf().getConfig().getRemote_servers().get(DragonProxy.getSelf().getConfig().getDefault_server()));
        }
    }

    @Override
    public void onClientDisconnect(RakNetClientSession session, String reason) {
        DragonProxy.getLogger().info("Client Disconnected: " + session.getAddress() + "; Reason: " + reason);
    }

    @Override
    public void onClientPreConnect(InetSocketAddress address) {
        DragonProxy.getLogger().info("Client " + address + " about to connect");
    }

    @Override
    public void onClientPreDisconnect(InetSocketAddress address, String reason) {
        DragonProxy.getLogger().info("Client " + address + " about to disconnect for reason: " + reason);
    }

    @Override
    public void onHandlerException(InetSocketAddress address, Throwable throwable) {
        DragonProxy.getLogger().severe("An unhandled exception has occured with the session: " + address);
        throwable.printStackTrace();
    }

    @Override
    public void onNotAcknowledge(RakNetClientSession session, Record record, Reliability reliability, int channel, RakNetPacket packet) {
    }

    @Override
    public void onServerShutdown() {
        DragonProxy.getLogger().info("Server shutting down");
    }

    @Override
    public void onServerStart() {
        DragonProxy.getLogger().info("Listening for PE connections");
    }

    @Override
    public void onThreadException(Throwable throwable) {
        DragonProxy.getLogger().severe("An unhandled thread exception has occured");
        throwable.printStackTrace();
    }

    @Override
    public void clientDisconectRequest(UUID id, String reason) {
        DragonProxy.getLogger().info("Proxy has commanded " + id + " to disconnect for the reason " + reason);
        Long sessionID = getSessionID(id);
        if (sessionID != null) {
            server.removeSession(server.getSession(sessionID));
        } else {
            DragonProxy.getLogger().warning("Could not disconnect client: " + id + "; The session ID was null");
        }
    }

    @Override
    public void handlePing(ServerPing ping) {
        //DragonProxy.getLogger().debug("Ping From Client: " + ping.getSender());
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
                    1500,
                    identifier);
            server.setListener(this);
            server.startThreaded();
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
 /*private void minimalClientHandshake(boolean errorMode, ClientConnection session) {
        PlayStatusPacket status = new PlayStatusPacket(); // Required; TODO: Find out why
        status.status = PlayStatusPacket.LOGIN_SUCCESS;
        sendPacket(status, session.getSessionID());

        sendPacket(new ResourcePacksInfoPacket(), session.getSessionID());  // Causes the client to switch to the "locating server" screen

        StartGamePacket startGamePacket = new StartGamePacket(); // Required; Makes the client switch to the "generating world" screen
        startGamePacket.entityUniqueId = 52;
        startGamePacket.entityRuntimeId = 52;
        startGamePacket.x = (float) 0.0;
        startGamePacket.y = (float) 72F;
        startGamePacket.z = (float) 0.0;
        startGamePacket.seed = 242540254;
        startGamePacket.dimension = (byte) ((errorMode ? 1 : 0) & 0xff);
        startGamePacket.gamemode = 0;
        startGamePacket.difficulty = 1;
        startGamePacket.spawnX = (int) 0.0;
        startGamePacket.spawnY = (int) 72;
        startGamePacket.spawnZ = (int) 0.0;
        startGamePacket.hasAchievementsDisabled = true;
        startGamePacket.dayCycleStopTime = -1;
        startGamePacket.eduMode = false;
        startGamePacket.rainLevel = 0;
        startGamePacket.lightningLevel = 0;
        startGamePacket.commandsEnabled = true;
        startGamePacket.levelId = "";
        startGamePacket.worldName = ""; // Must not be null or a NullPointerException will occur
        startGamePacket.generator = 1; //0 old, 1 infinite, 2 flat
        sendPacket(startGamePacket, session.getSessionID());

        SetSpawnPositionPacket pkSpawn = new SetSpawnPositionPacket();
        pkSpawn.x = 0;
        pkSpawn.y = 72;
        pkSpawn.z = 0;
        sendPacket(pkSpawn, session.getSessionID());

        MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
        pkMovePlayer.eid = 52;
        pkMovePlayer.x = (float) 0;
        pkMovePlayer.y = (float) 72;
        pkMovePlayer.z = (float) 0;
        pkMovePlayer.headYaw = 0.0f;
        pkMovePlayer.yaw = 0.0f;
        pkMovePlayer.pitch = 0.0f;
        pkMovePlayer.onGround = false;
        pkMovePlayer.mode = MovePlayerPacket.MODE_RESET;
        sendPacket(pkMovePlayer, session.getSessionID());

        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.time = 1000;
        setTimePacket.started = true;
        sendPacket(setTimePacket, session.getSessionID());

        SetDifficultyPacket pkSetDiff = new SetDifficultyPacket();
        pkSetDiff.difficulty = 1;
        sendPacket(pkSetDiff, session.getSessionID());

        AdventureSettingsPacket pkAdventureSettings = new AdventureSettingsPacket();
        pkAdventureSettings.allowFlight = true;
        pkAdventureSettings.isFlying = false;
        pkAdventureSettings.flags = 4;
        sendPacket(pkAdventureSettings, session.getSessionID());

        UpdateAttributesPacket pkUpdateAttr = new UpdateAttributesPacket();
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

        CraftingDataPacket pkCraftData = new CraftingDataPacket();
        pkCraftData.entries = Collections.EMPTY_LIST;
        sendPacket(pkCraftData, session.getSessionID());

        SetEntityDataPacket pkEntityData = new SetEntityDataPacket();
        pkEntityData.eid = 52;
        pkEntityData.metadata = new EntityMetadata();
        sendPacket(pkEntityData, session.getSessionID());

        ContainerSetContentPacket containerSetContentPacket = new ContainerSetContentPacket();
        containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
        containerSetContentPacket.slots = Item.getCreativeItems().stream().toArray(Item[]::new);
        sendPacket(containerSetContentPacket, session.getSessionID());

        //sendFlatChunks(0, 0, 10, false, session);
        //sendFlatChunks(0, 0, 17, false, session);
        ChunkRadiusUpdatedPacket pkChunkRadius = new ChunkRadiusUpdatedPacket();
        pkChunkRadius.radius = 3;
        sendPacket(pkChunkRadius, session.getSessionID());

        /*        
        
        SetCommandsEnabledPacket pk = new SetCommandsEnabledPacket();
        pk.enabled = true;
        sendPacket(pk, true);
        
     *//*
        RespawnPacket pkResp = new RespawnPacket();
        pkResp.y = 72F;
        sendPacket(pkResp, session.getSessionID());

        PlayStatusPacket pkStat = new PlayStatusPacket(); //Required; Spawns the client in the world and closes the loading screen
        pkStat.status = PlayStatusPacket.PLAYER_SPAWN;
        sendPacket(pkStat, session.getSessionID());
    }

    private void sendFlatChunks(int playerX, int playerZ, int circleRadius, boolean sendAir, ClientConnection session) {
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
