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
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.api.network.PEPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;

/**
 * Maintaince the connection between the proxy and remote Minecraft server.
 */
public class PCDownstreamSession implements IDownstreamSession<Packet> {

    public MinecraftProtocol protocol;

    private final ProxyServer proxy;
    private final IUpstreamSession upstream;
    private Client remoteClient;

    public PCDownstreamSession(ProxyServer proxy, IUpstreamSession upstream) {
        this.proxy = proxy;
        this.upstream = upstream;
    }

    @Override
    public void connect(String addr, int port) {
        if (this.protocol == null) {
            upstream.onConnected(); // Clear the flags
            upstream.disconnect("ERROR! ");
            return;
        }
        remoteClient = new Client(addr, port, protocol, new TcpSessionFactory());
        remoteClient.getSession().setConnectTimeout(5);
        remoteClient.getSession().setReadTimeout(5);
        remoteClient.getSession().setWriteTimeout(5);
        remoteClient.getSession().addListener(new SessionAdapter() {

            @Override
            public void connected(ConnectedEvent event) {
                proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_REMOTE_CONNECTED, upstream.getUsername(), upstream.getRemoteAddress()));
                upstream.onConnected();
            }

            @Override
            public void packetSending(PacketSendingEvent event) {
                onPacketSending(event);
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                System.out.println("DisconnectedEvent " + event.getCause() + " " + event.getReason());
                upstream.disconnect(proxy.getLang().get(event.getReason()));
            }

            @Override
            public void disconnecting(DisconnectingEvent event) {
                System.out.println("DisconnectingEvent " + event.getCause() + " " + event.getReason());
                upstream.disconnect(proxy.getLang().get(event.getReason()));
            }

            @Override
            public void packetReceived(PacketReceivedEvent event) {
                // Handle the packet
                onPacketRecieved(event);
            }
        });
        remoteClient.getSession().connect();
    }

    @Override
    public void disconnect() {
        if (remoteClient != null && remoteClient.getSession().isConnected())
            remoteClient.getSession().disconnect("Disconnect");
    }

    @Override
    public boolean isConnected() {
        return remoteClient != null && remoteClient.getSession().isConnected();
    }

    @Override
    public void send(Packet... packets) {
        for (Packet p : packets)
            send(p);
    }

    @Override
    public void send(Packet packet) {
        if (packet == null)
            return;
        remoteClient.getSession().send(packet);
    }

    @Override
    public void sendChat(String chat) {
        remoteClient.getSession().send(new ClientChatPacket(chat));
    }

    @Override
    public void onPacketSending(PacketSendingEvent event) {
        if (proxy.getAuthMode().equalsIgnoreCase("hybrid"))
            if (protocol.getSubProtocol() == SubProtocol.HANDSHAKE && event.getPacket() instanceof HandshakePacket) {
                HandshakePacket packet = event.getPacket();
                String host = remoteClient.getSession().getHost() + "\0" + upstream.getProfile().getChainJWT();
                packet = new HandshakePacket(packet.getProtocolVersion(), host, packet.getPort(), packet.getIntent());
                event.setPacket(packet);
            }
    }

    @Override
    public void onPacketRecieved(PacketReceivedEvent event) {
        try {
            PEPacket[] packets = PacketTranslatorRegister.translateToPE(upstream, event.getPacket());
            if (packets == null)
                return;
            if (packets.length <= 0)
                return;
            if (packets.length == 1)
                upstream.sendPacket(packets[0]);
            else
                upstream.sendAllPackets(packets, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onTick() {

    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public IUpstreamSession getUpstream() {
        return upstream;
    }
}
