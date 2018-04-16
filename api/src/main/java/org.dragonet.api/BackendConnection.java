package org.dragonet.api;

import java.net.InetSocketAddress;

/**
 * Represents a connection between the proxy and the destination server.
 */
public interface BackendConnection {

    /**
     * Returns the backend socket address.
     *
     * @return the backend socket address.
     */
    InetSocketAddress getBackendAddress();

    /**
     * Returns the backend connection ping.
     *
     * @return the backend connection ping.
     */
    int getPing();

}
