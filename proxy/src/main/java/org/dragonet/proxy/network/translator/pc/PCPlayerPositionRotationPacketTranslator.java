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

import org.dragonet.proxy.protocol.packet.MovePlayerPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;

public class PCPlayerPositionRotationPacketTranslator implements PCPacketTranslator<ServerPlayerPositionRotationPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPlayerPositionRotationPacket packet) {
        MovePlayerPacket pk = new MovePlayerPacket(0, (float) packet.getX(), (float) packet.getY(), (float) packet.getZ(), packet.getYaw(), packet.getPitch(), packet.getYaw(), false);
        CachedEntity cliEntity = session.getEntityCache().getClientEntity();
        cliEntity.x = packet.getX();
        cliEntity.y = packet.getY();
        cliEntity.z= packet.getZ();
        return new PEPacket[]{pk};
    }

}
