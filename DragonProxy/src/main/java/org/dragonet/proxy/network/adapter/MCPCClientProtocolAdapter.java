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

import java.util.UUID;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.PacketTranslatorRegister;
import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerBoundEvent;
import org.spacehq.packetlib.event.server.ServerClosedEvent;
import org.spacehq.packetlib.event.server.ServerClosingEvent;
import org.spacehq.packetlib.event.server.ServerListener;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectingEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

/**
 * This would be used to listen for MC PC client connections
 *
 * @author robotman3000
 */
public class MCPCClientProtocolAdapter extends SessionAdapter implements ClientProtocolAdapter<Packet>, ServerListener, ServerLoginHandler, ServerInfoBuilder {

    private Server server;

    public MCPCClientProtocolAdapter() {
        DragonProxy.getLogger().info("Starting up the Minecraft PC ClientProtocolAdapter");
        server = new Server(DragonProxy.getSelf().getConfig().getUdp_bind_ip(), DragonProxy.getSelf().getConfig().getUdp_bind_port(), MinecraftProtocol.class, new TcpSessionFactory());
        //server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, );
        server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, (DragonProxy.getSelf().getConfig().getMode().equalsIgnoreCase("online")));
        server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, this);
        server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, this);
        server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, 100);
        server.addListener(this);
        server.bind();
    }

    @Override
    public ServerStatusInfo buildInfo(Session session) {
        return new ServerStatusInfo(new VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION), new PlayerInfo(100, 0, new GameProfile[0]), new TextMessage("Hello world!"), null);
    }

    @Override
    public void sendPacket(Packet packet, ClientConnection id) {
        
        for (Session sess : server.getSessions()) {
            if (((GameProfile) sess.getFlag(MinecraftConstants.PROFILE_KEY)).getId().equals(id.getSessionID())) {
                DragonProxy.getLogger().debug("Sending Packet To Client: " + packet.getClass().getCanonicalName());
                sess.send(packet);
            }
        }
    }

    @Override
    public void clientDisconectRequest(ClientConnection id, String reason) {
        //TODO: Lazy
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onTick() {
    }

    @Override
    public void handlePacket(Packet packet, ClientConnection session) {
        DragonProxy.getLogger().debug("Handling Packet From Client " + session.getSessionID() + ": " + packet.getClass().getCanonicalName());
        
        if(packet.getClass().getCanonicalName().contains("KeepAlive")){
            DragonProxy.getLogger().debug("Client Ignoring KeepAlive"); 
            return;
        }
        
        Object[] packets = {packet};
        if (session.getDownstreamProtocol().getSupportedPacketType() != getSupportedPacketType()) {
            packets = PacketTranslatorRegister.translateToPE(session, packet);
        }

        for (Object pack : packets) {
            session.getDownstreamProtocol().sendPacket(pack);
        }
    }

    @Override
    public void serverBound(ServerBoundEvent sbe) {
    }

    @Override
    public void serverClosing(ServerClosingEvent sce) {
    }

    @Override
    public void serverClosed(ServerClosedEvent sce) {
    }

    @Override
    public void loggedIn(Session sn) {
        DragonProxy.getLogger().info("Client " + ((GameProfile) sn.getFlag(MinecraftConstants.PROFILE_KEY)).getName() + " has logged in with id " + ((GameProfile) sn.getFlag(MinecraftConstants.PROFILE_KEY)).getId());
        UUID sessionID = ((GameProfile) sn.getFlag(MinecraftConstants.PROFILE_KEY)).getId();
        ClientConnection clientSession = new ClientConnection(this, sessionID);
        clientSession.setUsername(((GameProfile) sn.getFlag(MinecraftConstants.PROFILE_KEY)).getName());
        if (!DragonProxy.getSelf().getNetwork().getSessionRegister().acceptConnection(clientSession)) {
            DragonProxy.getLogger().info("SessionRegister refused connection from " + clientSession.getSessionID());
            sn.disconnect("DragonProxy has refused your connection");
        } else {
            clientSession.onConnected();
            clientSession.connectToServer(DragonProxy.getSelf().getConfig().getRemote_servers().get(DragonProxy.getSelf().getConfig().getDefault_server()));
        }
    }

    @Override
    public void sessionAdded(SessionAddedEvent event) {
        event.getSession().addListener(this);
    }

    @Override
    public void sessionRemoved(SessionRemovedEvent event) {
    }

    @Override
    public void connected(ConnectedEvent event) {
        DragonProxy.getLogger().info("Connected to client " + event.getSession().getHost());
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        DragonProxy.getLogger().info("Disconected client " + event.getSession().getLocalAddress() + " for " + event.getReason());
        event.getCause().printStackTrace();
        ClientConnection client = DragonProxy.getSelf().getNetwork().getSessionRegister().getSession(((GameProfile) event.getSession().getFlag(MinecraftConstants.PROFILE_KEY)).getId());
        if(client != null){
            client.getDownstreamProtocol().disconnectFromRemoteServer(event.getReason());
        }
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        DragonProxy.getLogger().info("Disconecting client " + event.getSession().getLocalAddress() + " for " + event.getReason());
        event.getCause().printStackTrace();
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        DragonProxy.getLogger().debug("Received packet from client: " + event.getPacket().getClass().getCanonicalName());

        Session sess = event.getSession();

        if (((MinecraftProtocol) sess.getPacketProtocol()).getSubProtocol() == SubProtocol.GAME) {
            GameProfile profile = sess.getFlag(MinecraftConstants.PROFILE_KEY);
            UUID id = profile.getId();
            
            ClientConnection conn = DragonProxy.getSelf().getNetwork().getSessionRegister().getSession(id);
            if (conn != null){
                handlePacket(event.getPacket(), conn);
            } else {
                DragonProxy.getLogger().warning("ID: " + id + " had no session registered");
                sess.disconnect("Error! Session not registered");
            }
        }
    }

    @Override
    public void packetSent(PacketSentEvent event) {
        DragonProxy.getLogger().debug("Sent packet to client: " + event.getPacket().getClass().getCanonicalName());
    }

    @Override
    public Class<Packet> getSupportedPacketType() {
        return Packet.class;
    }
}
