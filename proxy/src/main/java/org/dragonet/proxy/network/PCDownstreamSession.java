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

import lombok.Getter;
import lombok.Setter;
import org.dragonet.proxy.protocol.packet.PEPacket;
import org.dragonet.proxy.DesktopServer;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

/**
 * Maintaince the connection between the proxy and remote Minecraft server.
 */
public class PCDownstreamSession implements DownstreamSession<Packet> {

    @Getter
    private final DragonProxy proxy;

    @Getter
    private final UpstreamSession upstream;

    @Getter
    private DesktopServer serverInfo;
    
    private Client remoteClient;
    
    @Getter
    @Setter
    private MinecraftProtocol protocol;

    public PCDownstreamSession(DragonProxy proxy, UpstreamSession upstream) {
        this.proxy = proxy;
        this.upstream = upstream;
    }

    @Override
    public boolean isConnected() {
        return remoteClient != null && remoteClient.getSession().isConnected();
    }

    @Override
    public void send(Packet... packets) {
        for (Packet p : packets) {
            send(p);
        }
    }

    @Override
    public void send(Packet packet) {
        remoteClient.getSession().send(packet);
    }

    public void connect(DesktopServer serverInfo){
        this.serverInfo = serverInfo;
        connect(serverInfo.getRemoteAddr(), serverInfo.getRemotePort());
    }
    
    @Override
    public void connect(String addr, int port) {
        if (this.protocol == null) {
            upstream.onConnected(); // Clear the flags
            upstream.disconnect("ERROR! ");
            return;
        }
        remoteClient = new Client(addr, port, protocol, new TcpSessionFactory());
        remoteClient.getSession().addListener(new SessionAdapter() {
            @Override
            public void connected(ConnectedEvent event) {
                proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_REMOTE_CONNECTED, upstream.getUsername(), upstream.getRemoteAddress()));
                upstream.onConnected();
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                upstream.disconnect(proxy.getLang().get(event.getReason()));
            }

            @Override
            public void packetReceived(PacketReceivedEvent event) {
                /*
                if (!event.getPacket().getClass().getSimpleName().toLowerCase().contains("block")
                        && !event.getPacket().getClass().getSimpleName().toLowerCase().contains("entity")
                        && !event.getPacket().getClass().getSimpleName().toLowerCase().contains("time")) {
                    System.out.println(event.getPacket().getClass().getSimpleName() + " > " + event.getPacket().toString());
                }
                */
                //Handle the packet
                try {
                    PEPacket[] packets = PacketTranslatorRegister.translateToPE(upstream, event.getPacket());
                    if (packets == null) {
                        return;
                    }
                    if (packets.length <= 0) {
                        return;
                    }
                    if (packets.length == 1) {
                        upstream.sendPacket(packets[0]);
                    } else {
                        upstream.sendAllPackets(packets, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        remoteClient.getSession().connect();
    }

    @Override
    public void onTick() {
    }

    @Override
    public void disconnect() {
        if (remoteClient != null && remoteClient.getSession().isConnected()) {
            remoteClient.getSession().disconnect("Disconnect");
        }
    }
    
    @Override
    public void sendChat(String chat) {
        remoteClient.getSession().send(new ClientChatPacket(chat));
    }

}
