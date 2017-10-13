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
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.bedrock137.play.AddItemEntity;
import sul.utils.Packet;
import sul.utils.Tuples;

public class PCEntityMetadataPacketTranslator implements PCPacketTranslator<ServerEntityMetadataPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerEntityMetadataPacket packet) {
        CachedEntity entity = session.getEntityCache().get(packet.getEntityId());
        if (entity == null) {
            return null;
        }
        if (!entity.spawned && entity.objType == ObjectType.ITEM) {
            entity.spawned = true;  //Spawned
            AddItemEntity pk = new AddItemEntity();
            pk.entityId = packet.getEntityId();
            pk.item = ItemBlockTranslator.translateToPE((ItemStack) packet.getMetadata()[0].getValue());
            pk.position = new Tuples.FloatXYZ((float) entity.x, (float) entity.y, (float) entity.z);
            pk.motion = new Tuples.FloatXYZ((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
            return new Packet[]{pk};
        }
        return null;
    }

}
