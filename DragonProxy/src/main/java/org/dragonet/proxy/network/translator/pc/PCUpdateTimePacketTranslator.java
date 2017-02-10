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
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.SetTime;

public class PCUpdateTimePacketTranslator implements PCPacketTranslator<ServerUpdateTimePacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerUpdateTimePacket packet) {
        SetTime pk = new SetTime();
        pk.time = (int) packet.getTime();
        pk.daylightCycle = true;
        
        return fromSulPackets(pk);
    }

}
