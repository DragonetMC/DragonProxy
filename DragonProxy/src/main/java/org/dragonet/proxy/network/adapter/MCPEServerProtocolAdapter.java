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
package org.dragonet.proxy.network.adapter;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Arrays;
import lombok.Getter;
import net.marfgamer.jraknet.RakNetPacket;
import net.marfgamer.jraknet.client.RakNetClient;
import net.marfgamer.jraknet.client.RakNetClientListener;
import net.marfgamer.jraknet.client.Warning;
import net.marfgamer.jraknet.identifier.Identifier;
import net.marfgamer.jraknet.protocol.Reliability;
import net.marfgamer.jraknet.protocol.message.acknowledge.Record;
import net.marfgamer.jraknet.session.RakNetServerSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.PacketTranslatorRegister;
import org.dragonet.proxy.network.RakNetUtil;

/**
 * @author robotman3000
 */
public class MCPEServerProtocolAdapter implements ServerProtocolAdapter<RakNetPacket>, RakNetClientListener {

    /**
     * The PE Server
     */
    private RakNetClient client;

    @Getter
    /**
     * The PE Client
     */
    private ClientConnection upstream;
    private ArrayDeque<RakNetPacket> queuedPackets = new ArrayDeque<>();
    private final String sender = "[PE Serverside] ";
    private boolean passthrough = false;

    public MCPEServerProtocolAdapter() {
        client = new RakNetClient();
    }

    @Override
    public void connectToRemoteServer(String address, int port) {
        DragonProxy.getLogger().info(sender + "[" + upstream.getUsername() + "] Connecting to remote pocket server at [" + String.format("%s:%s", address, port) + "] ");
        try {
            client.setListener(this);
            client.connectThreaded(address, port);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void disconnectFromRemoteServer(String reason) {
        client.disconnect(reason);
    }

    @Override
    public void setClient(ClientConnection session) {
        this.upstream = session;
    }

    @Override
    public void sendPacket(RakNetPacket packet) {
        if (client.isConnected()) {
            DragonProxy.getLogger().debug(sender + "Sending Packet: " + upstream.getSessionID() + ": " + packet.getClass().getCanonicalName());
            client.sendMessage(Reliability.UNRELIABLE, packet);
        } else {
            DragonProxy.getLogger().debug(sender + "Queuing packet: " + upstream.getSessionID() + ": " + packet.getClass().getCanonicalName());
            queuedPackets.add(packet);
        }
    }

    @Override
    public void handlePacket(RakNetPacket raknetPk, ClientConnection session) {
        sul.utils.Packet packet = RakNetUtil.getPacket(raknetPk);
        DragonProxy.getLogger().debug(sender + "Handling Packet: " + session.getSessionID() + ": " + packet.getClass().getCanonicalName());
        RakNetUtil.handlePackets(session, raknetPk, packet, true);
    }

    @Override
    public void handleMessage(RakNetServerSession session, RakNetPacket packet, int channel) {
        DragonProxy.getLogger().debug(sender + "Handling Message from Channel: " + channel + ": " + Integer.toHexString(packet.buffer().getByte(1)));
        handlePacket(packet, upstream);
    }

    @Override
    public void onConnect(RakNetServerSession session) {
        DragonProxy.getLogger().info(sender + "Remote pocket server downstream established!");

        while (!queuedPackets.isEmpty()) {
            RakNetPacket packet = queuedPackets.pop();
            DragonProxy.getLogger().debug(sender + "Handling queued packet: " + packet);
            sendPacket(packet);
        }
    }

    @Override
    public void onDisconnect(RakNetServerSession session, String reason) {
        DragonProxy.getLogger().info(sender + "Remote pocket server downstream CLOSED!\nReason: " + reason);
    }

    @Override
    public void onExternalServerAdded(InetSocketAddress address) {
    }

    @Override
    public void onExternalServerIdentifierUpdate(InetSocketAddress address, Identifier identifier) {
    }

    @Override
    public void onExternalServerRemoved(InetSocketAddress address) {
    }

    @Override
    public void onServerDiscovered(InetSocketAddress address, Identifier identifier) {
    }

    @Override
    public void onServerForgotten(InetSocketAddress address) {
    }

    @Override
    public void onServerIdentifierUpdate(InetSocketAddress address, Identifier identifier) {
    }

    @Override
    public void onAcknowledge(RakNetServerSession session, Record record) {
        DragonProxy.getLogger().debug(sender + "Recieved ACK");
    }

    @Override
    public void onNotAcknowledge(RakNetServerSession session, Record record) {
        DragonProxy.getLogger().debug(sender + "Did not recieve ACK");
    }

    @Override
    public void onThreadException(Throwable throwable) {
        DragonProxy.getLogger().severe(sender + "An unhandled thread exception has with the server occured");
        throwable.printStackTrace();
    }

    @Override
    public void onHandlerException(InetSocketAddress address, Throwable throwable) {
        DragonProxy.getLogger().severe(sender + "An unhandled exception has occured with the server session: " + address);
        throwable.printStackTrace();
    }

    @Override
    public void onWarning(Warning warning) {
        DragonProxy.getLogger().warning(sender + "Proxy Warn: " + warning.getMessage());
    }

    @Override
    public Class<RakNetPacket> getSupportedPacketType() {
        return RakNetPacket.class;
    }
}
