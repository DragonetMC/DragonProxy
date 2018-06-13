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

import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import org.dragonet.proxy.network.InventoryTranslatorRegister;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;

public class PCClosedWindowPacketTranslator implements IPCPacketTranslator<ServerCloseWindowPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerCloseWindowPacket packet) {
        System.out.println("Window " + packet.getWindowId() + " closed from server !");
//        session.getProxy().getGeneralThreadPool().execute(() -> {
            InventoryTranslatorRegister.closeOpened(session, true);
//        });
        return null;
    }
}
