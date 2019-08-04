/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.remote;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

public class RemoteServerManager {
    private Map<String, RemoteServer> remoteServers = new HashMap<>();

    public void addServer(RemoteServer server) {
        Preconditions.checkNotNull(server, "Remote server cannot be null");
        remoteServers.put(server.getName(), server);
    }

    public RemoteServer getServer(String name) {
        return remoteServers.get(name);
    }

    public Map<String, RemoteServer> getServers() {
        return remoteServers;
    }
}
