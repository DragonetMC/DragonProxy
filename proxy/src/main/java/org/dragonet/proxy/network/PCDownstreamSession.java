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

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import org.dragonet.protocol.PEPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;

/**
 * Maintaince the connection between the proxy and remote Minecraft server.
 */
public class PCDownstreamSession implements IDownstreamSession<Packet> {

    public MinecraftProtocol protocol;

    private final DragonProxy proxy;
    private final UpstreamSession upstream;
    private Client remoteClient;

    public PCDownstreamSession(DragonProxy proxy, UpstreamSession upstream) {
        this.proxy = proxy;
        this.upstream = upstream;
    }

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
            public void connected(ConnectedEvent event) {
                proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_REMOTE_CONNECTED, upstream.getUsername(), upstream.getRemoteAddress()));

                // Notify the server
//                BinaryStream bis = new BinaryStream();
//                bis.putString("Notification"); // command
//                ClientPluginMessagePacket pluginMessage = new ClientPluginMessagePacket("DragonProxy", bis.get());
//                send(pluginMessage);

                upstream.onConnected();
            }

            public void disconnected(DisconnectedEvent event) {
                System.out.println("DisconnectedEvent " + event.getReason());
                upstream.disconnect(proxy.getLang().get(event.getReason()));
            }
            public void disconnecting(DisconnectingEvent event) {
                System.out.println("DisconnectingEvent " + event.getReason());
                upstream.disconnect(proxy.getLang().get(event.getReason()));
            }

            public void packetReceived(PacketReceivedEvent event) {
                // Handle the packet
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
                        upstream.sendAllPackets(packets, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        remoteClient.getSession().connect();
    }

    public void disconnect() {
        if (remoteClient != null && remoteClient.getSession().isConnected()) {
            remoteClient.getSession().disconnect("Disconnect");
        }
    }

    public boolean isConnected() {
        return remoteClient != null && remoteClient.getSession().isConnected();
    }

    public void send(Packet... packets) {
        for (Packet p : packets) {
            send(p);
        }
    }

    public void send(Packet packet) {
        if (packet == null) {
            return;
        }
        remoteClient.getSession().send(packet);
    }

    public void sendChat(String chat) {
        remoteClient.getSession().send(new ClientChatPacket(chat));
    }

    public void onTick() {

    }

    public DragonProxy getProxy() {
        return proxy;
    }

    public UpstreamSession getUpstream() {
        return upstream;
    }
}
