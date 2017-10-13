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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import sul.metadata.Bedrock137;
import sul.protocol.bedrock137.play.AddEntity;
import sul.utils.Packet;
import sul.utils.Tuples;

public class PCSpawnMobPacketTranslator implements PCPacketTranslator<ServerSpawnMobPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerSpawnMobPacket packet) {
        try {
            CachedEntity e = session.getEntityCache().newEntity(packet);
            if (e == null) {
                return null;
            }

            AddEntity pk = new AddEntity();
            pk.entityId = e.eid;
            pk.type = e.peType.getPeType();
            pk.position = new Tuples.FloatXYZ((float) e.x, (float) e.y, (float) e.z);
            pk.motion = new Tuples.FloatXYZ((float) e.motionX, (float) e.motionY, (float) e.motionZ);
            //TODO: Hack for now. ;P 
            pk.metadata = new Bedrock137();
            pk.metadata._buffer = EntityMetaTranslator.translateToPE(e.pcMeta, e.peType).encode();

            return new Packet[]{pk};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
