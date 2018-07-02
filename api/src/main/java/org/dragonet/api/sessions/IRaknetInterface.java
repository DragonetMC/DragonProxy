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

import org.dragonet.api.ProxyServer;

/**
 * Represents the Raknet server interface.
 */
public interface IRaknetInterface {

    /**
     * Returns the proxy instance.
     *
     * @return the proxy instance.
     */
    ProxyServer getProxy();

    /**
     * Returns the current server name.
     *
     * @return the current server name.
     */
    String getServerName();

    /**
     * Returns the maximum player slot count.
     *
     * @return the maximum player slot count.
     */
    int getMaxPlayers();

    /**
     * Sets the server broadcast attributes.
     *
     * @param serverName the new server name.
     * @param players the new player count.
     * @param maxPlayers the new maximum player slot count.
     */
    void setBroadcastName(String serverName, int players, int maxPlayers);

    /**
     * Shutdowns the Raknet server instance.
     */
    void shutdown();
}
