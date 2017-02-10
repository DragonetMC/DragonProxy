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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.RemoteServer;
import lombok.Setter;
import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.Text;

import org.dragonet.proxy.network.adapter.ClientProtocolAdapter;
import org.dragonet.proxy.network.adapter.ServerProtocolAdapter;
import org.dragonet.proxy.network.cache.EntityCache;
import org.dragonet.proxy.network.cache.WindowCache;
import org.spacehq.mc.protocol.data.game.PlayerListEntry;

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
    
    /* ======================================================================================================= 
     * |                                 Caches for Protocol Compatibility                                   | 
    /* ======================================================================================================= */ 
    
    @Getter 
    private final Map<String, Object> dataCache = Collections.synchronizedMap(new HashMap<String, Object>()); 
 
    @Getter 
    private final Map<UUID, PlayerListEntry> playerInfoCache = Collections.synchronizedMap(new HashMap<UUID, PlayerListEntry>()); 
 
    @Getter 
    private final EntityCache entityCache = new EntityCache(this); 
 
    @Getter 
    private final WindowCache windowCache = new WindowCache(this); 

    public ClientConnection(ClientProtocolAdapter upstream, UUID sessionID) {
        this.upstreamProtocol = upstream;
        this.sessionID = sessionID;
        //packetProcessor = new PEPacketProcessor(this);
        //packetProcessorScheule = DragonProxy.getSelf().getGeneralThreadPool().scheduleAtFixedRate(packetProcessor, 10, 50, TimeUnit.MILLISECONDS);
        status = ConnectionStatus.AWAITING_CLIENT_LOGIN;
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
    
    @SuppressWarnings("unchecked")
	public void sendChat(String msg)	{
    	upstreamProtocol.sendPacket(new RakNetPacket(new Text().new Raw(msg).encode()), this);
    }
    
    @SuppressWarnings("unchecked")
	public void sendAllPackets(Object[] pks, boolean idkSoIgnoreIt)	{
    	for (Object obj : pks)	{
    		if (obj instanceof RakNetPacket)	{
    			upstreamProtocol.sendPacket((RakNetPacket) obj, this);
    		}
    	}
    }
}
