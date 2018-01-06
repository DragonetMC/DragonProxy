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

import java.net.InetSocketAddress;
import java.util.*;

import net.marfgamer.jraknet.RakNetPacket;
import net.marfgamer.jraknet.identifier.MinecraftIdentifier;
import net.marfgamer.jraknet.server.RakNetServer;
import net.marfgamer.jraknet.server.RakNetServerListener;
import net.marfgamer.jraknet.server.ServerPing;
import net.marfgamer.jraknet.session.RakNetClientSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;

public class RaknetInterface implements RakNetServerListener {

    public static final Set<String> IMMEDIATE_PACKETS = new HashSet<>();

    private final DragonProxy proxy;
    private final SessionRegister sessions;
    private final RakNetServer rakServer;

    static {
        IMMEDIATE_PACKETS.add("PlayStatus");
    }

    public RaknetInterface(DragonProxy proxy, String ip, int port) {
        this.proxy = proxy;
        rakServer = new RakNetServer(port, Integer.MAX_VALUE);
        rakServer.addListener(this);
        rakServer.addSelfListener();
        sessions = this.proxy.getSessionRegister();
        rakServer.startThreaded();
    }

    public DragonProxy getProxy() {
        return proxy;
    }

    public RakNetServer getRakServer() {
        return rakServer;
    }

    /*
     * public void onTick() { }
     */
    public void handlePing(ServerPing ping) {
        DragonProxy.getInstance().getLogger().debug("PING " + ping.getSender().toString());
    }

    public void handleMessage(RakNetClientSession session, RakNetPacket packet, int channel) {
        UpstreamSession upstream = sessions.getSession(session.getAddress().toString());
        if (upstream == null)
            return;
        // System.out.println("Received RakNet packet: " +
        // packet.getClass().getSimpleName());
        upstream.handlePacketBinary(packet.array());
    }

    public void onClientConnect(RakNetClientSession session) {
        DragonProxy.getInstance().getLogger().debug("CLIENT CONNECT");
        String identifier = session.getAddress().toString();
        UpstreamSession upstream = new UpstreamSession(proxy, identifier, session, session.getAddress());
        sessions.newSession(upstream);
    }

    public void onClientDisconnect(RakNetClientSession session, String reason) {
        DragonProxy.getInstance().getLogger().debug("CLIENT DISCONNECT");
        UpstreamSession upstream = sessions.getSession(session.getAddress().toString());
        if (upstream == null)
            return;
        upstream.onDisconnect(proxy.getLang().get(Lang.MESSAGE_CLIENT_DISCONNECT)); // It will handle rest of the
        // things.
    }

    public void onThreadException(Throwable throwable) {
        DragonProxy.getInstance().getLogger().debug("Thread exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void onHandlerException(InetSocketAddress address, Throwable throwable) {
        DragonProxy.getInstance().getLogger().debug("Handler exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void onSessionException(RakNetClientSession session, Throwable throwable) {
        DragonProxy.getInstance().getLogger().debug("Session exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void setBroadcastName(String serverName, int players, int maxPlayers) {
        rakServer.setIdentifier(
            new MinecraftIdentifier(serverName, ProtocolInfo.CURRENT_PROTOCOL, ProtocolInfo.MINECRAFT_VERSION_NETWORK,
                players, maxPlayers, new Random().nextLong(), "DragonProxy", "Survival"));
        if (!rakServer.isBroadcastingEnabled())
            rakServer.setBroadcastingEnabled(true);
    }

    public void shutdown() {
        rakServer.shutdown();
    }
}
