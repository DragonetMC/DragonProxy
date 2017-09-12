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

import org.dragonet.proxy.protocol.packet.AddEntityPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;

public class PCSpawnMobPacketTranslator implements PCPacketTranslator<ServerSpawnMobPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnMobPacket packet) {
        try {
            CachedEntity e = session.getEntityCache().newEntity(packet);
            if (e == null) {
                return null;
            }

            AddEntityPacket pk = new AddEntityPacket();
            pk.eid = e.eid;
            pk.type = e.peType.getPeType();
            pk.x = (float) e.x;
            pk.y = (float) e.y;
            pk.z = (float) e.z;
            pk.speedX = (float) e.motionX;
            pk.speedY = (float) e.motionY;
            pk.speedZ = (float) e.motionZ;
            //TODO: Hack for now. ;P 
            pk.meta = EntityMetaTranslator.translateToPE(e.pcMeta, e.peType);

            return new PEPacket[]{pk};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
