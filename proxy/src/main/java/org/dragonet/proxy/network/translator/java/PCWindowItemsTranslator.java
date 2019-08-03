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

import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
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
public class PCWindowItemsTranslator implements PacketTranslator<ServerWindowItemsPacket> {
    public static final PCWindowItemsTranslator INSTANCE = new PCWindowItemsTranslator();

    @Override
    public void translate(ProxySession session, ServerWindowItemsPacket packet) {
        WindowCache windowCache = session.getWindowCache();
        if(!windowCache.getWindows().containsKey(packet.getWindowId())) {
            log.warn("WindowItemsTranslator: Window not in cache, id: " + packet.getWindowId());
            return;
        }
        CachedWindow window = windowCache.getWindows().get(packet.getWindowId());
        if(packet.getWindowId() != 0 && window.getWindowType() == null) {
            return;
        }

        if(packet.getWindowId() == 0) {
            if(packet.getItems().length < 40) {
                return;
            }
            window.setItems(packet.getItems());
            InventoryTranslator.sendPlayerInventory(session);
        }
    }
}
