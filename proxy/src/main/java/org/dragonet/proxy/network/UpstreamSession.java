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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import net.marfgamer.jraknet.protocol.Reliability;
import net.marfgamer.jraknet.session.RakNetClientSession;
import org.dragonet.proxy.DesktopServer;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.RemoteServer;
import org.dragonet.proxy.network.cache.EntityCache;
import org.dragonet.proxy.network.cache.WindowCache;
import org.dragonet.proxy.utilities.*;
import sul.protocol.bedrock137.play.*;
import sul.protocol.bedrock137.types.BlockPosition;
import sul.utils.Packet;
import sul.utils.Tuples;

/**
 * Maintaince the connection between the proxy and Minecraft: Pocket Edition
 * clients.
 */
public class UpstreamSession {

    @Getter
    private final DragonProxy proxy;

    @Getter
    private final String raknetID;

    @Getter
    private final RakNetClientSession raknetClient;

    @Getter
    private final InetSocketAddress remoteAddress;

    @Getter
    private final PEPacketProcessor packetProcessor;

    private final ScheduledFuture<?> packetProcessorScheule;

    @Getter
    private LoginChainDecoder profile;

    @Getter
    private String username;

    @Getter
    private DownstreamSession downstream;

    /* =======================================================================================================
     * |                                 Caches for Protocol Compatibility                                   |
    /* ======================================================================================================= */
    @Getter
    private final Map<String, Object> dataCache = Collections.synchronizedMap(new HashMap<String, Object>());

    @Getter
    private final Map<UUID, PlayerListEntry> playerInfoCache = Collections.synchronizedMap(new HashMap<UUID, PlayerListEntry>());

    @Getter
    private final EntityCache entityCache = new EntityCache(this);

    @Getter
    private final WindowCache windowCache = new WindowCache(this);

    protected boolean connecting;
    
    /* ======================================================================================================= */
    private MinecraftProtocol protocol;

    public UpstreamSession(DragonProxy proxy, String raknetID, RakNetClientSession raknetClient, InetSocketAddress remoteAddress) {
        this.proxy = proxy;
        this.raknetID = raknetID;
        this.remoteAddress = remoteAddress;
        this.raknetClient = raknetClient;
        packetProcessor = new PEPacketProcessor(this);
        packetProcessorScheule = proxy.getGeneralThreadPool().scheduleAtFixedRate(packetProcessor, 10, 50, TimeUnit.MILLISECONDS);
    }

    public void sendPacket(Packet packet) {
        sendPacket(packet, false);
    }

