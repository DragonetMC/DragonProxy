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
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.dragonet.proxy.protocol.packets.MovePlayerPacket;
import org.dragonet.proxy.utilities.Constants;

public class PEMovePlayerPacketTranslator implements IPEPacketTranslator<MovePlayerPacket> {

    public PEMovePlayerPacketTranslator() {

    }

    public Packet[] translate(UpstreamSession session, MovePlayerPacket packet) {
        ClientPlayerPositionRotationPacket pk = new ClientPlayerPositionRotationPacket(packet.onGround,
                packet.position.x, packet.position.y - Constants.PLAYER_HEAD_OFFSET, packet.position.z, packet.headYaw,
                packet.pitch);
        CachedEntity cliEntity = session.getEntityCache().getClientEntity();
        cliEntity.x = packet.position.x;
        cliEntity.y = packet.position.y - Constants.PLAYER_HEAD_OFFSET;
        cliEntity.z = packet.position.z;
        return new Packet[]{pk};
    }
}
