package org.dragonet.api;

import java.net.InetSocketAddress;

/**
 * Represents a connection between the client and the proxy.
 */
public interface FrontendConnection {

    /**
     * Returns the frontend socket address.
     *
     * @return the frontend socket address.
     */
    InetSocketAddress getFrontendAddress();

    /**
     * Returns the frontend connection ping.
     *
     * @return the frontend connection ping.
     */
    int getPing();

}
