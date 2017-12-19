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
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.RespawnPacket;
import org.dragonet.proxy.protocol.packets.SetHealthPacket;

public class PCUpdateHealthPacketTranslator implements IPCPacketTranslator<ServerPlayerHealthPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerPlayerHealthPacket packet) {
        SetHealthPacket h = new SetHealthPacket((int) packet.getHealth());
        if (packet.getHealth() > 0 && h.health <= 0) {
            h.health = 1;
        }
        if (packet.getHealth() <= 0.0f) {
            RespawnPacket r = new RespawnPacket();
            return new PEPacket[]{h, r};
        }
        return new PEPacket[]{h};
    }
}