    public void sendPacket(Packet packet, boolean immediate) {
        System.out.println("Sending [" + packet.getClass().getSimpleName() + "] ... ");

        packet.encode();

        byte[] buffer;
        try {
            buffer = Zlib.deflate(
                    Binary.appendBytes(Binary.writeUnsignedVarInt(packet.getBuffer().length), packet.getBuffer()),
                    6
            );
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // handler.sendEncapsulated(identifier, encapsulated, RakNet.FLAG_NEED_ACK | (overridedImmediate ? RakNet.PRIORITY_IMMEDIATE : RakNet.PRIORITY_NORMAL));
        raknetClient.sendMessage(Reliability.RELIABLE_ORDERED, 0, new net.marfgamer.jraknet.Packet(Binary.appendBytes((byte) 0xfe, buffer)));
    }

    public void sendAllPackets(Packet[] packets, boolean immediate) {
        if (packets.length < 5) {
            for (Packet packet : packets) {
                sendPacket(packet);
            }
        }/* else {
            Batch batch = new BatchPacket();
            boolean mustImmediate = immediate;
            if (!mustImmediate) {
                for (PEPacket packet : packets) {
                    if (packet.isShouldSendImmidate()) {
                        batch.packets.add(packet);
                        mustImmediate = true;
                        break;
                    }
                }
            }
            sendPacket(batch, mustImmediate);
        }*/
    }

    public void onTick() {
        entityCache.onTick();
        if(downstream != null)
            downstream.onTick();
    }

    /**
     * Disconnected from server. 
     * @param reason 
     */
    public void disconnect(String reason) {
        if(!connecting) {
            sendPacket(new Disconnect(false, reason));
            //RakNet server will call onDisconnect()
        }
    }

    /**
     * Called when this client disconnects.
     *
     * @param reason The reason of disconnection.
     */
    public void onDisconnect(String reason) {
        proxy.getLogger().info(proxy.getLang().get(Lang.CLIENT_DISCONNECTED, proxy.getAuthMode().equals("cls") ? "unknown" : username, remoteAddress, reason));
        if (downstream != null) {
            downstream.disconnect();
        }
        proxy.getSessionRegister().removeSession(this);
        packetProcessorScheule.cancel(true);
    }

    public void handlePacketBinary(byte[] packet) {
        packetProcessor.putPacket(packet);
    }

    public void onLogin(Login packet) {
        if (username != null) {
            disconnect("Already logged in, this must be an error! ");
            return;
        }

        PlayStatus status = new PlayStatus();
        System.out.println("CLIENT PROTOCOL = " + packet.protocol);
        if (packet.protocol != Versioning.MINECRAFT_PE_PROTOCOL) {
            status.status = PlayStatus.OUTDATED_CLIENT;
            sendPacket(status, true);
            disconnect(proxy.getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }
        status.status = PlayStatus.OK;
        sendPacket(status, true);

        System.out.println("chain len = " + packet.body.chain);

        profile = new LoginChainDecoder(packet.body);
        profile.decode();

        this.username = profile.username;
        System.out.println("decoded username: " + this.username);
        proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, username, remoteAddress));
        if (proxy.getAuthMode().equals("online")) {
            StartGame pkStartGame = new StartGame();
            pkStartGame.entityId = 0; //Use EID 0 for eaisier management
            pkStartGame.dimension = (byte) 0;
            pkStartGame.seed = 0;
            pkStartGame.generator = 1;
            pkStartGame.spawnPosition = new Tuples.IntXYZ(0, 0, 0);
            pkStartGame.position = new Tuples.FloatXYZ(0f, 72f, 0f);
            pkStartGame.levelId = "World";
            pkStartGame.worldName = "World";
            pkStartGame.premiumWorldTemplate = "";
            sendPacket(pkStartGame, true);

            SetSpawnPosition pkSpawn = new SetSpawnPosition();
            pkSpawn.position = new BlockPosition(0, 72, 0);
            sendPacket(pkSpawn, true);

            sendPacket(new ResourcePacksInfo(), true);

            PlayStatus pkStat = new PlayStatus();
            pkStat.status = PlayStatus.SPAWNED;
            sendPacket(pkStat, true);

            dataCache.put(CacheKey.AUTHENTICATION_STATE, "email");

            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_NOTICE, username));
            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_EMAIL));
        } else if (proxy.getAuthMode().equals("cls")) {
            //CLS LOGIN! 
            if ((username.length() < 6 + 1 + 1) || (!username.contains("_"))) {
                disconnect(proxy.getLang().get(Lang.MESSAGE_CLS_NOTICE));
                return;
            }
            String name = username.substring(0, username.length() - 7);
            String keyCode = username.substring(username.length() - 6);
            String resp = HTTP.performGetRequest("http://api.dragonet.org/cls/query_token.php?" + String.format("username=%s&keycode=%s", name, keyCode));
            if (resp == null) {
                disconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)));
                proxy.getLogger().severe(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)).replace("§c", "").replace("§0", ""));
                return;
            }
            JsonElement json;
            try{
                JsonParser jsonParser = new JsonParser();
                json = jsonParser.parse(resp);
            }catch(Exception e){
                disconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
                proxy.getLogger().severe(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)).replace("§c", "").replace("§0", ""));
                //Json parse error! 
                return;
            }
            JsonObject obj = json.getAsJsonObject();
            if(!obj.get("status").getAsString().equals("success")){
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
            HTTP.performGetRequest("http://api.dragonet.org/cls/update_token.php?" + String.format("username=%s&oldtoken=%s&newtoken=%s", name, obj.get("token").getAsString(), authSvc.getAccessToken()));
            protocol = new MinecraftProtocol(authSvc.getSelectedProfile(), authSvc.getAccessToken());

            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().getDefault_server() + "]... ");
            connectToServer(proxy.getConfig().getRemote_servers().get(proxy.getConfig().getDefault_server()));
        } else {
            protocol = new MinecraftProtocol(username);

            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().getDefault_server() + "]... ");
            connectToServer(proxy.getConfig().getRemote_servers().get(proxy.getConfig().getDefault_server()));
        }
    }
    
    public void connectToServer(RemoteServer server){
        if(server == null) return;
        connecting = true;
        if(downstream != null && downstream.isConnected()){
            downstream.disconnect();
            // TODO: Send chat message about server change.

            // Remove all loaded entities
            /* BatchPacket batch = new BatchPacket();
            this.entityCache.getEntities().entrySet().forEach((ent) -> {
                if(ent.getKey() != 0){
                    batch.packets.add(new RemoveEntityPacket(ent.getKey()));
                }
            });
            this.entityCache.reset(true);
            sendPacket(batch, true); */
            return;
        }
        if(server.getClass().isAssignableFrom(DesktopServer.class)){
            downstream = new PCDownstreamSession(proxy, this);
            ((PCDownstreamSession)downstream).setProtocol(protocol);
            downstream.connect(server.getRemoteAddr(), server.getRemotePort());
        }else{
            // downstream = new PEDownstreamSession(proxy, this);
            // ((PEDownstreamSession)downstream).connect((PocketServer) server);
            disconnect("PE targets not supported yet");
        }
    }

    public void sendChat(String chat) {
        if (chat.contains("\n")) {
            String[] lines = chat.split("\n");
            for (String line : lines) {
                sendChat(line);
            }
            return;
        }
        Text text = new Text(); // raw
        Text.Raw raw = text.new Raw(chat, "");
        sendPacket(raw, true);
    }

    public void sendFakeBlock(int x, int y, int z, int id, int meta) {
        UpdateBlock pkBlock = new UpdateBlock();
        pkBlock.flagsAndMeta = UpdateBlock.NEIGHBORS << 4 | (meta & 0xF);
        pkBlock.position = new BlockPosition(x, y, z);
        pkBlock.block = (byte) (id & 0xFF);
        sendPacket(pkBlock, true);
    }

    public void authenticate(String password) {
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
    
    public void onConnected() {
        connecting = false;
    }
}
