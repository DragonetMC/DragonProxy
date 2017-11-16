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
package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import org.dragonet.proxy.protocol.packets.InteractPacket;

public class PEInteractPacketTranslator implements IPEPacketTranslator<InteractPacket> {
	// vars

	// constructor
	public PEInteractPacketTranslator() {

	}

	// public
	public Packet[] translate(UpstreamSession session, InteractPacket packet) {
		ClientPlayerInteractEntityPacket pk = new ClientPlayerInteractEntityPacket(
				(int) (packet.targetRtid & 0xFFFFFFFF), InteractAction.ATTACK);
		return new Packet[] { pk };
	}

	// private

}
