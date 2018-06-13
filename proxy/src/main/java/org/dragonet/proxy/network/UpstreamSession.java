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

import org.dragonet.api.sessions.IDownstreamSession;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.whirvis.jraknet.protocol.Reliability;
import com.whirvis.jraknet.session.RakNetClientSession;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.events.defaults.packets.PackettoPlayerEvent;
import org.dragonet.proxy.events.defaults.player.PlayerAuthenticationEvent;
import org.dragonet.proxy.events.defaults.player.PlayerKickEvent;
import org.dragonet.proxy.events.defaults.player.PlayerLoginEvent;
import org.dragonet.proxy.events.defaults.player.PlayerQuitEvent;
import org.dragonet.proxy.utilities.CLSAuthenticationService;

import org.dragonet.common.data.entity.EntityType;
import org.dragonet.proxy.network.cache.EntityCache;
import org.dragonet.proxy.network.cache.JukeboxCache;
import org.dragonet.proxy.network.cache.WindowCache;
import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.protocol.packets.DisconnectPacket;
import org.dragonet.protocol.packets.FullChunkDataPacket;
import org.dragonet.protocol.packets.LoginPacket;
import org.dragonet.protocol.packets.PlayStatusPacket;
import org.dragonet.protocol.packets.ResourcePackStackPacket;
import org.dragonet.protocol.packets.ResourcePacksInfoPacket;
import org.dragonet.protocol.packets.SetSpawnPositionPacket;
import org.dragonet.protocol.packets.StartGamePacket;
import org.dragonet.protocol.packets.TextPacket;
import org.dragonet.protocol.packets.UpdateBlockPacket;
import org.dragonet.common.data.chunk.ChunkData;
import org.dragonet.common.data.chunk.Section;
import org.dragonet.common.utilities.Binary;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.common.utilities.LoginChainDecoder;
import org.dragonet.common.utilities.Zlib;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.Deflater;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.caches.IChunkCache;
import org.dragonet.api.caches.IEntityCache;
import org.dragonet.api.caches.IJukeboxCache;
import org.dragonet.api.caches.IWindowCache;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.common.data.blocks.BlockEnum;
import org.dragonet.common.data.inventory.ContainerId;
import org.dragonet.common.data.inventory.Slot;
import org.dragonet.protocol.packets.BatchPacket;
import org.dragonet.protocol.packets.InventoryContentPacket;
import org.dragonet.proxy.network.cache.ChunkCache;

/**
 * Maintaince the connection between the proxy and Minecraft: Pocket Edition
 * clients.
 */
public class UpstreamSession implements IUpstreamSession {

    private final ProxyServer proxy;
    private final String raknetID;
    private final RakNetClientSession raknetClient;
    private boolean loggedIn = false;
    private boolean spawned = false;
    private boolean connecting = false;
    private Queue<PEPacket> cachedPackets = new ConcurrentLinkedQueue();
    private final InetSocketAddress remoteAddress;
    private final PEPacketProcessor packetProcessor;
    private LoginChainDecoder profile;
    private String username;
    private IDownstreamSession downstream;
    private MinecraftProtocol protocol;

    /*
     * =============================================================================
     * =================== | Caches for Protocol Compatibility | ==================
     * =============================================================================
     */
    private final Map<String, Object> dataCache = Collections.synchronizedMap(new HashMap<String, Object>());
    private final Map<UUID, PlayerListEntry> playerInfoCache = Collections.synchronizedMap(new HashMap<UUID, PlayerListEntry>());
    private final IEntityCache entityCache = new EntityCache(this);
    private final IWindowCache windowCache = new WindowCache(this);
    private final IChunkCache chunkCache = new ChunkCache(this);
    private final IJukeboxCache jukeboxCache = new JukeboxCache();

    public UpstreamSession(ProxyServer proxy, String raknetID, RakNetClientSession raknetClient,
            InetSocketAddress remoteAddress) {
        this.proxy = proxy;
        this.raknetID = raknetID;
        this.remoteAddress = remoteAddress;
        this.raknetClient = raknetClient;
        this.packetProcessor = new PEPacketProcessor(this);
    }

    @Override
    public ProxyServer getProxy() {
        return proxy;
    }

    @Override
    public String getRaknetID() {
        return raknetID;
    }

