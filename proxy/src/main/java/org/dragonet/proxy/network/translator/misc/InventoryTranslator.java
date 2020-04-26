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
package org.dragonet.proxy.network.translator.misc;

import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.nukkitx.protocol.bedrock.data.ContainerId;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.inventory.GenericInventoryTranslator;

import java.util.HashMap;
import java.util.Map;

public class InventoryTranslator {
    public static final Map<WindowType, GenericInventoryTranslator> TRANSLATORS = new HashMap<>();

    static {
        TRANSLATORS.put(WindowType.GENERIC_9X1, new GenericInventoryTranslator());
        TRANSLATORS.put(WindowType.GENERIC_9X2, new GenericInventoryTranslator());
        TRANSLATORS.put(WindowType.GENERIC_9X3, new GenericInventoryTranslator());
        TRANSLATORS.put(WindowType.GENERIC_9X4, new GenericInventoryTranslator());
        TRANSLATORS.put(WindowType.GENERIC_9X5, new GenericInventoryTranslator());
        TRANSLATORS.put(WindowType.GENERIC_9X6, new GenericInventoryTranslator());
    }

    /**
     * Sends the items from the players cached inventory to the client
     */
    public static void sendPlayerInventory(ProxySession session) {
        CachedWindow cachedWindow = session.getWindowCache().getPlayerInventory();

        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(ContainerId.INVENTORY);

        ItemData[] contents = new ItemData[40];

        // Hotbar
        for(int i = 36; i < 45; i++) {
            contents[i - 36] = ItemTranslatorRegistry.translateToBedrock(cachedWindow.getItems()[i]);
        }

        // Inventory
        for(int i = 9; i < 36; i++) {
            contents[i] = ItemTranslatorRegistry.translateToBedrock(cachedWindow.getItems()[i]);
        }

        // Armour
        for(int i = 5; i < 9; i++) {
            contents[i + 31] = ItemTranslatorRegistry.translateToBedrock(cachedWindow.getItems()[i]);
        }

        inventoryContentPacket.setContents(contents);
        // TODO: fixes issues on cubecraft and hypixel with crashing clients. Need to investate.
        session.sendPacket(inventoryContentPacket);
    }

    public static void updateSlot(ProxySession session, ServerSetSlotPacket packet) {

    }
}
