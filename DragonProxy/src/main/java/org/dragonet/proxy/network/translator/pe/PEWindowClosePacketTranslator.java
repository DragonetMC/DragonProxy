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
package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.InventoryTranslatorRegister;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.packetlib.packet.Packet;

import sul.protocol.pocket101.play.ContainerClose;

public class PEWindowClosePacketTranslator implements PEPacketTranslator<ContainerClose> {

    @Override
    public Packet[] translate(ClientConnection session, ContainerClose packet) {
        DragonProxy.getSelf().getGeneralThreadPool().execute(() -> {
            InventoryTranslatorRegister.closeOpened(session, false);
        });
        
        return new Packet[0];
    }

}
