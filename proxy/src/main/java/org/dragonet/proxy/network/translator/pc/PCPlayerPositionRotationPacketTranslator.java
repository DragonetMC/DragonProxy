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
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;

public class PCPlayerPositionRotationPacketTranslator implements PCPacketTranslator<ServerPlayerPositionRotationPacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerPlayerPositionRotationPacket packet) {
        MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
        pkMovePlayer.eid = 0;
        pkMovePlayer.x = (float) packet.getX();
        pkMovePlayer.y = (float) packet.getY();
        pkMovePlayer.z = (float) packet.getZ();
        pkMovePlayer.headYaw = 0.0f;
        pkMovePlayer.yaw = 0.0f;
        pkMovePlayer.pitch = 0.0f;
        pkMovePlayer.onGround = false;
        pkMovePlayer.mode = MovePlayerPacket.MODE_RESET;
        
        CachedEntity cliEntity = session.getEntityCache().getClientEntity();
        cliEntity.x = packet.getX();
        cliEntity.y = packet.getY();
        cliEntity.z= packet.getZ();
        return new DataPacket[]{pkMovePlayer};
    }

}
