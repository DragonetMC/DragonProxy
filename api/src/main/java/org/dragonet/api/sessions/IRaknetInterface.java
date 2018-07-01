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
