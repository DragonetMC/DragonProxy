/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.InventoryTranslatorRegister;
import org.dragonet.api.translators.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.dragonet.api.caches.ICachedWindow;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;

public class PCWindowItemsTranslator implements IPCPacketTranslator<ServerWindowItemsPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerWindowItemsPacket packet) {
        if (!session.getWindowCache().hasWindow(packet.getWindowId())) {
            // Cache this
            session.getWindowCache().newCachedPacket(packet.getWindowId(), packet);
            return null;
        }
        ICachedWindow win = session.getWindowCache().get(packet.getWindowId());
        if (packet.getWindowId() == 0) {
            if (packet.getItems().length < 40)
                // Almost impossible to happen either.
                return null;
            // Update items in window cache
            win.setSlots(packet.getItems());
            return InventoryTranslatorRegister.sendPlayerInventory(session);
        }
        InventoryTranslatorRegister.updateContent(session, packet);
        return null;
    }

}
