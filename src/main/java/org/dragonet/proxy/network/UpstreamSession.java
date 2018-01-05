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
package org.dragonet.proxy.network;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.marfgamer.jraknet.protocol.Reliability;
import net.marfgamer.jraknet.session.RakNetClientSession;
import org.dragonet.proxy.DesktopServer;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.RemoteServer;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.cache.EntityCache;
import org.dragonet.proxy.network.cache.WindowCache;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.packets.DisconnectPacket;
import org.dragonet.proxy.protocol.packets.FullChunkDataPacket;
import org.dragonet.proxy.protocol.packets.LoginPacket;
import org.dragonet.proxy.protocol.packets.PlayStatusPacket;
import org.dragonet.proxy.protocol.packets.ResourcePackStackPacket;
import org.dragonet.proxy.protocol.packets.ResourcePacksInfoPacket;
import org.dragonet.proxy.protocol.packets.SetSpawnPositionPacket;
import org.dragonet.proxy.protocol.packets.StartGamePacket;
import org.dragonet.proxy.protocol.packets.TextPacket;
import org.dragonet.proxy.protocol.packets.UpdateBlockPacket;
import org.dragonet.proxy.protocol.type.chunk.ChunkData;
import org.dragonet.proxy.protocol.type.chunk.Section;
import org.dragonet.proxy.utilities.Binary;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.HTTP;
import org.dragonet.proxy.utilities.LoginChainDecoder;
import org.dragonet.proxy.utilities.Vector3F;
import org.dragonet.proxy.utilities.Zlib;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Maintaince the connection between the proxy and Minecraft: Pocket Edition
 * clients.
 */
public class UpstreamSession {

    private final DragonProxy proxy;
    private final String raknetID;
    private final RakNetClientSession raknetClient;
    private boolean loggedIn;
    private boolean spawned;
    private List<PEPacket> cachedPackets;
    private final InetSocketAddress remoteAddress;
    private final PEPacketProcessor packetProcessor;
    private final ScheduledFuture<?> packetProcessorScheule;
    private LoginChainDecoder profile;
    private String username;
    private IDownstreamSession downstream;

    /*
     * =============================================================================
	 * ========================== | Caches for Protocol Compatibility | /*
	 * =============================================================================
	 * ==========================
     */
    private final Map<String, Object> dataCache = Collections.synchronizedMap(new HashMap<String, Object>());
    private final Map<UUID, PlayerListEntry> playerInfoCache = Collections
        .synchronizedMap(new HashMap<UUID, PlayerListEntry>());
    private final EntityCache entityCache = new EntityCache(this);
    private final WindowCache windowCache = new WindowCache(this);
    protected boolean connecting;

    /*
	 * =============================================================================
	 * ==========================
     */
    private MinecraftProtocol protocol;

    public UpstreamSession(DragonProxy proxy, String raknetID, RakNetClientSession raknetClient,
                           InetSocketAddress remoteAddress) {
        this.proxy = proxy;
        this.raknetID = raknetID;
        this.remoteAddress = remoteAddress;
        this.raknetClient = raknetClient;
        packetProcessor = new PEPacketProcessor(this);
        packetProcessorScheule = proxy.getGeneralThreadPool().scheduleAtFixedRate(packetProcessor, 10, 50,
            TimeUnit.MILLISECONDS);
    }

    public DragonProxy getProxy() {
        return proxy;
    }

    public String getRaknetID() {
        return raknetID;
    }

