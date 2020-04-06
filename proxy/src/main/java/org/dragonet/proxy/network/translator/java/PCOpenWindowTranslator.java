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
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@PCPacketTranslator(packetClass = ServerOpenWindowPacket.class)
public class PCOpenWindowTranslator extends PacketTranslator<ServerOpenWindowPacket> {
    private static final Map<WindowType, BedrockWindowType> windowMap = new HashMap<>();

    static {
        windowMap.put(WindowType.ANVIL, BedrockWindowType.ANVIL);
        windowMap.put(WindowType.BEACON, BedrockWindowType.BEACON);
        windowMap.put(WindowType.ENCHANTMENT, BedrockWindowType.ENCHANT_TABLE);
        windowMap.put(WindowType.BREWING_STAND, BedrockWindowType.BREWING_STAND);
        windowMap.put(WindowType.FURNACE, BedrockWindowType.FURNACE);
        windowMap.put(WindowType.HOPPER, BedrockWindowType.HOPPER);
        windowMap.put(WindowType.CRAFTING, BedrockWindowType.CRAFTING_TABLE);
        //windowMap.put(WindowType.MERCHANT, BedrockWindowType.TRADING);
        windowMap.put(WindowType.BLAST_FURNACE, BedrockWindowType.BLAST_FURNACE);
        windowMap.put(WindowType.SMOKER, BedrockWindowType.SMOKER);
        windowMap.put(WindowType.STONECUTTER, BedrockWindowType.STONECUTTER);
        // TODO: chest style inventories
    }

    @Override
    public void translate(ProxySession session, ServerOpenWindowPacket packet) {
        BedrockWindowType bedrockWindowType = windowMap.get(packet.getType());
        if(bedrockWindowType == null) {
            log.info(TextFormat.GRAY + "(debug) Unhandled window type: " + packet.getType().name() + TextFormat.AQUA + " It is not supported yet.");
            return;
        }

        //log.warn("WINDOW: " + packet.getWindowId() + " - " + packet.getType().name());

        CachedWindow cachedWindow = session.getWindowCache().newWindow(bedrockWindowType, packet.getWindowId());
        cachedWindow.setName(MessageTranslator.translate(packet.getName()));
        cachedWindow.open(session);

        //log.warn("OPENING WINDOW id: " + packet.getWindowId() + "  -  " + packet.getName());
    }
}
