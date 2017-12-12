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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AnimatePacket;

public class PCAnimationPacketTranslator implements IPCPacketTranslator<ServerEntityAnimationPacket> {
	// vars

	// constructor
	public PCAnimationPacketTranslator() {

	}

	// public
	public PEPacket[] translate(UpstreamSession session, ServerEntityAnimationPacket packet) {
            
            CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
		if (entity == null) {
			return null;
		}
		AnimatePacket pk = new AnimatePacket();
                pk.action = packet.getAnimation().ordinal();
                pk.eid = entity.proxyEid;
                System.out.println(packet.getAnimation().name());
		return new PEPacket[] { pk };
	}

	// private

}
