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
package org.dragonet.api.translators;

import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;

public interface IPCPacketTranslator<P extends Packet> {

    /**
     * Translate a packet from PC version to PE version.
     *
     * @param session
     * @param packet
     * @return
     */
    PEPacket[] translate(IUpstreamSession session, P packet);
}
