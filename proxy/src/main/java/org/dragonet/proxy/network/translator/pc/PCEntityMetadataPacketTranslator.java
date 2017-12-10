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

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AddItemEntityPacket;
import org.dragonet.proxy.utilities.Vector3F;

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
		if (!entity.spawned && entity.objType == ObjectType.ITEM) {
			entity.spawned = true; // Spawned
			AddItemEntityPacket pk = new AddItemEntityPacket();
			pk.eid = entity.proxyEid;
			pk.rtid = entity.proxyEid;
			pk.item = ItemBlockTranslator.translateSlotToPE((ItemStack) packet.getMetadata()[6].getValue()); //dirty for now
			pk.position = new Vector3F((float) entity.x, (float) entity.y, (float) entity.z);
			pk.motion = new Vector3F((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
			return new PEPacket[] { pk };
		}
		return null;
	}

	// private

}
