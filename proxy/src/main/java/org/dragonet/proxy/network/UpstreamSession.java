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

import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import net.marfgamer.jraknet.protocol.Reliability;
import net.marfgamer.jraknet.session.RakNetClientSession;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.utilities.CLSAuthenticationService;

import org.dragonet.common.data.entity.EntityType;
import org.dragonet.proxy.network.cache.EntityCache;
import org.dragonet.proxy.network.cache.WindowCache;
import org.dragonet.protocol.PEPacket;
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
import org.dragonet.protocol.type.chunk.ChunkData;
import org.dragonet.protocol.type.chunk.Section;
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
import org.dragonet.protocol.packets.BatchPacket;
import org.dragonet.proxy.network.cache.ChunkCache;

/**
 * Maintaince the connection between the proxy and Minecraft: Pocket Edition
 * clients.
 */
public class UpstreamSession {

    private final DragonProxy proxy;
    private final String raknetID;
    private final RakNetClientSession raknetClient;
    private boolean loggedIn = false;
    private boolean spawned = false;
    private Queue<PEPacket> cachedPackets = new ConcurrentLinkedQueue();
    private final InetSocketAddress remoteAddress;
    private final PEPacketProcessor packetProcessor;
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
    private final Map<UUID, PlayerListEntry> playerInfoCache = Collections.synchronizedMap(new HashMap<UUID, PlayerListEntry>());
    private final EntityCache entityCache = new EntityCache(this);
    private final WindowCache windowCache = new WindowCache(this);
    private final ChunkCache chunkCache = new ChunkCache(this);
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
        this.packetProcessor = new PEPacketProcessor(this);
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

    public ChunkCache getChunkCache() {
        return chunkCache;
    }

    public MinecraftProtocol getProtocol() {
        return protocol;
    }

    public void sendPacket(PEPacket packet) {
        sendPacket(packet, false);
    }

    //if sending a packer before spawn, you should set high_priority to true !
    public void sendPacket(PEPacket packet, boolean high_priority) {
        if (packet == null)
            return;

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
            raknetClient.sendMessage(Reliability.RELIABLE_ORDERED, 0, new net.marfgamer.jraknet.Packet(Binary.appendBytes((byte) 0xfe, buffer)));
        }
    }

    public void sendAllPackets(PEPacket[] packets, boolean high_priority) {
        if (packets.length < 5 || true) //<- this disable batched packets
            for (PEPacket packet : packets)
                sendPacket(packet, high_priority);
        else {
            BatchPacket batchPacket = new BatchPacket();
//            System.out.println("BatchPacket :");
            for (PEPacket packet : packets) {
//                System.out.println(" - " + packet.getClass().getSimpleName());
                if (high_priority) {
                    batchPacket.packets.add(packet);
                    break;
                }
            }
            sendPacket(batchPacket, high_priority);
        }
    }

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
            sendPacket(new DisconnectPacket(false, reason), true);
            raknetClient.update(); //Force the DisconnectPacket to be sent before we close the connection
        }
        //Forceing the connection to close
        proxy.getNetwork().getRakServer().removeSession(getRaknetClient(), reason);
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
    }

    public void authenticate(String email, String password, Proxy authProxy) {
        proxy.getGeneralThreadPool().execute(() -> {
            try {
                if (authProxy == null)
                    protocol = new MinecraftProtocol(email, password, false, Proxy.NO_PROXY);
                else
                    protocol = new MinecraftProtocol(email, password, false, authProxy);
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
            connectToServer(proxy.getConfig().remote_server_addr, proxy.getConfig().remote_server_port);
        });
    }

    public void onLogin(LoginPacket packet) {
        if (username != null) {
            disconnect("Already logged in, this must be an error! ");
            return;
        }

        getDataCache().put(CacheKey.PACKET_LOGIN_PACKET, packet);

        PlayStatusPacket status = new PlayStatusPacket();
        proxy.getLogger().debug("CLIENT PROTOCOL = " + packet.protocol);
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
        sendPacket(new ResourcePacksInfoPacket(), true);

        // now wait for response
    }

    public void postLogin() {
        sendPacket(new ResourcePackStackPacket(), true);

        loggedIn = true;
        proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, username, remoteAddress));
        if (proxy.getAuthMode().equals("online")) {
            proxy.getLogger().debug("Login online mode, sending placeholder datas");
            StartGamePacket pkStartGame = new StartGamePacket();
            pkStartGame.eid = getEntityCache().getClientEntity().proxyEid; // well we use 1 now
            pkStartGame.rtid = getEntityCache().getClientEntity().proxyEid;
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

            protocol = new MinecraftProtocol(authSvc.getSelectedProfile(), authSvc.getAccessToken());

            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().remote_server_addr + "]... ");
            connectToServer(proxy.getConfig().remote_server_addr, proxy.getConfig().remote_server_port);
        } else {
            protocol = new MinecraftProtocol(username);
            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().remote_server_addr + "]... ");
            connectToServer(proxy.getConfig().remote_server_addr, proxy.getConfig().remote_server_port);
        }
    }

    public void setSpawned() {
        if (!spawned) {
            spawned = true;
            PlayStatusPacket play = new PlayStatusPacket(PlayStatusPacket.PLAYER_SPAWN);
            sendPacket(play, true);
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
        sendPacket(pkBlock);
    }

    public void handlePacketBinary(byte[] packet) {
        packetProcessor.putPacket(packet);
    }

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

    public void onTick() {
        entityCache.onTick();
        chunkCache.onTick();
        if (packetProcessor != null)
            packetProcessor.onTick();
        if (downstream != null)
            downstream.onTick();
    }
}
