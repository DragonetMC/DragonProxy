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
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.UpdateBlockPacket;
import org.dragonet.proxy.utilities.BlockPosition;

public class PCMultiBlockChangePacketTranslator implements IPCPacketTranslator<ServerMultiBlockChangePacket> {
	// vars

	// constructor
	public PCMultiBlockChangePacketTranslator() {

	}

	// public
	public PEPacket[] translate(UpstreamSession session, ServerMultiBlockChangePacket packet) {
		UpdateBlockPacket[] packets = new UpdateBlockPacket[packet.getRecords().length];
		int generalFlag = packet.getRecords().length > 64 ? UpdateBlockPacket.FLAG_ALL_PRIORITY
				: UpdateBlockPacket.FLAG_NEIGHBORS;
		for (int i = 0; i < packets.length; i++) {
			packets[i] = new UpdateBlockPacket();
			packets[i].blockPosition = new BlockPosition(packet.getRecords()[i].getPosition().getX(),
					packet.getRecords()[i].getPosition().getY(), packet.getRecords()[i].getPosition().getZ());
			packets[i].id = (byte) (ItemBlockTranslator.translateToPE(packet.getRecords()[i].getBlock().getId())
					& 0xFF);
			packets[i].data = generalFlag;
			packets[i].flags = packet.getRecords()[i].getBlock().getData();
		}
		return packets;
	}

	// private

}
