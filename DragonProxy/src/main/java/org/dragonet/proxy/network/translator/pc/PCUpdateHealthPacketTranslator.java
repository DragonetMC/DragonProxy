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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.Respawn;
import sul.protocol.pocket101.play.SetHealth;
import sul.utils.Tuples;

public class PCUpdateHealthPacketTranslator implements PCPacketTranslator<ServerPlayerHealthPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerPlayerHealthPacket packet) {
    	// TODO: Support food and saturation
        SetHealth h = new SetHealth();
        h.health = (int) packet.getHealth();
        if (packet.getHealth() > 0 && h.health <= 0) {
            h.health = 1;
        }
        
        if (packet.getHealth() <= 0.0f) {
            Respawn r = new Respawn();
            r.position = new Tuples.FloatXYZ(0.0f, 0.0f, 0.0f);
            
            return fromSulPackets(h, r);
        }
        
        return fromSulPackets(h);
    }

}
