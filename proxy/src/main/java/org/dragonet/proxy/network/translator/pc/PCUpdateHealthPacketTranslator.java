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
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerUpdateHealthPacket;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.SetHealthPacket;

public class PCUpdateHealthPacketTranslator implements PCPacketTranslator<ServerUpdateHealthPacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerUpdateHealthPacket packet) {
    	// TODO: Support food and saturation
        SetHealthPacket h = new SetHealthPacket();
        h.health = (int) packet.getHealth();
        if (packet.getHealth() > 0 && h.health <= 0) {
            h.health = 1;
        }
        if (packet.getHealth() <= 0.0f) {
            RespawnPacket r = new RespawnPacket();
            r.x = 0.0f;
            r.y = 0.0f;
            r.z = 0.0f;
            return new DataPacket[]{h, r};
        }
        return new DataPacket[]{h};
    }

}
