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
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.data.entity.PEEntityAttribute;
import org.dragonet.proxy.data.entity.meta.EntityMetaData;
import org.dragonet.proxy.data.entity.meta.type.SlotMeta;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AddEntityPacket;
import org.dragonet.proxy.protocol.packets.AddItemEntityPacket;
import org.dragonet.proxy.protocol.packets.AddPaintingPacket;
import org.dragonet.proxy.protocol.packets.SetEntityDataPacket;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.DebugTools;
import org.dragonet.proxy.utilities.Vector3F;

public class PCEntityMetadataPacketTranslator implements IPCPacketTranslator<ServerEntityMetadataPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityMetadataPacket packet) {
        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());

        if (entity == null) {
            return null;
        }

        if (entity.peType == EntityType.PAINTING) {
            System.out.println("PAINTING !");
        }

        entity.pcMeta = packet.getMetadata();
        if (entity.spawned) {
            SetEntityDataPacket pk = new SetEntityDataPacket();
            pk.rtid = entity.proxyEid;
            pk.meta = EntityMetaTranslator.translateToPE(session, packet.getMetadata(), entity.peType);
            session.sendPacket(pk);
        } else {
            if (entity.peType == EntityType.ITEM) {
                AddItemEntityPacket pk = new AddItemEntityPacket();
                pk.rtid = entity.proxyEid;
                pk.eid = entity.proxyEid;
                pk.metadata = EntityMetaTranslator.translateToPE(session, packet.getMetadata(), entity.peType);
                pk.item = ((SlotMeta) pk.metadata.map.get(EntityMetaData.Constants.DATA_TYPE_SLOT)).slot;
                pk.position = new Vector3F((float) entity.x, (float) entity.y + entity.peType.getOffset(), (float) entity.z);
                pk.motion = new Vector3F((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
                entity.spawned = true;
                session.sendPacket(pk);
            } else if (entity.peType == EntityType.PAINTING) {
//                AddPaintingPacket pk = new AddPaintingPacket();
//                pk.rtid = entity.proxyEid;
//                pk.eid = entity.proxyEid;
//                pk.pos = new BlockPosition((int) entity.x, (int) entity.y, (int) entity.z);
//                pk.direction = 1;
//                pk.title = "Kebab";
//                entity.spawned = true;
//                session.sendPacket(pk);
            } else {
                AddEntityPacket pk = new AddEntityPacket();
                pk.rtid = entity.proxyEid;
                pk.eid = entity.proxyEid;
                pk.type = entity.peType.getPeType();
                pk.position = new Vector3F((float) entity.x, (float) entity.y + entity.peType.getOffset(), (float) entity.z);
                pk.motion = new Vector3F((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
                pk.yaw = (byte) (entity.yaw + 90 / (360d / 256d));
                pk.pitch = entity.pitch;
                pk.meta = EntityMetaTranslator.translateToPE(session, entity.pcMeta, entity.peType);
                entity.spawned = true;
                // TODO: Hack for now. ;P
                pk.attributes = new PEEntityAttribute[]{};
                session.sendPacket(pk);
            }
        }
        return null;
    }
}
