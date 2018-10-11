package org.dragonet.dragonproxy.api;

import java.nio.file.Path;

public interface Proxy {

    String getVersion();
    Path getFolder();
    void shutdown();
}
