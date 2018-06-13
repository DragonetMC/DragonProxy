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
package org.dragonet.api.sessions;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.UUID;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.network.PEPacket;

public interface IUpstreamSession<Packet> {

    public ProxyServer getProxy();

    public String getRaknetID();

//    public RakNetClientSession getRaknetClient();

    public boolean isLoggedIn();

    public boolean isSpawned();

    public InetSocketAddress getRemoteAddress();

//    public PEPacketProcessor getPacketProcessor();
//
//    public LoginChainDecoder getProfile();

    public String getUsername();

    public IDownstreamSession getDownstream();

    public Map<String, Object> getDataCache();

//    public Map<UUID, PlayerListEntry> getPlayerInfoCache();
//
//    public EntityCache getEntityCache();
//
//    public WindowCache getWindowCache();
//
//    public ChunkCache getChunkCache();
//
    public MinecraftProtocol getProtocol();
//
//    public JukeboxCache getJukeboxCache();

    public void sendPacket(PEPacket packet);
//
    // if sending a packer before spawn, you should set high_priority to true !
    public void sendPacket(PEPacket packet, boolean high_priority);

    public void sendAllPackets(PEPacket[] packets, boolean high_priority);

    public void connectToServer(String address, int port);

    public void onConnected();

    /**
     * Disconnected from server.
     *
     * @param reason
     */
    public void disconnect(String reason);

    /**
     * Called when this client disconnects.
     *
     * @param reason The reason of disconnection.
     */
    public void onDisconnect(String reason);

    public void authenticate(String email, String password, Proxy authProxy);

//    public void onLogin(LoginPacket packet);

    public void postLogin();

    public void setSpawned();

    public void sendChat(String chat);

    public void sendFakeBlock(int x, int y, int z, int id, int meta);

    public void sendCreativeInventory();

    public void handlePacketBinary(byte[] packet);

    public void putCachePacket(PEPacket packet);

    public void onTick();
}
