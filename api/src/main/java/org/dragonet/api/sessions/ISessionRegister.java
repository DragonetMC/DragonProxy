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

import java.util.Map;

/**
 * Represents the session register.
 */
public interface ISessionRegister {

    /**
     * The session register tick handler.
     */
    void onTick();

    /**
     * Registers a new upstream session.
     *
     * @param session the upstream session.
     */
    void newSession(IUpstreamSession session);

    /**
     * Removes a registered upstream session.
     *
     * @param session the upstream session.
     */
    void removeSession(IUpstreamSession session);

    /**
     * Returns registered new upstream session.
     *
     * @param identifier the session identifier string.
     */
    IUpstreamSession getSession(String identifier);

    /**
     * Returns a copy of the registered sessions.
     * TODO: actually check if implementation returns a copy
     *
     * @return a copy of the registered session map.
     */
    Map<String, IUpstreamSession> getAll();

    /**
     * Returns the count of registered sessions.
     *
     * @return the connected session count.
     */
    int getOnlineCount();
}
