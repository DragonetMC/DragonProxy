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

import java.util.*;

import lombok.Getter;
import net.marfgamer.jraknet.RakNetPacket;
import net.marfgamer.jraknet.identifier.MCPEIdentifier;
import net.marfgamer.jraknet.protocol.message.EncapsulatedPacket;
import net.marfgamer.jraknet.server.RakNetServer;
import net.marfgamer.jraknet.server.RakNetServerListener;
import net.marfgamer.jraknet.session.RakNetClientSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.utilities.Binary;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.proxy.utilities.Zlib;
import sul.utils.Packet;

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
        rakServer.setListener(this);
        sessions = this.proxy.getSessionRegister();
        rakServer.startThreaded();
    }

    public void setBroadcastName(String serverName, int players, int maxPlayers) {
        rakServer.setIdentifier(new MCPEIdentifier(serverName, Versioning.MINECRAFT_PE_PROTOCOL, Versioning.MINECRAFT_PE_VERSION, players, maxPlayers, 0L, "DragonProxy", "DragonProxy"));
    }

    /* public void onTick() {
    } */

    @Override
    public void onClientConnect(RakNetClientSession session) {
        String identifier = session.getAddress().toString();
        UpstreamSession upstream = new UpstreamSession(proxy, identifier, session, session.getAddress());
        sessions.newSession(upstream);
    }

    @Override
    public void onClientDisconnect(RakNetClientSession session, String reason) {
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

    public void shutdown() {
        rakServer.shutdown();
    }
}
