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

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.SetTimePacket;

public class PCUpdateTimePacketTranslator implements PCPacketTranslator<ServerUpdateTimePacket> {

    @Override
    public DataPacket[] translate(ClientConnection session, ServerUpdateTimePacket packet) {
        SetTimePacket pk = new SetTimePacket();
        pk.time = (int) packet.getTime();
        pk.started = true;
        return new DataPacket[]{pk};
    }

}