    public RakNetClientSession getRaknetClient() {
        return raknetClient;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public boolean isSpawned() {
        return spawned;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public PEPacketProcessor getPacketProcessor() {
        return packetProcessor;
    }

    public LoginChainDecoder getProfile() {
        return profile;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public IDownstreamSession getDownstream() {
        return downstream;
    }

    @Override
    public Map<String, Object> getDataCache() {
        return dataCache;
    }

    @Override
    public Map<UUID, PlayerListEntry> getPlayerInfoCache() {
        return playerInfoCache;
    }

    @Override
    public IEntityCache getEntityCache() {
        return entityCache;
    }

    @Override
    public IWindowCache getWindowCache() {
        return windowCache;
    }

    @Override
    public IChunkCache getChunkCache() {
        return chunkCache;
    }

    @Override
    public MinecraftProtocol getProtocol() {
        return protocol;
    }

    @Override
    public IJukeboxCache getJukeboxCache() {
    	return jukeboxCache;
    }

    @Override
    public void sendPacket(PEPacket packet) {
        sendPacket(packet, false);
    }

    //if sending a packer before spawn, you should set high_priority to true !
    @Override
    public void sendPacket(PEPacket packet, boolean high_priority) {
        if (packet == null)
            return;

        if(!proxy.getConfig().getDisable_packet_events()){
            PackettoPlayerEvent packetEvent = new PackettoPlayerEvent(this, packet);
            proxy.getEventManager().callEvent(packetEvent);
            packet = packetEvent.getPacket();
        }

        //cache in case of not spawned and no high priority
        if (!spawned && !high_priority) {
            putCachePacket(packet);
            return;
        }

        while (!cachedPackets.isEmpty())
            sendPacket(cachedPackets.poll(), true); //TODO sendAllPackets

        try (Timing timing = Timings.getSendDataPacketTiming(packet)) {

            packet.encode();

            byte[] buffer;
            try {
                buffer = Zlib.deflate(Binary.appendBytes(Binary.writeUnsignedVarInt(packet.getBuffer().length), packet.getBuffer()), Deflater.BEST_COMPRESSION);
            } catch (Exception e) {
                timing.stopTiming();
                e.printStackTrace();
                return;
            }
            raknetClient.sendMessage(Reliability.RELIABLE_ORDERED, 0, new com.whirvis.jraknet.Packet(Binary.appendBytes((byte) 0xfe, buffer)));
        }
    }

    @Override
    public void sendAllPackets(PEPacket[] packets, boolean high_priority) {
        if (packets.length < 5 || true) //<- this disable batched packets
            for (PEPacket packet : packets)
                sendPacket(packet, high_priority);
        else {
            BatchPacket batchPacket = new BatchPacket();
//            System.out.println("BatchPacket :");
            for (PEPacket packet : packets)
//                System.out.println(" - " + packet.getClass().getSimpleName());
                if (high_priority) {
                    batchPacket.packets.add(packet);
                    break;
                }
            sendPacket(batchPacket, high_priority);
        }
    }

    @Override
    public void connectToServer(String address, int port) {
        if (address == null)
            return;
        connecting = true;
        if (downstream != null && downstream.isConnected()) {
            spawned = false;
            downstream.disconnect();
            return;
        }
        downstream = new PCDownstreamSession(proxy, this);
        ((PCDownstreamSession) downstream).protocol = protocol;
        downstream.connect(address, port);
    }

    @Override
    public void onConnected() {
        connecting = false;
    }

    /**
     * Disconnected from server.
     *
     * @param reason
     */
    @Override
    public void disconnect(String reason) {
        PlayerKickEvent kickEvent = new PlayerKickEvent(this);
        proxy.getEventManager().callEvent(kickEvent);
        if (!connecting) {
            if(kickEvent.isCancelled​())return; //not cancellable for pre pre login phase
            sendPacket(new DisconnectPacket(false, reason), true);
            raknetClient.update(); //Force the DisconnectPacket to be sent before we close the connection
        }
    }

    /**
     * Called when this client disconnects.
     *
     * @param reason The reason of disconnection.
     */
    @Override
    public void onDisconnect(String reason) {
        PlayerQuitEvent playerQuit = new PlayerQuitEvent(this);
        proxy.getEventManager().callEvent(playerQuit);

        proxy.getLogger().info(proxy.getLang().get(Lang.CLIENT_DISCONNECTED,
                proxy.getAuthMode().equals("cls") ? "unknown" : username, remoteAddress, reason));
        if (downstream != null)
            downstream.disconnect();
        proxy.getSessionRegister().removeSession(this);
        getChunkCache().purge();
    }

    @Override
    public void authenticate(String email, String password, Proxy authProxy) {
        proxy.getGeneralThreadPool().execute(() -> {
            try {
                if (authProxy == null)
                    protocol = new MinecraftProtocol(email, password, Proxy.NO_PROXY);
                else
                    protocol = new MinecraftProtocol(email, password, authProxy);
            } catch (RequestException ex) {
                ex.printStackTrace();
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
            connectToServer(proxy.getConfig().getRemote_server_addr(), proxy.getConfig().getRemote_server_port());
        });
    }

    @Override
    public void onLogin(PEPacket loginPacket) {
        LoginPacket packet = null;
        if (loginPacket instanceof LoginPacket)
            packet = (LoginPacket)loginPacket;
        else {
            disconnect(proxy.getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }

        if (username != null) {
            disconnect("Already logged in, this must be an error! ");
            return;
        }

        getDataCache().put(CacheKey.PACKET_LOGIN_PACKET, packet);

        PlayStatusPacket status = new PlayStatusPacket();
        proxy.getLogger().info("Player " + packet.decoded.username + " login with protocol " + packet.protocol  + "(actual " + ProtocolInfo.CURRENT_PROTOCOL + ")");
        if (packet.protocol < ProtocolInfo.CURRENT_PROTOCOL) {
            proxy.getLogger().severe("Player " + packet.decoded.username + " login with protocol " + "Unsupported protocol " + packet.protocol  + " < " + ProtocolInfo.CURRENT_PROTOCOL);
            status.status = PlayStatusPacket.LOGIN_FAILED_CLIENT;
            sendPacket(status, true);
            disconnect(proxy.getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }

        // Get the profile and read out the username!
        profile = packet.decoded;

        // Verify the integrity of the LoginPacket
        if (proxy.getConfig().isAuthenticate_players() && !packet.decoded.isLoginVerified()) {
            status.status = PlayStatusPacket.LOGIN_FAILED_INVALID_TENANT;
            sendPacket(status, true);
            disconnect(proxy.getLang().get(Lang.LOGIN_VERIFY_FAILED));
            return;
        }

        status.status = PlayStatusPacket.LOGIN_SUCCESS;
        sendPacket(status, true);

        this.username = profile.username;

        // Okay @dktapps ;)
        sendPacket(new ResourcePacksInfoPacket(), true);

        PlayerLoginEvent loginEvent = new PlayerLoginEvent(this);
        proxy.getEventManager().callEvent(loginEvent);

        // now wait for response
    }

    @Override
    public void postLogin() {
        sendPacket(new ResourcePackStackPacket(), true);

        loggedIn = true;
        proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, username, remoteAddress));
        PlayerAuthenticationEvent authEvent = new PlayerAuthenticationEvent(this);
        proxy.getEventManager().callEvent(authEvent);
        if(authEvent.isCancelled​())return;

        if (proxy.getAuthMode().equals("online")) {
            proxy.getLogger().debug("Login online mode, sending placeholder datas");
            StartGamePacket pkStartGame = new StartGamePacket();
            pkStartGame.eid = getEntityCache().getClientEntity().getProxyEid(); // well we use 1 now
            pkStartGame.rtid = getEntityCache().getClientEntity().getProxyEid();
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
            sendPacket(new FullChunkDataPacket(0, 0, data.getBuffer()), true);
            sendPacket(new FullChunkDataPacket(0, -1, data.getBuffer()), true);
            sendPacket(new FullChunkDataPacket(-1, 0, data.getBuffer()), true);
            sendPacket(new FullChunkDataPacket(-1, -1, data.getBuffer()), true);

            dataCache.put(CacheKey.AUTHENTICATION_STATE, "online_login_wait");

            PlayStatusPacket pkStat = new PlayStatusPacket();
            pkStat.status = PlayStatusPacket.PLAYER_SPAWN;
            sendPacket(pkStat, true);

            sendChat(proxy.getLang().get(Lang.MESSAGE_LOGIN_PROMPT));
        } else if (proxy.getAuthMode().equals("cls")) {
            // CLS LOGIN!
            if (!CLSAuthenticationService.getInstance().authenticate(this)) {
                if (getDataCache().containsKey("cls_link_server") && getDataCache().containsKey("cls_link_pin")) {
                    disconnect("You must link your Mojang account, please visit :\n"
                            + (String) getDataCache().get("cls_link_server") + "\n"
                            + "Your pin code is: " + (String) getDataCache().get("cls_link_pin"));
                    return;
                }
                disconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)));
                proxy.getLogger().severe(proxy.getLang()
                        .get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE))
                        .replace("§c", "").replace("§0", ""));
                return;
            }
            AuthenticationService authSvc = new AuthenticationService((String) dataCache.get("mojang_clientToken"));
            authSvc.setUsername((String) dataCache.get("mojang_displayName"));
            authSvc.setAccessToken((String) dataCache.get("mojang_accessToken"));
            try {
                authSvc.login();
                getDataCache().put("mojang_accessToken", authSvc.getAccessToken());
            } catch (RequestException ex) {
                ex.printStackTrace();
                disconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
                return;
            }
            username = authSvc.getSelectedProfile().getName();

            CLSAuthenticationService.getInstance().refresh(this, authSvc.getAccessToken());

            protocol = new MinecraftProtocol(authSvc.getSelectedProfile(), authSvc.getClientToken(), authSvc.getAccessToken());

            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().getRemote_server_addr() + "]... ");
            connectToServer(proxy.getConfig().getRemote_server_addr(), proxy.getConfig().getRemote_server_port());
        } else {
            protocol = new MinecraftProtocol(username);
            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().getRemote_server_addr() + "]... ");
            connectToServer(proxy.getConfig().getRemote_server_addr(), proxy.getConfig().getRemote_server_port());
        }
    }

    @Override
    public void setSpawned() {
        if (!spawned) {
            spawned = true;
            PlayStatusPacket play = new PlayStatusPacket(PlayStatusPacket.PLAYER_SPAWN);
            sendPacket(play, true);
        }
    }

    @Override
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

    @Override
    public void sendFakeBlock(int x, int y, int z, int id, int meta) {
        UpdateBlockPacket pkBlock = new UpdateBlockPacket();
        pkBlock.id = id;
        pkBlock.data = meta;
        pkBlock.flags = UpdateBlockPacket.FLAG_NEIGHBORS;
        pkBlock.blockPosition = new BlockPosition(x, y, z);
        sendPacket(pkBlock);
    }

    @Override
    public void sendCreativeInventory() {
        // main inventory
//        ContainerId.CREATIVE.getId();
//        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
//        inventoryContentPacket.windowId = ContainerId.INVENTORY.getId();
//        Slot[] inventory = new Slot[36];
//        for(int index = 0; index < inventory.length; index++) {
//            inventory[index] = new Slot(0, 0, 0);
//        }
//        inventoryContentPacket.items = inventory;
//        sendPacket(inventoryContentPacket);
//
//        // ? inventory
//        InventoryContentPacket inventoryContentPacket2 = new InventoryContentPacket();
//        inventoryContentPacket2.windowId = ContainerId.ARMOR.getId();
//        Slot[] inventory2 = new Slot[4];
//        for(int index = 0; index < inventory2.length; index++) {
//            inventory2[index] = new Slot(0, 0, 0);
//        }
//        inventoryContentPacket2.items = inventory2;
//        sendPacket(inventoryContentPacket2);

        // creative inventory
        InventoryContentPacket inventoryContentPacket3 = new InventoryContentPacket();
        inventoryContentPacket3.windowId = ContainerId.CREATIVE.getId();
        inventoryContentPacket3.items = BlockEnum.getCreativeBlocks().stream().toArray(Slot[]::new);
        sendPacket(inventoryContentPacket3);
    }

    @Override
    public void handlePacketBinary(byte[] packet) {
        packetProcessor.putPacket(packet);
    }

    @Override
    public void putCachePacket(PEPacket packet) {
        if (packet == null)
            return;
        if (spawned) {
            //            System.out.println("Not caching since already spawned! ");
            sendPacket(packet);
            return;
        }
        cachedPackets.offer(packet);
    }

    @Override
    public void onTick() {
        entityCache.onTick();
        chunkCache.onTick();
        if (packetProcessor != null)
            packetProcessor.onTick();
        if (downstream != null)
            downstream.onTick();
    }
}
