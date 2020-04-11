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

import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.misc.InventoryTranslator;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.network.translator.misc.inventory.IInventoryTranslator;
import org.dragonet.proxy.network.translator.misc.inventory.SingleChestInventoryTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@PCPacketTranslator(packetClass = ServerOpenWindowPacket.class)
public class PCOpenWindowTranslator extends PacketTranslator<ServerOpenWindowPacket> {
    private static final Map<WindowType, IInventoryTranslator> windowMap = new HashMap<>();

    static {
        windowMap.put(WindowType.GENERIC_9X1, new SingleChestInventoryTranslator(9));
        windowMap.put(WindowType.GENERIC_9X2, new SingleChestInventoryTranslator(18));
        windowMap.put(WindowType.GENERIC_9X3, new SingleChestInventoryTranslator(27));
    }

    @Override
    public void translate(ProxySession session, ServerOpenWindowPacket packet) {
        IInventoryTranslator bedrockWindowTranslator = windowMap.get(packet.getType());
        if(bedrockWindowTranslator == null) {
            log.info(TextFormat.GRAY + "(debug) Unhandled window type: " + packet.getType().name() + ". It is not supported yet.");

            // Close the window
            session.sendRemotePacket(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }

        CachedWindow cachedWindow = session.getWindowCache().newWindow(bedrockWindowTranslator, packet.getWindowId());
        cachedWindow.setName(MessageTranslator.translate(packet.getName()));
        cachedWindow.open(session);

        //log.warn("OPENING WINDOW id: " + packet.getWindowId() + "  -  " + packet.getName());
    }
}
