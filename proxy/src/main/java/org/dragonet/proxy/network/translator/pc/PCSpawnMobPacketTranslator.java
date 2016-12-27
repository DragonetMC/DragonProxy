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
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;

import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;

public class PCSpawnMobPacketTranslator implements PCPacketTranslator<ServerSpawnMobPacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerSpawnMobPacket packet) {
        try {
            CachedEntity e = session.getEntityCache().newEntity(packet);
            if (e == null) {
                return null;
            }

            AddEntityPacket pk = new AddEntityPacket();
            pk.entityRuntimeId = e.eid;
            pk.type = e.peType.getPeType();
            pk.x = (float) e.x;
            pk.y = (float) e.y;
            pk.z = (float) e.z;
            pk.speedX = (float) e.motionX;
            pk.speedY = (float) e.motionY;
            pk.speedZ = (float) e.motionZ;
            //TODO: Hack for now. ;P 
            pk.metadata = EntityMetaTranslator.translateToPE(e.pcMeta, e.peType);

            return new DataPacket[]{pk};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