    public RakNetClientSession getRaknetClient() {
        return raknetClient;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public PEPacketProcessor getPacketProcessor() {
        return packetProcessor;
    }

    public LoginChainDecoder getProfile() {
        return profile;
    }

    public String getUsername() {
        return username;
    }

    public IDownstreamSession getDownstream() {
        return downstream;
    }

    public Map<String, Object> getDataCache() {
        return dataCache;
    }

    public Map<UUID, PlayerListEntry> getPlayerInfoCache() {
        return playerInfoCache;
    }

    public EntityCache getEntityCache() {
        return entityCache;
    }

    public WindowCache getWindowCache() {
        return windowCache;
    }

    public void sendPacket(PEPacket packet) {
        sendPacket(packet, false);
    }

    public void sendPacket(PEPacket packet, boolean immediate) {
        if (packet == null)
            return;

        packet.encode();

        byte[] buffer;
        try {
            buffer = Zlib.deflate(
                Binary.appendBytes(Binary.writeUnsignedVarInt(packet.getBuffer().length), packet.getBuffer()), 6);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // handler.sendEncapsulated(identifier, encapsulated, RakNet.FLAG_NEED_ACK |
        // (overridedImmediate ? RakNet.PRIORITY_IMMEDIATE : RakNet.PRIORITY_NORMAL));
        raknetClient.sendMessage(Reliability.RELIABLE_ORDERED, 0,
            new net.marfgamer.jraknet.Packet(Binary.appendBytes((byte) 0xfe, buffer)));
    }

    public void sendAllPackets(PEPacket[] packets, boolean immediate) {
        if (packets.length < 5)
            for (PEPacket packet : packets)
                sendPacket(packet);
        /*
			 * else { Batch batch = new BatchPacket(); boolean mustImmediate = immediate; if
			 * (!mustImmediate) { for (PEPacket packet : packets) { if
			 * (packet.isShouldSendImmidate()) { batch.packets.add(packet); mustImmediate =
			 * true; break; } } } sendPacket(batch, mustImmediate); }
         */
    }

    public void connectToServer(RemoteServer server) {
        if (server == null)
            return;
        connecting = true;
        if (downstream != null && downstream.isConnected()) {
            spawned = false;
            cachedPackets = null;

            downstream.disconnect();
            // TODO: Send chat message about server change.

            // Remove all loaded entities
            /*
			 * BatchPacket batch = new BatchPacket();
			 * this.entityCache.getEntities().entrySet().forEach((ent) -> { if(ent.getKey()
			 * != 0){ batch.packets.add(new RemoveEntityPacket(ent.getKey())); } });
			 * this.entityCache.reset(true); sendPacket(batch, true);
             */
            return;
        }
        cachedPackets = new LinkedList<>();
        if (server.getClass().isAssignableFrom(DesktopServer.class)) {
            downstream = new PCDownstreamSession(proxy, this);
            ((PCDownstreamSession) downstream).protocol = protocol;
            downstream.connect(server.remote_addr, server.remote_port);
        } else
            // downstream = new PEDownstreamSession(proxy, this);
            // ((PEDownstreamSession)downstream).connect((PocketServer) server);
            disconnect("PE targets not supported yet");
    }

    public void onConnected() {
        connecting = false;
    }

    /**
     * Disconnected from server.
     *
     * @param reason
     */
    public void disconnect(String reason) {
        if (!connecting) {
            sendPacket(new DisconnectPacket(false, reason));
            raknetClient.update(); //Force the DisconnectPacket to be sent before we close the connection
        }
        //Forceing the connection to close
        getProxy().getNetwork().getRakServer().removeSession(getRaknetClient(), reason);
    }

    /**
     * Called when this client disconnects.
     *
     * @param reason The reason of disconnection.
     */
    public void onDisconnect(String reason) {
        proxy.getLogger().info(proxy.getLang().get(Lang.CLIENT_DISCONNECTED,
            proxy.getAuthMode().equals("cls") ? "unknown" : username, remoteAddress, reason));
        if (downstream != null)
            downstream.disconnect();
        proxy.getSessionRegister().removeSession(this);
        packetProcessorScheule.cancel(true);
    }

    public void authenticate(String email, String password) {
        proxy.getGeneralThreadPool().execute(() -> {
            try {
                protocol = new MinecraftProtocol(email, password, false);
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

            proxy.getLogger().info(
                proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_SUCCESS_CONSOLE, username, remoteAddress, username));
            connectToServer(proxy.getConfig().remote_servers.get(proxy.getConfig().default_server));
        });
    }

    public void onLogin(LoginPacket packet) {
        if (username != null) {
            disconnect("Already logged in, this must be an error! ");
            return;
        }

        getDataCache().put(CacheKey.PACKET_LOGIN_PACKET, packet);

        PlayStatusPacket status = new PlayStatusPacket();
        DragonProxy.getInstance().getLogger().debug("CLIENT PROTOCOL = " + packet.protocol);
        if (packet.protocol != ProtocolInfo.CURRENT_PROTOCOL) {
            status.status = PlayStatusPacket.LOGIN_FAILED_CLIENT;
            sendPacket(status, true);
            disconnect(proxy.getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }
        
        // Get the profile and read out the username!
        profile = packet.decoded;

        // Verify the integrity of the LoginPacket
        if (proxy.getConfig().authenticate_players && !packet.decoded.isLoginVerified()) {
            status.status = PlayStatusPacket.LOGIN_FAILED_INVALID_TENANT;
            sendPacket(status, true);
            disconnect(proxy.getLang().get(Lang.LOGIN_VERIFY_FAILED));
            return;
        }
        
        status.status = PlayStatusPacket.LOGIN_SUCCESS;
        sendPacket(status, true);

        this.username = profile.username;

        // Okay @dktapps ;)
        sendPacket(new ResourcePacksInfoPacket());

        // now wait for response
    }

    public void postLogin() {
        sendPacket(new ResourcePackStackPacket());

        loggedIn = true;
        proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, username, remoteAddress));
        if (proxy.getAuthMode().equals("online")) {
            proxy.getLogger().debug("Login online mode, sending placeholder datas");
            StartGamePacket pkStartGame = new StartGamePacket();
            pkStartGame.eid = 1L; // well we use 1 now
            pkStartGame.rtid = 1L;
            pkStartGame.dimension = 0;
            pkStartGame.seed = 0;
            pkStartGame.generator = 1;
            pkStartGame.difficulty = Difficulty.PEACEFUL;
            pkStartGame.spawnPosition = new BlockPosition(0, 72, 0);
            pkStartGame.position = new Vector3F(0f, 72f + EntityType.PLAYER.getOffset(), 0f);
            pkStartGame.levelId = "";
            pkStartGame.worldName = "World";
            pkStartGame.defaultPlayerPermission = 2;
            pkStartGame.commandsEnabled = true;
            pkStartGame.premiumWorldTemplateId = "";
            sendPacket(pkStartGame, true);

            SetSpawnPositionPacket pkSpawn = new SetSpawnPositionPacket();
            pkSpawn.position = new BlockPosition(0, 72, 0);
            sendPacket(pkSpawn, true);

            ChunkData data = new ChunkData();
            data.sections = new Section[16];
            for (int cy = 0; cy < 16; cy++) {
                data.sections[cy] = new Section();
                if (cy < 6)
                    Arrays.fill(data.sections[cy].blockIds, (byte) 1);
            }
            data.encode();
            sendPacket(new FullChunkDataPacket(0, 0, data.getBuffer()));
            sendPacket(new FullChunkDataPacket(0, -1, data.getBuffer()));
            sendPacket(new FullChunkDataPacket(-1, 0, data.getBuffer()));
            sendPacket(new FullChunkDataPacket(-1, -1, data.getBuffer()));

            dataCache.put(CacheKey.AUTHENTICATION_STATE, "online_login_wait");

            PlayStatusPacket pkStat = new PlayStatusPacket();
            pkStat.status = PlayStatusPacket.PLAYER_SPAWN;
            sendPacket(pkStat, true);

            sendChat(proxy.getLang().get(Lang.MESSAGE_LOGIN_PROMPT));
        } else if (proxy.getAuthMode().equals("cls")) {
            // CLS LOGIN!
            if ((username.length() < 6 + 1 + 1) || (!username.contains("_"))) {
                disconnect(proxy.getLang().get(Lang.MESSAGE_CLS_NOTICE));
                return;
            }
            String name = username.substring(0, username.length() - 7);
            String keyCode = username.substring(username.length() - 6);
            String resp = HTTP.performGetRequest("http://api.dragonet.org/cls/query_token.php?"
                + String.format("username=%s&keycode=%s", name, keyCode));
            if (resp == null) {
                disconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR,
                    proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)));
                proxy.getLogger()
                    .severe(proxy.getLang()
                        .get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE))
                        .replace("§c", "").replace("§0", ""));
                return;
            }
            JsonElement json;
            try {
                JsonParser jsonParser = new JsonParser();
                json = jsonParser.parse(resp);
            } catch (Exception e) {
                disconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
                proxy.getLogger()
                    .severe(proxy.getLang()
                        .get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR))
                        .replace("§c", "").replace("§0", ""));
                // Json parse error!
                return;
            }
            JsonObject obj = json.getAsJsonObject();
            if (!obj.get("status").getAsString().equals("success")) {
                disconnect(proxy.getLang().get(Lang.MESSAGE_CLS_NOTICE));
                return;
            }
            AuthenticationService authSvc = new AuthenticationService(obj.get("client").getAsString());
            authSvc.setUsername(obj.get("ign").getAsString());
            authSvc.setAccessToken(obj.get("token").getAsString());
            try {
                authSvc.login();
            } catch (RequestException ex) {
                ex.printStackTrace();
                disconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
                return;
            }
            username = authSvc.getSelectedProfile().getName();
            HTTP.performGetRequest("http://api.dragonet.org/cls/update_token.php?"
                + String.format("username=%s&oldtoken=%s&newtoken=%s", name, obj.get("token").getAsString(),
                authSvc.getAccessToken()));
            protocol = new MinecraftProtocol(authSvc.getSelectedProfile(), authSvc.getAccessToken());

            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().default_server + "]... ");
            connectToServer(proxy.getConfig().remote_servers.get(proxy.getConfig().default_server));
        } else {
            protocol = new MinecraftProtocol(username);
            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().default_server + "]... ");
            connectToServer(proxy.getConfig().remote_servers.get(proxy.getConfig().default_server));
        }
    }

    public void setSpawned() {
        spawned = true;

        if (cachedPackets != null) {
            cachedPackets.stream().forEach(this::sendPacket);

            PlayStatusPacket play = new PlayStatusPacket(PlayStatusPacket.PLAYER_SPAWN);
            sendPacket(play);

            cachedPackets = null;
        }
    }

    public void sendChat(String chat) {
        if (chat.contains("\n")) {
            String[] lines = chat.split("\n");
            for (String line : lines)
                sendChat(line);
            return;
        }
        TextPacket text = new TextPacket(); // raw
        text.type = TextPacket.TYPE_RAW;
        text.message = chat;
        sendPacket(text, true);
    }

    public void sendFakeBlock(int x, int y, int z, int id, int meta) {
        UpdateBlockPacket pkBlock = new UpdateBlockPacket();
        pkBlock.id = id;
        pkBlock.data = meta;
        pkBlock.flags = UpdateBlockPacket.FLAG_NEIGHBORS;
        pkBlock.blockPosition = new BlockPosition(x, y, z);
        sendPacket(pkBlock, true);
    }

    public void handlePacketBinary(byte[] packet) {
        packetProcessor.putPacket(packet);
    }

    public void putCachePacket(PEPacket p) {
        if (p == null)
            return;
        if (spawned || cachedPackets == null) {
            // System.out.println("Not caching since already spawned! ");
            sendPacket(p);
            return;
        }
        cachedPackets.add(p);
    }

    public void onTick() {
        entityCache.onTick();
        if (downstream != null)
            downstream.onTick();
    }
}
