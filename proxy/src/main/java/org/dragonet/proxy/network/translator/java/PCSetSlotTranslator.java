/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
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
