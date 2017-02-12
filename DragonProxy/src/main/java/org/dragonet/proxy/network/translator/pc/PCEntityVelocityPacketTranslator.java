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
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.SetEntityMotion;
import sul.utils.Tuples;

public class PCEntityVelocityPacketTranslator implements PCPacketTranslator<ServerEntityVelocityPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerEntityVelocityPacket packet) {
        CachedEntity e = session.getEntityCache().get(packet.getEntityId());
        if (e == null) {
            return new RakNetPacket[0];
        }
        e.motionX = packet.getMotionX();
        e.motionY = packet.getMotionY();
        e.motionZ = packet.getMotionZ();

        SetEntityMotion pk = new SetEntityMotion();
        pk.entityId = packet.getEntityId();
        pk.motion = new Tuples.FloatXYZ((float) packet.getMotionX(), (float) packet.getMotionY(), (float) packet.getMotionZ());
        
        return fromSulPackets(pk);
    }

}
