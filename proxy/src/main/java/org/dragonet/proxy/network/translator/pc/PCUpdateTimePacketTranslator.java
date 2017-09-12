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
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import sul.protocol.pocket113.play.SetTime;
import sul.utils.Packet;

public class PCUpdateTimePacketTranslator implements PCPacketTranslator<ServerUpdateTimePacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerUpdateTimePacket packet) {
        SetTime pk = new SetTime();
        pk.time = (int) (packet.getTime());
        return new Packet[]{pk};
    }

}
