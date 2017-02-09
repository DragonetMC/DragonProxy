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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.metadata.ItemStack;
import org.spacehq.mc.protocol.data.game.entity.type.object.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.AddItemEntity;
import sul.protocol.pocket100.types.Slot;
import sul.utils.Item;
import sul.utils.Tuples;

public class PCEntityMetadataPacketTranslator implements PCPacketTranslator<ServerEntityMetadataPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerEntityMetadataPacket packet) {
        CachedEntity entity = session.getEntityCache().get(packet.getEntityId());
        if (entity == null) {
            return null;
        }
        if (!entity.spawned && entity.objType == ObjectType.ITEM) {
            entity.spawned = true;  //Spawned
            AddItemEntity pk = new AddItemEntity();
            pk.runtimeId = packet.getEntityId();
            pk.entityId = packet.getEntityId(); //Not sure if both of these are needed, but adding them to be sure
            Item item = ItemBlockTranslator.translateToPE((ItemStack) packet.getMetadata()[0].getValue());
            pk.item = new Slot(item.id, item.meta, new byte[0]);
            pk.position = new Tuples.FloatXYZ((float) entity.x, (float) entity.y, (float) entity.z);
            pk.motion = new Tuples.FloatXYZ((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
            return new RakNetPacket[]{new RakNetPacket(pk.encode())};
        }
        return null;
    }

}
