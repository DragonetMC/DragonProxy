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
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.metadata.Pocket100;
import sul.protocol.pocket100.play.AddEntity;
import sul.protocol.pocket100.types.Attribute;
import sul.utils.Tuples;

public class PCSpawnMobPacketTranslator implements PCPacketTranslator<ServerSpawnMobPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerSpawnMobPacket packet) {
        try {
            CachedEntity e = session.getEntityCache().newEntity(packet);
            if (e == null) {
                return null;
            }

            AddEntity pk = new AddEntity();
            pk.entityId = e.eid;
            pk.runtimeId = e.eid;
            pk.type = e.peType.getPeType();
            pk.position = new Tuples.FloatXYZ((float) e.x, (float) e.y, (float) e.z);
            pk.motion = new Tuples.FloatXYZ((float) e.motionX, (float) e.motionY, (float) e.motionZ);
            pk.pitch = e.pitch;
            pk.yaw = e.yaw;
            pk.attributes = new Attribute[0];
            pk.metadata = new Pocket100();
            pk.links = new long[0];
            
            return fromSulPackets(pk);
        } catch (Exception e) {
            e.printStackTrace();
            return new RakNetPacket[0];
        }
    }

}
