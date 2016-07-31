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

import org.dragonet.net.packet.minecraft.AddItemEntityPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.metadata.ItemStack;
import org.spacehq.mc.protocol.data.game.entity.type.object.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;

public class PCEntityMetadataPacketTranslator implements PCPacketTranslator<ServerEntityMetadataPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerEntityMetadataPacket packet) {
        CachedEntity entity = session.getEntityCache().get(packet.getEntityId());
        if (entity == null) {
            return null;
        }
        if (!entity.spawned && entity.objType == ObjectType.ITEM) {
            entity.spawned = true;  //Spawned
            AddItemEntityPacket pk = new AddItemEntityPacket();
            pk.eid = packet.getEntityId();
            pk.item = ItemBlockTranslator.translateToPE((ItemStack) packet.getMetadata()[0].getValue());
            pk.x = (float) entity.x;
            pk.y = (float) entity.y;
            pk.z = (float) entity.z;
            pk.speedX = (float) entity.motionX;
            pk.speedY = (float) entity.motionY;
            pk.speedZ = (float) entity.motionZ;
            return new PEPacket[]{pk};
        }
        return null;
    }

}
