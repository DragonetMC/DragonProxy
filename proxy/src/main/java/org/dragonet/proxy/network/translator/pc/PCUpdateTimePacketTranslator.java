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

import org.dragonet.proxy.protocol.packet.SetTimePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;

public class PCUpdateTimePacketTranslator implements PCPacketTranslator<ServerUpdateTimePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerUpdateTimePacket packet) {
        SetTimePacket pk = new SetTimePacket((int) (packet.getTime()), true);
        return new PEPacket[]{pk};
    }

}
