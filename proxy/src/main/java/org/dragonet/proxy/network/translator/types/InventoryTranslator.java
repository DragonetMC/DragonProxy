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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.types;

import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.nukkitx.protocol.bedrock.data.ContainerId;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.session.ProxySession;

public class InventoryTranslator {

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
            contents[i - 36] = ItemTranslator.translateSlotToBedrock(cachedWindow.getItems()[i]);
        }

        // Inventory
        for(int i = 9; i < 36; i++) {
            contents[i] = ItemTranslator.translateSlotToBedrock(cachedWindow.getItems()[i]);
        }

        // Armour
        for(int i = 5; i < 9; i++) {
            contents[i + 31] = ItemTranslator.translateSlotToBedrock(cachedWindow.getItems()[i]);
        }

        inventoryContentPacket.setContents(contents);
        session.getBedrockSession().sendPacket(inventoryContentPacket);
    }

    public static void updateSlot(ProxySession session, ServerSetSlotPacket packet) {

    }
}
