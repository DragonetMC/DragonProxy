package org.dragonet.dragonproxy.proxy.util;

import java.util.LinkedHashMap;

public final class Server {
    private static final LinkedHashMap<String, Server> servers = new LinkedHashMap<>();

    private final String name;
    private final int port;

    public Server getServer(String name) {
        return servers.get(name);
    }

    public Server(String name, int port) {
        this.name = name;
        this.port = port;
        if(servers.containsKey(name)) {
            throw new RuntimeException();
        }
        servers.put(name, this);
    }
}
