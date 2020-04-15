/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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
package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.nukkitx.protocol.bedrock.packet.SetTimePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@Log4j2
@PCPacketTranslator(packetClass = ServerUpdateTimePacket.class)
public class PCUpdateTimeTranslator extends PacketTranslator<ServerUpdateTimePacket> {

    @Override
    public void translate(ProxySession session, ServerUpdateTimePacket packet) {
        WorldCache worldCache = session.getWorldCache();
        long time = Math.abs(packet.getTime());

        if(packet.getTime() < 0) {
            // Skip it the first time the time is sent to get the client at the same time as the server
            if(session.isFirstTimePacket()) {
                session.setFirstTimePacket(false);
                sendTime(session, time);
                return;
            }

            // Now we can check if we have already send the game rule
            if(!worldCache.isTimeStopped()) {
                worldCache.setTimeStopped(session, true);
            }
            return;
        }
        else if(packet.getTime() >= 0 && worldCache.isTimeStopped()) {
            worldCache.setTimeStopped(session, false);
            return;
        }

        sendTime(session, time);
    }

    private void sendTime(ProxySession session, long time) {
        SetTimePacket setTime = new SetTimePacket();
        setTime.setTime((int) time);
        session.sendPacket(setTime);
    }
}
