/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
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
