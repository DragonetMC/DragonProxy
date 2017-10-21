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

import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AddEntityPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCSpawnMobPacketTranslator implements PCPacketTranslator<ServerSpawnMobPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnMobPacket packet) {
        try {
            CachedEntity e = session.getEntityCache().newEntity(packet);
            if (e == null) {
                return null;
            }

            AddEntityPacket pk = new AddEntityPacket();
            pk.rtid = e.eid;
            pk.eid = e.eid;
            pk.type = e.peType.getPeType();
            pk.position = new Vector3F((float) e.x, (float) e.y, (float) e.z);
            pk.motion = new Vector3F((float) e.motionX, (float) e.motionY, (float) e.motionZ);
            //TODO: Hack for now. ;P 
            pk.meta = EntityMetaData.createDefault();

            return new PEPacket[]{pk};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
