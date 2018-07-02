/*
 * DragonProxy API
 * Copyright © 2016 Dragonet Foundation (https://github.com/DragonetMC/DragonProxy)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.api.translators;

import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;

/**
 * Represents a Java to Bedrock edition packet translator.
 *
 * @param <P> the original Java edition packet class.
 */
public interface IPCPacketTranslator<P extends Packet> {

    /**
     * Translate a packet from Java to Bedrock edition.
     *
     * @param session the upstream session.
     * @param packet the packet.
     * @return the resulting translated packet array.
     */
    PEPacket[] translate(IUpstreamSession session, P packet);
}
