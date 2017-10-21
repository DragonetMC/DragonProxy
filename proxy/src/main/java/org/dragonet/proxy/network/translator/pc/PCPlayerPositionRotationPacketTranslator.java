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

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.dragonet.proxy.protocol.packets.MovePlayerPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCPlayerPositionRotationPacketTranslator implements PCPacketTranslator<ServerPlayerPositionRotationPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPlayerPositionRotationPacket packet) {
        MovePlayerPacket pk = new MovePlayerPacket();
        pk.rtid = 0;
        pk.mode = MovePlayerPacket.MODE_TELEPORT;
        pk.position = new Vector3F((float) packet.getX(), (float) packet.getY(), (float) packet.getZ());
        pk.yaw = packet.getYaw();
        pk.pitch = packet.getPitch();
        pk.headYaw = packet.getYaw();
        CachedEntity cliEntity = session.getEntityCache().getClientEntity();
        cliEntity.x = packet.getX();
        cliEntity.y = packet.getY();
        cliEntity.z= packet.getZ();
        return new PEPacket[]{pk};
    }

}
