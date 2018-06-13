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
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.dragonet.api.caches.ICachedWindow;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;

public class PCSetSlotPacketTranslator implements IPCPacketTranslator<ServerSetSlotPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerSetSlotPacket packet) {
        if (!session.getWindowCache().hasWindow(packet.getWindowId())) {
            // Cache this
            session.getWindowCache().newCachedPacket(packet.getWindowId(), packet);
            return null;
        }
        ICachedWindow win = session.getWindowCache().get(packet.getWindowId());
        if (win.getPcType() == null && packet.getWindowId() != 0)
            return null;
        if (packet.getWindowId() == 0) {
            if (packet.getSlot() >= win.getSlots().length)
                return null;
            win.getSlots()[packet.getSlot()] = packet.getItem();
            return InventoryTranslatorRegister.sendPlayerInventory(session); // Too lazy lol
        }
//        if (packet.getItem() != null)
//            System.out.println("Caching window " + packet.getWindowId() + " item " + packet.getItem().getId());
        if (win.isIsOpen())
            InventoryTranslatorRegister.updateSlot(session, packet);
        else
            session.getWindowCache().newCachedPacket(packet.getWindowId(), packet);
        return null;
    }
}
