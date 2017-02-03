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
package org.dragonet.proxy.network.translator;

import net.marfgamer.jraknet.RakNetPacket;
import org.dragonet.proxy.network.ClientConnection;
import org.spacehq.packetlib.packet.Packet;

public interface PEPacketTranslator<P extends sul.utils.Packet> {

    /**
     * Translate a packet from PE version to PC version.
     *
     * @param session
     * @param packet
     * @return
     */
    public Packet[] translate(ClientConnection session, P packet);

}
