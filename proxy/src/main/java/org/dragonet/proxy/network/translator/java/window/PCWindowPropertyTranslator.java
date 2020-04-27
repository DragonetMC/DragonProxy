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
package org.dragonet.proxy.network.translator.java.window;

import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WindowCache;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;


@Log4j2
@PacketRegisterInfo(packet = ServerWindowPropertyPacket.class)
public class PCWindowPropertyTranslator extends PacketTranslator<ServerWindowPropertyPacket> {

    @Override
    public void translate(ProxySession session, ServerWindowPropertyPacket packet) {
        WindowCache windowCache = session.getWindowCache();
        if(!windowCache.getWindows().containsKey(packet.getWindowId())) {
            //log.info("(debug) WindowPropertyTranslator: Window not in cache, id: " + packet.getWindowId());
            return;
        }

        CachedWindow window = windowCache.getWindows().get(packet.getWindowId());

        switch(window.getWindowType()) {
            case FURNACE:

                break;
        }
    }
}
