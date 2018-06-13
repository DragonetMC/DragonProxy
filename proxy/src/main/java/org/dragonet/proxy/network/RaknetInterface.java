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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.whirvis.jraknet.RakNetPacket;
import com.whirvis.jraknet.identifier.MinecraftIdentifier;
import com.whirvis.jraknet.server.RakNetServer;
import com.whirvis.jraknet.server.RakNetServerListener;
import com.whirvis.jraknet.server.ServerPing;
import com.whirvis.jraknet.session.RakNetClientSession;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.sessions.ISessionRegister;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.translator.MessageTranslator;
import org.dragonet.proxy.utilities.pingpassthrough.PingThread;

public class RaknetInterface implements RakNetServerListener {

    public static final Set<String> IMMEDIATE_PACKETS = new HashSet<>();

    private final ProxyServer proxy;
    private final ISessionRegister sessions;
    private final RakNetServer rakServer;

    private long serverId = new Random().nextLong();
    private String serverName;
    private int maxPlayers;
    private final ScheduledFuture updatePing;

    static {
        IMMEDIATE_PACKETS.add("PlayStatus");
    }

    public RaknetInterface(ProxyServer proxy, String ip, int port, String serverName, int maxPlayers) {
        this.proxy = proxy;
        this.serverName = serverName;
        this.maxPlayers = maxPlayers;
        this.rakServer = new RakNetServer(port, Integer.MAX_VALUE);
        this.rakServer.addListener(this);
        this.rakServer.addSelfListener();
        this.rakServer.setBroadcastingEnabled(true);
        this.sessions = this.proxy.getSessionRegister();
        this.rakServer.startThreaded();
        this.updatePing = proxy.getGeneralThreadPool().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //TODO option to display minecraft server players instead of proxy
                setBroadcastName(getServerName(), sessions.getOnlineCount(), getMaxPlayers());
            }
        }, 500, 500, TimeUnit.MILLISECONDS);
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public String getServerName() {
        return serverName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public RakNetServer getRakServer() {
        return rakServer;
    }

    @Override
    public void handlePing(ServerPing ping) {
        DragonProxy.getInstance().getLogger().debug("PING " + ping.getSender().toString());
    }

    @Override
    public void handleMessage(RakNetClientSession session, RakNetPacket packet, int channel) {
        IUpstreamSession upstream = sessions.getSession(session.getAddress().toString());
        if (upstream == null)
            return;
        // System.out.println("Received RakNet packet: " +
        // packet.getClass().getSimpleName());
        upstream.handlePacketBinary(packet.array());
    }

    @Override
    public void onClientConnect(RakNetClientSession session) {
        DragonProxy.getInstance().getLogger().debug("CLIENT CONNECT");
        String identifier = session.getAddress().toString();
        UpstreamSession upstream = new UpstreamSession(proxy, identifier, session, session.getAddress());
        sessions.newSession(upstream);
    }

    @Override
    public void onClientDisconnect(RakNetClientSession session, String reason) {
        DragonProxy.getInstance().getLogger().debug("CLIENT DISCONNECT");
        UpstreamSession upstream = sessions.getSession(session.getAddress().toString());
        if (upstream == null)
            return;
        upstream.onDisconnect(proxy.getLang().get(Lang.MESSAGE_CLIENT_DISCONNECT)); // It will handle rest of the
        // things.
    }

    @Override
    public void onThreadException(Throwable throwable) {
        DragonProxy.getInstance().getLogger().debug("Thread exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void onHandlerException(InetSocketAddress address, Throwable throwable) {
        DragonProxy.getInstance().getLogger().debug("Handler exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void onSessionException(RakNetClientSession session, Throwable throwable) {
        DragonProxy.getInstance().getLogger().debug("Session exception: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void setBroadcastName(String serverName, int players, int maxPlayers) {
        if (maxPlayers == -1)
            maxPlayers = Integer.MAX_VALUE;

        if (DragonProxy.getInstance().getConfig().ping_passthrough && PingThread.getInstance() != null) {
            ServerStatusInfo info = PingThread.getInstance().getInfo();
            if (info == null) {
                // server offline, should probably do something
            } else {
                String motd = MessageTranslator.translate(info.getDescription())
                        .replace("§k", "") // disabled due to &r not working (?)
                        .replace("\n", ""); // multiline is not supported atm
                rakServer.setIdentifier(
                        new MinecraftIdentifier(motd, ProtocolInfo.CURRENT_PROTOCOL, ProtocolInfo.MINECRAFT_VERSION_NETWORK,
                                info.getPlayerInfo().getOnlinePlayers(), info.getPlayerInfo().getMaxPlayers(), serverId, "DragonProxy", "Survival"));
            }
        } else {
            rakServer.setIdentifier(
                    new MinecraftIdentifier(serverName, ProtocolInfo.CURRENT_PROTOCOL, ProtocolInfo.MINECRAFT_VERSION_NETWORK,
                            players, maxPlayers, serverId, "DragonProxy", "Survival"));
        }

        if (!rakServer.isBroadcastingEnabled())
            rakServer.setBroadcastingEnabled(true);
    }

    public void shutdown() {
        updatePing.cancel(true);
        rakServer.shutdown();
    }
}
