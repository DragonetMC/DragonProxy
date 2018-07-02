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
package org.dragonet.api.sessions;

import org.dragonet.api.network.PEPacket;

/**
 * Represents a Bedrock edition packet processor.
 */
public interface IPEPacketProcessor {

    /**
     * Returns the current Bedrock edition session.
     *
     * @return the Bedrock edition session.
     */
    IUpstreamSession getClient();

    /**
     * Put a raw packet data into the buffer.
     *
     * @param packet the packet byte array
     */
    void putPacket(byte[] packet);

    /**
     * The processor tick handler.
     */
    void onTick();

    /**
     * Handles a Bedrock edition packet.
     * TODO: this method should be in UpstreamSession
     *
     * @param packet the Bedrock edition packet.
     */
    void handlePacket(PEPacket packet);

    /**
     * Enables/disables the packet forward mode.
     *
     * @param enabled if set to true the forward mode will be activated.
     */
    void setPacketForwardMode(boolean enabled);
}
