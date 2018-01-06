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
package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.common.mcbedrock.blocks.Block;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;
import org.dragonet.common.mcbedrock.data.entity.EntityType;
import org.dragonet.common.mcbedrock.protocol.packets.MovePlayerPacket;

public class PEMovePlayerPacketTranslator implements IPEPacketTranslator<MovePlayerPacket> {

    public Packet[] translate(UpstreamSession session, MovePlayerPacket packet) {
        CachedEntity entity = session.getEntityCache().getClientEntity();

        if (entity.riding != 0 && packet.ridingRuntimeId != 0) { //Riding case
            ClientVehicleMovePacket pk = new ClientVehicleMovePacket(
                packet.position.x,
                packet.position.y - EntityType.PLAYER.getOffset(),
                packet.position.z,
                packet.yaw,
                packet.pitch);
            session.getDownstream().send(pk);
        } else { //not riding
            ClientPlayerPositionRotationPacket pk = new ClientPlayerPositionRotationPacket(
                packet.onGround,
                packet.position.x,
                ceilToHalf(packet.position.y - EntityType.PLAYER.getOffset()), // To simplify the movements
                packet.position.z,
                packet.yaw,
                packet.pitch);

            session.getEntityCache().getClientEntity().absoluteMove(pk.getX(), pk.getY(), pk.getZ(), (float)pk.getYaw(), (float)pk.getPitch());

            boolean isColliding = false;
            for (Block b: session.getBlockCache().getAllBlocks()) {
                if (b.collidesWithBB(session.getEntityCache().getClientEntity().boundingBox.clone())) {
                    DragonProxy.getInstance().getLogger().debug("Colliding packet, skipping");
                    isColliding = true;
                }
            }

            if (!isColliding)
                session.getDownstream().send(pk);
        }
        return null;
    }

    public double ceilToHalf(double value) {
        return Math.ceil(value * 2) / 2;
    }
}
