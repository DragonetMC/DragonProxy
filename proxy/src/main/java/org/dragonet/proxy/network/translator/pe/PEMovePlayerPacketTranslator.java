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
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import sul.protocol.bedrock137.play.MovePlayer;

public class PEMovePlayerPacketTranslator implements PEPacketTranslator<MovePlayer> {
    
    @Override
    public Packet[] translate(UpstreamSession session, MovePlayer packet) {
        ClientPlayerPositionRotationPacket pk = new ClientPlayerPositionRotationPacket(!packet.onGround, packet.position.x, packet.position.y, packet.position.z, packet.yaw, packet.pitch);
        CachedEntity cliEntity = session.getEntityCache().getClientEntity();
        cliEntity.x = packet.position.x;
        cliEntity.y = packet.position.y;
        cliEntity.z = packet.position.z;
        return new Packet[]{pk};
    }

}
