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
package org.dragonet.proxy.util;

import java.util.LinkedHashMap;

public final class RemoteServer {
    private static final LinkedHashMap<String, RemoteServer> servers = new LinkedHashMap<>();

    private final String name;
    private final int port;

    public RemoteServer getServer(String name) {
        return servers.get(name);
    }

    public RemoteServer(String name, int port) {
        this.name = name;
        this.port = port;
        if(servers.containsKey(name)) {
            throw new RuntimeException();
        }
        servers.put(name, this);
    }
}
