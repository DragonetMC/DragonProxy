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

import org.dragonet.proxy.protocol.packet.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.spacehq.packetlib.packet.Packet;

public interface PEPacketTranslator<P extends PEPacket> {

    /**
     * Translate a packet from PE version to PC version.
     *
     * @param session
     * @param packet
     * @return
     */
    public Packet[] translate(UpstreamSession session, P packet);

}
