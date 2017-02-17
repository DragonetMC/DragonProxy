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
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.MovePlayer;
import sul.utils.Tuples;

public class PCPlayerPositionRotationPacketTranslator implements PCPacketTranslator<ServerPlayerPositionRotationPacket> {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, ServerPlayerPositionRotationPacket packet) {
        MovePlayer pkMovePlayer = new MovePlayer();
        pkMovePlayer.entityId = 0;
        pkMovePlayer.position = new Tuples.FloatXYZ((float) packet.getX(), (float) packet.getY(), (float) packet.getZ());
        pkMovePlayer.headYaw = packet.getYaw();
        pkMovePlayer.yaw = packet.getYaw();
        pkMovePlayer.pitch = packet.getPitch();
        pkMovePlayer.onGround = false;
        pkMovePlayer.animation = MovePlayer.FULL;
        
        CachedEntity cliEntity = session.getEntityCache().getClientEntity();
        cliEntity.x = packet.getX();
        cliEntity.y = packet.getY();
        cliEntity.z = packet.getZ();
        return new sul.utils.Packet[] {pkMovePlayer};
    }

}
