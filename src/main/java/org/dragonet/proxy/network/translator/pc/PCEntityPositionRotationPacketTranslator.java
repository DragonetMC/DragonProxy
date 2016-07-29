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

import org.dragonet.net.packet.minecraft.MoveEntitiesPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;

public class PCEntityPositionRotationPacketTranslator implements PCPacketTranslator<ServerEntityPositionRotationPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerEntityPositionRotationPacket packet) {
        CachedEntity e = session.getEntityCache().get(packet.getEntityId());
        if (e == null) {
            return null;
        }

        e.relativeMove(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ(), packet.getYaw(), packet.getPitch());

        MoveEntitiesPacket.MoveEntityData data = new MoveEntitiesPacket.MoveEntityData();
        data.eid = e.eid;
        data.yaw = e.yaw;
        data.headYaw = e.yaw;
        data.pitch = e.pitch;
        data.x = (float) e.x;
        data.y = (float) e.y;
        if(e.player){
            data.y += 1.62f;
        }
        data.z = (float) e.z;
        MoveEntitiesPacket pk = new MoveEntitiesPacket(new MoveEntitiesPacket.MoveEntityData[]{data});
        return new PEPacket[]{pk};
    }

}
