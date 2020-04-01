/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@Log4j2
@PCPacketTranslator(packetClass = ServerKeepAlivePacket.class)
public class PCKeepAliveTranslator extends PacketTranslator<ServerKeepAlivePacket> {

    @Override
    public void translate(ProxySession session, ServerKeepAlivePacket packet) {
        // This packet will cause the remote server (in my testing only vanilla servers)
        // to disconnect the client with a 'Timed out' message.
        // This is completely the opposite to what is described on wiki.vg:
        // https://wiki.vg/Protocol#Keep_Alive_.28clientbound.29

        //session.sendRemotePacket(new ClientKeepAlivePacket(packet.getPingId()));
    }
}


