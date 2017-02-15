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

import org.dragonet.proxy.protocol.packet.InteractPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.data.game.values.entity.player.InteractAction;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import org.spacehq.packetlib.packet.Packet;

public class PEInteractPacketTranslator implements PEPacketTranslator<InteractPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, InteractPacket packet) {
        ClientPlayerInteractEntityPacket pk = new ClientPlayerInteractEntityPacket((int) (packet.target & 0xFFFFFFFF), InteractAction.ATTACK);
        return new Packet[]{pk};
    }

}
