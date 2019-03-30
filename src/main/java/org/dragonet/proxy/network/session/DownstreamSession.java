/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import com.nukkitx.network.SessionManager;
import com.nukkitx.network.raknet.session.RakNetSession;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.util.RemoteServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;

public class DownstreamSession extends ProxySession {

    private BedrockSession<UpstreamSession> upstream;
    private Client remoteClient;
    private Logger logger;
    private DragonProxy proxy;

    public DownstreamSession(BedrockSession<UpstreamSession> upstream, DragonProxy proxy) {
        this.upstream = upstream;
        this.proxy = proxy;
        logger = LoggerFactory.getLogger(DownstreamSession.class);
    }

    @Override
    public void onDisconnect(@Nonnull DisconnectReason disconnectReason) {

    }

    @Override
    public void onDisconnect(@Nonnull String s) {

    }

    @Override
    public void close() {
        if(!isClosed()) {
            remoteClient.getSession().disconnect("Disconnect");
        }
    }

    @Override
    public boolean isClosed() {
        return remoteClient != null && !remoteClient.getSession().isConnected();
    }


    @Override
    public void setRemoteServer(RemoteServer server) {
        System.out.println("set remote server inside");
        MinecraftProtocol protocol = new MinecraftProtocol("lukeeey21");
        remoteClient = new Client(server.getAddress(), server.getPort(), protocol, new TcpSessionFactory());
        remoteClient.getSession().addListener(new SessionAdapter() {

            @Override
            public void connected(ConnectedEvent event) {
                System.out.println("Player connected to remote " + server.getAddress());
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                System.out.println("Player  disconnected from remote. Reason: " + event.getReason());
                upstream.disconnect(event.getReason());
            }

            @Override
            public void packetReceived(PacketReceivedEvent event) {
                System.out.println("Packet received from remote: " + event.getPacket().getClass().getSimpleName());
            }
        });
        remoteClient.getSession().connect();
}

    @Override
    public RemoteServer getRemoteServer() {
        return null;
    }

}
