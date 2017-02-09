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

import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.RemoteServer;
import lombok.Setter;
import org.dragonet.proxy.network.adapter.ClientProtocolAdapter;
import org.dragonet.proxy.network.adapter.ServerProtocolAdapter;
import org.dragonet.proxy.network.cache.EntityCache;

/**
 * Maintaince the connection between the proxy and Minecraft: Pocket Edition
 * clients.
 */
public class ClientConnection {

    //@Getter
    //private final PEPacketProcessor packetProcessor;

    //private final ScheduledFuture<?> packetProcessorScheule;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private ConnectionStatus status = ConnectionStatus.UNCONNECTED;

    @Getter
    private ServerProtocolAdapter downstreamProtocol;

    @Getter
    private ClientProtocolAdapter upstreamProtocol;
    private UUID sessionID;
    
    @Getter
    private EntityCache entityCache;

    public ClientConnection(ClientProtocolAdapter upstream, UUID sessionID) {
        this.upstreamProtocol = upstream;
        this.sessionID = sessionID;
        //packetProcessor = new PEPacketProcessor(this);
        //packetProcessorScheule = DragonProxy.getSelf().getGeneralThreadPool().scheduleAtFixedRate(packetProcessor, 10, 50, TimeUnit.MILLISECONDS);
        status = ConnectionStatus.AWAITING_CLIENT_LOGIN;
        this.entityCache = new EntityCache(this);
    }

    public void onTick() {
        //entityCache.onTick();
        if (downstreamProtocol != null) {
            //downstreamProtocol.onTick();
        }
    }

    public void onConnected() {
        status = ConnectionStatus.CONNECTED;
    }

    public void connectToServer(RemoteServer server) {
        if (server != null) {
            status = ConnectionStatus.CONNECTING_SERVER;
            downstreamProtocol = server.getProtocolAdapter(this);
            downstreamProtocol.connectToRemoteServer(server.getRemoteAddr(), server.getRemotePort());
        }
    }

    public UUID getSessionID() {
        return sessionID;
    }
}
