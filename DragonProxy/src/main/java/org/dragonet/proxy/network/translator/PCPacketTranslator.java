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
package org.dragonet.proxy.network.translator;

import net.marfgamer.jraknet.RakNetPacket;
import org.dragonet.proxy.network.ClientConnection;
import org.spacehq.packetlib.packet.Packet;

public interface PCPacketTranslator<P extends Packet> {

    /**
     * Translate a packet from PC version to PE version.
     *
     * @param session
     * @param packet
     * @return
     */
    public RakNetPacket[] translate(ClientConnection session, P packet);
    
    public default RakNetPacket[] fromSulPackets(Object... packets)	{
    	RakNetPacket[] ret = new RakNetPacket[packets.length];
    	for (int i = 0; i < packets.length; i++)	{
    		if (packets[i] instanceof sul.utils.Packet)	{
    			ret[i] = new RakNetPacket(((sul.utils.Packet) packets[i]).encode());
    		} else if (packets[i] instanceof cn.nukkit.network.protocol.DataPacket)	{
    			((cn.nukkit.network.protocol.DataPacket) packets[i]).encode();
    			ret[i] = new RakNetPacket(((cn.nukkit.network.protocol.DataPacket) packets[i]).getByteArray());
    		} else {
    			System.err.println("Non-packet object passed to org.dragonet.proxy.network.translator.PCPacketTranslator.fromSulPackets(), aborting");
    			//System.exit(1);
    		}
    	}
    	return ret;
    }
}
