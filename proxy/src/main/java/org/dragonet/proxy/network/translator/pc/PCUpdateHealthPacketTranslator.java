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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.pocket113.play.Respawn;
import sul.protocol.pocket113.play.SetHealth;
import sul.utils.Packet;

public class PCUpdateHealthPacketTranslator implements PCPacketTranslator<ServerPlayerHealthPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerPlayerHealthPacket packet) {
        SetHealth h = new SetHealth((int) packet.getHealth());
        if (packet.getHealth() > 0 && h.health <= 0) {
            h.health = 1;
        }
        if (packet.getHealth() <= 0.0f) {
            Respawn r = new Respawn();
            return new Packet[]{h, r};
        }
        return new Packet[]{h};
    }

}
