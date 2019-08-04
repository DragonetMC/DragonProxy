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
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.cache.WindowCache;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.InventoryTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCSetSlotTranslator implements PacketTranslator<ServerSetSlotPacket> {
    public static final PCSetSlotTranslator INSTANCE = new PCSetSlotTranslator();

    @Override
    public void translate(ProxySession session, ServerSetSlotPacket packet) {
        WindowCache windowCache = session.getWindowCache();
        if(!windowCache.getWindows().containsKey(packet.getWindowId())) {
            log.warn("SetSlotTranslator: Window not in cache, id: " + packet.getWindowId());
            return;
        }
        CachedWindow window = windowCache.getWindows().get(packet.getWindowId());
        if(packet.getWindowId() != 0 && window.getWindowType() == null) {
            return;
        }

        if(packet.getWindowId() == 0) {
            if(packet.getSlot() >= window.getItems().length) {
                return;
            }
            ItemStack[] items = window.getItems();
            items[packet.getSlot()] = packet.getItem();
            window.setItems(items);

            InventoryTranslator.sendPlayerInventory(session);
        }

        if(window.isOpen()) {
            // update slot
        } else {
            // cache packet
        }
    }
}
