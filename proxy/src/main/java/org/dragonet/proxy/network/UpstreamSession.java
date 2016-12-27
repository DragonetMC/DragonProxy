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

import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.network.protocol.StartGamePacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

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

import org.dragonet.proxy.DesktopServer;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.PocketServer;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.RemoteServer;
import org.dragonet.proxy.network.cache.EntityCache;
import org.dragonet.proxy.network.cache.WindowCache;
import org.dragonet.proxy.utilities.HTTP;
import org.dragonet.proxy.utilities.MCColor;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.raknet.protocol.EncapsulatedPacket;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.auth.service.AuthenticationService;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;

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
    private final InetSocketAddress remoteAddress;

    @Getter
    private final PEPacketProcessor packetProcessor;

    private final ScheduledFuture<?> packetProcessorScheule;

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

    public UpstreamSession(DragonProxy proxy, String raknetID, InetSocketAddress remoteAddress) {
        this.proxy = proxy;
        this.raknetID = raknetID;
        this.remoteAddress = remoteAddress;
        packetProcessor = new PEPacketProcessor(this);
        packetProcessorScheule = proxy.getGeneralThreadPool().scheduleAtFixedRate(packetProcessor, 10, 50, TimeUnit.MILLISECONDS);
    }

    public void sendPacket(DataPacket packet) {
        sendPacket(packet, false);
    }

    public void sendPacket(DataPacket packet, boolean immediate) {
        proxy.getNetwork().sendPacket(raknetID, packet, immediate);
    }

    public void sendAllPackets(DataPacket[] packets, boolean immediate) {
        if (packets.length < 5) {
            for (DataPacket packet : packets) {
                sendPacket(packet);
            }
        } else {
        	System.err.println("Batch Packet 1");
            BatchPacket batch = new BatchPacket();
            boolean mustImmediate = immediate;
            if (!mustImmediate) {
                for (DataPacket packet : packets) {
                	//sendPacket(packet, mustImmediate);
                    if (true) {
                        batch.putByteArray(packet.getBuffer());
                        mustImmediate = true;
                        break;
                    }
                }
            }
            sendPacket(batch, mustImmediate);
        }
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
            proxy.getNetwork().closeSession(raknetID, reason);
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

    public void handlePacketBinary(EncapsulatedPacket packet) {
        packetProcessor.putPacket(packet.buffer);
    }

    public void onLogin(LoginPacket packet) {
        if (username != null) {
            disconnect("Already logged in, this must be an error! ");
            return;
        }

        PlayStatusPacket status = new PlayStatusPacket();
        if (packet.protocol != Versioning.MINECRAFT_PE_PROTOCOL) {
            status.status = PlayStatusPacket.LOGIN_FAILED_CLIENT;
            sendPacket(status, true);
            disconnect(proxy.getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }
        status.status = PlayStatusPacket.LOGIN_SUCCESS;
        sendPacket(status, true);

        sendPacket(new ResourcePacksInfoPacket(), true);

        this.username = packet.username;
        proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, username, remoteAddress));
        if (proxy.getAuthMode().equals("online")) {
            StartGamePacket pkStartGame = new StartGamePacket();
            pkStartGame.entityRuntimeId = 0; //Use EID 0 for eaisier management
            pkStartGame.dimension = (byte) 0;
            pkStartGame.seed = 0;
            pkStartGame.generator = 1;
            pkStartGame.spawnX = 0;
            pkStartGame.spawnY = 0;
            pkStartGame.spawnZ = 0;
            pkStartGame.x = 0.0f;
            pkStartGame.y = 72.0f;
            pkStartGame.z = 0.0f;
            sendPacket(pkStartGame, true);

            SetSpawnPositionPacket pkSpawn = new SetSpawnPositionPacket();
            pkSpawn.x = 0;
            pkSpawn.y = 72;
            pkSpawn.z = 0;
            sendPacket(pkSpawn, true);

            PlayStatusPacket pkStat = new PlayStatusPacket();
            pkStat.status = PlayStatusPacket.PLAYER_SPAWN;
            sendPacket(pkStat, true);

            dataCache.put(CacheKey.AUTHENTICATION_STATE, "email");

            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_NOTICE, username));
            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_EMAIL));
        } else if (proxy.getAuthMode().equals("cls")) {
            //CLS LOGIN! 
            if ((username.length() < 6 + 1 + 1) || (!username.contains("_"))) {
                sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_CLS_NOTICE));
                return;
            }
            String name = username.substring(0, username.length() - 7);
            String keyCode = username.substring(username.length() - 6);
            String resp = HTTP.performGetRequest("http://api.dragonet.org/cls/query_token.php?" + String.format("username=%s&keycode=%s", name, keyCode));
            if (resp == null) {
                sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)));
                proxy.getLogger().severe(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)).replace("§c", "").replace("§0", ""));
                return;
            }
            JsonElement json = null;
            try{
                JsonParser jsonParser = new JsonParser();
                json = jsonParser.parse(resp);
            }catch(Exception e){
                sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
                proxy.getLogger().severe(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)).replace("§c", "").replace("§0", ""));
                //Json parse error! 
                return;
            }
            JsonObject obj = json.getAsJsonObject();
            if(!obj.get("status").getAsString().equals("success")){
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
            BatchPacket batch = new BatchPacket();
            this.entityCache.getEntities().entrySet().forEach((ent) -> {
                if(ent.getKey() != 0){
                	RemoveEntityPacket pkRemoveEntity = new RemoveEntityPacket();
                	pkRemoveEntity.eid = ent.getKey();
                	sendPacket(pkRemoveEntity, true);
                    //batch.packets.add(pkRemoveEntity);
                }
            });
            this.entityCache.reset(true);
            sendPacket(batch, true);
            return;
        }
        if(server.getClass().isAssignableFrom(DesktopServer.class)){
            downstream = new PCDownstreamSession(proxy, this);
            ((PCDownstreamSession)downstream).setProtocol(protocol);
            ((PCDownstreamSession)downstream).connect(server.getRemoteAddr(), server.getRemotePort());
        }else{
            downstream = new PEDownstreamSession(proxy, this);
            ((PEDownstreamSession)downstream).connect((PocketServer) server);
        }
    }

    public void sendStartGameAndDisconnect(String reason) {
        StartGamePacket pkStartGame = new StartGamePacket();
        pkStartGame.dimension = (byte) 1; //Login error so player in nether (Red screen)
        pkStartGame.generator = 1;
        pkStartGame.y = 72.0f;
        sendPacket(pkStartGame, true);

        PlayStatusPacket pkStat = new PlayStatusPacket();
        pkStat.status = PlayStatusPacket.PLAYER_SPAWN;
        sendPacket(pkStat, true);

        sendChat(reason);
        disconnect(reason);
    }

    public void sendChat(String chat) {
        if (chat.contains("\n")) {
            String[] lines = chat.split("\n");
            for (String line : lines) {
                sendChat(line);
            }
            return;
        }
        TextPacket pk = new TextPacket();
        pk.type = TextPacket.TYPE_CHAT;
        pk.source = "";
        pk.message = chat;
        sendPacket(pk, true);
    }

    public void sendPopup(String text) {
        TextPacket pk = new TextPacket();
        pk.type = TextPacket.TYPE_POPUP;
        pk.source = "";
        pk.message = text;
        sendPacket(pk, true);
    }

    public void sendFakeBlock(int x, int y, int z, int id, int meta) {
        UpdateBlockPacket pkBlock = new UpdateBlockPacket();
        pkBlock.flags = UpdateBlockPacket.FLAG_ALL;
        pkBlock.x = x;
        pkBlock.y = (byte) (y & 0xFF);
        pkBlock.z = z;
        pkBlock.blockId = (byte) (id & 0xFF);
        pkBlock.blockData = (byte) (meta & 0xFF);
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
