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
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;

import cn.nukkit.network.protocol.MoveEntityPacket;
import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.MoveEntity;
import sul.utils.Tuples;

public class PCEntityPositionPacketTranslator implements PCPacketTranslator<ServerEntityPositionPacket> {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, ServerEntityPositionPacket packet) {
        CachedEntity e = session.getEntityCache().get(packet.getEntityId());
        if (e == null) {
            return new sul.utils.Packet[0];
        }

        e.relativeMove(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ());

        MoveEntity pk = new MoveEntity();
        pk.entityId = e.eid;
        pk.yaw = (byte) e.yaw;
        pk.headYaw = (byte) e.yaw;
        pk.pitch = (byte) e.pitch;
        pk.position = new Tuples.FloatXYZ((float) e.x, (float) (e.player ? e.y + 1.62f : e.y), (float) e.z); 
        
        return new sul.utils.Packet[] {pk};
    }

}
