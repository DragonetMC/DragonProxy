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
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;

public class PCEntityPositionRotationPacketTranslator implements PCPacketTranslator<ServerEntityPositionRotationPacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerEntityPositionRotationPacket packet) {
        CachedEntity e = session.getEntityCache().get(packet.getEntityId());
        if (e == null) {
            return null;
        }

        e.relativeMove(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ(), packet.getYaw(), packet.getPitch());

        MoveEntityPacket pk = new MoveEntityPacket();
        pk.eid = e.eid;
        pk.yaw = e.yaw;
        pk.headYaw = e.yaw;
        pk.pitch = e.pitch;
        pk.x = (float) e.x;
        pk.y = (float) e.y;
        if(e.player){
            pk.y += 1.62f;
        }
        pk.z = (float) e.z;
        return new DataPacket[]{pk};
    }

}
