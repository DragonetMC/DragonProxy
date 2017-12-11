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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.SetEntityDataPacket;

public class PCEntityMetadataPacketTranslator implements IPCPacketTranslator<ServerEntityMetadataPacket> {
	// vars

	// constructor
	public PCEntityMetadataPacketTranslator() {

	}

	// public
	public PEPacket[] translate(UpstreamSession session, ServerEntityMetadataPacket packet) {
		CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
		if (entity == null) {
			return null;
		}
                entity.pcMeta = packet.getMetadata();
                SetEntityDataPacket pk = new SetEntityDataPacket();
                pk.rtid = entity.proxyEid;
                pk.meta = EntityMetaTranslator.translateToPE(packet.getMetadata(), entity.peType);
                session.sendPacket(pk);
		return null;
	}

	// private

}
