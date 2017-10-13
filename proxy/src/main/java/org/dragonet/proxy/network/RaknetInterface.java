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

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.marfgamer.jraknet.RakNetPacket;
import net.marfgamer.jraknet.identifier.Identifier;
import net.marfgamer.jraknet.identifier.MinecraftIdentifier;
import net.marfgamer.jraknet.protocol.ConnectionType;
import net.marfgamer.jraknet.server.RakNetServer;
import net.marfgamer.jraknet.server.RakNetServerListener;
import net.marfgamer.jraknet.server.ServerPing;
import net.marfgamer.jraknet.session.RakNetClientSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.utilities.Versioning;

public class RaknetInterface implements RakNetServerListener {

    public final static Set<String> IMMEDIATE_PACKETS = new HashSet<>();

    static {
        IMMEDIATE_PACKETS.add("PlayStatus");
    }

    @Getter
    private final DragonProxy proxy;

    private final SessionRegister sessions;

    @Getter
    private final RakNetServer rakServer;

    public RaknetInterface(DragonProxy proxy, String ip, int port) {
        this.proxy = proxy;
        rakServer = new RakNetServer(port, Integer.MAX_VALUE);
        rakServer.addListener(this);
        rakServer.addSelfListener();
        sessions = this.proxy.getSessionRegister();
        rakServer.startThreaded();
    }

    public void setBroadcastName(String serverName, int players, int maxPlayers) {
        rakServer.setIdentifier(new MinecraftIdentifier(serverName, Versioning.MINECRAFT_PE_PROTOCOL, Versioning.MINECRAFT_PE_VERSION, players, maxPlayers, new Random().nextLong(), "DragonProxy", "Survival"));
        if(!rakServer.isBroadcastingEnabled()) {
            rakServer.setBroadcastingEnabled(true);
        }
    }

    /* public void onTick() {
    } */

    @Override
    public void handlePing(ServerPing ping) {
        System.out.println("PING " + ping.getSender().toString());
    }

    @Override
    public void onClientConnect(RakNetClientSession session) {
        System.out.println("CLIENT CONNECT");
        String identifier = session.getAddress().toString();
        UpstreamSession upstream = new UpstreamSession(proxy, identifier, session, session.getAddress());
        sessions.newSession(upstream);
    }

    @Override
    public void onClientDisconnect(RakNetClientSession session, String reason) {
        System.out.println("CLIENT DISCONNECT");
        UpstreamSession upstream = sessions.getSession(session.getAddress().toString());
        if (upstream == null) {
            return;
        }
        upstream.onDisconnect(proxy.getLang().get(Lang.MESSAGE_CLIENT_DISCONNECT)); //It will handle rest of the things.
    }

    @Override
    public void handleMessage(RakNetClientSession session, RakNetPacket packet, int channel) {
        UpstreamSession upstream = sessions.getSession(session.getAddress().toString());
        if (upstream == null) {
            return;
        }
        System.out.println("Received RakNet packet: " + packet.getClass().getSimpleName());
        upstream.handlePacketBinary(packet.buffer().array());
    }

    @Override
    public void onThreadException(Throwable throwable) {
        System.out.println("Thread exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void onHandlerException(InetSocketAddress address, Throwable throwable) {
        System.out.println("Handler exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void onSessionException(RakNetClientSession session, Throwable throwable) {
        System.out.println("Session exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void shutdown() {
        rakServer.shutdown();
    }
}
