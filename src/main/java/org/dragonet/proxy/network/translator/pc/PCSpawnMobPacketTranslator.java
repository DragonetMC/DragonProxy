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

import org.dragonet.proxy.data.entity.meta.EntityMetaData;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.data.entity.PEEntityAttribute;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AddEntityPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCSpawnMobPacketTranslator implements IPCPacketTranslator<ServerSpawnMobPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerSpawnMobPacket packet) {
        try {
            CachedEntity e = session.getEntityCache().newEntity(packet);
//            System.out.println("ServerSpawnObjectPacket register " + packet.getEntityId() + " entity " + (packet.getMetadata().length > 0 ? "has meta" : "no meta"));
            if (e == null) {
                return null;
            }

            AddEntityPacket pk = new AddEntityPacket();
            pk.rtid = e.proxyEid;
            pk.eid = e.proxyEid;
            pk.type = e.peType.getPeType();
            pk.position = new Vector3F((float) e.x, (float) e.y, (float) e.z);
            pk.motion = new Vector3F((float) e.motionX, (float) e.motionY, (float) e.motionZ);
            pk.meta = EntityMetaTranslator.translateToPE(e.pcMeta, e.peType);
            // TODO: Hack for now. ;P
            pk.attributes = new PEEntityAttribute[]{};
            e.spawned = true;
            return new PEPacket[]{pk};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
