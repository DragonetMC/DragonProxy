/**
 * JRakLib is not affiliated with Jenkins Software LLC or RakNet.
 * This software is a port of RakLib https://github.com/PocketMine/RakLib.

 * This file is part of JRakLib.
 *
 * JRakLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JRakLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JRakLib.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.raknet.client;

import org.dragonet.raknet.protocol.EncapsulatedPacket;

/**
 * An interface for communication with the client implementation
 * .
 * @author jython234
 */
public interface ClientInstance {

    /**
     * Called when the connection is opened to the RakNet server.
     * @param serverId The serverId of the RakNet server.
     */
    void connectionOpened(long serverId);

    /**
     * Called when the connection is closed.
     * @param reason The reason for the closure.
     */
    void connectionClosed(String reason);

    /**
     * Called when an <code>EncapsulatedPacket</code> is received from the RakNet server.
     * @param packet The packet received.
     * @param flags Flags from the packet.
     */
    void handleEncapsulated(EncapsulatedPacket packet, int flags);

    /**
     * Called when an unrecognized packet is received from the server.
     * @param payload Raw payload (in bytes) of the strange packet.
     */
    void handleRaw(byte[] payload);

    /**
     * Called when an internal option is updated. Current only supports "bandwith"
     * @param option The option name.
     * @param value The option value.
     */
    void handleOption(String option, String value);
}
