package org.dragonet.dragonproxy.api;

import org.dragonet.dragonproxy.api.command.CommandSource;

import java.nio.file.Path;

public interface Proxy {

    String getVersion();
    Path getFolder();
    void shutdown();
    CommandSource getConsole();
}
