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

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.dragonet.proxy.data.entity.PEEntityAttribute;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AddEntityPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCSpawnMobPacketTranslator implements IPCPacketTranslator<ServerSpawnMobPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerSpawnMobPacket packet) {
        try {
            CachedEntity entity = session.getEntityCache().newEntity(packet);
            if (entity == null) {
                return null;
            }

            AddEntityPacket pk = new AddEntityPacket();
            pk.rtid = entity.proxyEid;
            pk.eid = entity.proxyEid;
            pk.type = entity.peType.getPeType();
            pk.position = new Vector3F((float) entity.x, (float) entity.y - entity.peType.getOffset(), (float) entity.z);
            pk.motion = new Vector3F((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
            pk.yaw = entity.yaw;
            pk.pitch = entity.pitch;
            pk.meta = EntityMetaTranslator.translateToPE(session, entity.pcMeta, entity.peType);
            // TODO: Hack for now. ;P
            pk.attributes = new PEEntityAttribute[]{};
            entity.spawned = true;
            return new PEPacket[]{pk};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
