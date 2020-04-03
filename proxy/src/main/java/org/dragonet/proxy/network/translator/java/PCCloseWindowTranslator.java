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
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@PCPacketTranslator(packetClass = ServerCloseWindowPacket.class)
public class PCCloseWindowTranslator extends PacketTranslator<ServerCloseWindowPacket> {

    @Override
    public void translate(ProxySession session, ServerCloseWindowPacket packet) {
        CachedWindow cachedWindow = session.getWindowCache().getById(packet.getWindowId());
        if(cachedWindow == null) {
            log.info(TextFormat.GRAY + "(debug) PCCloseWindowTranslator: cached window is null");
            return;
        }

        cachedWindow.close(session);
    }
}
