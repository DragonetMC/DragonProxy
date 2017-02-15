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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.packetlib.packet.Packet;

import sul.protocol.pocket101.play.MovePlayer;

public class PEMovePlayerPacketTranslator implements PEPacketTranslator<MovePlayer> {
    
    @Override
    public Packet[] translate(ClientConnection session, MovePlayer packet) {
        ClientPlayerPositionRotationPacket pk = new ClientPlayerPositionRotationPacket(!packet.onGround, packet.position.x, packet.position.y, packet.position.z, packet.yaw, packet.pitch);
        CachedEntity cliEntity = session.getEntityCache().getClientEntity();
        cliEntity.x = packet.position.x;
        cliEntity.y = packet.position.y;
        cliEntity.z = packet.position.z;        
        return new Packet[]{pk};
    }

}
