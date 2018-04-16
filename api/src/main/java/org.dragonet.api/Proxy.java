package org.dragonet.api;

import java.util.Collection;

/**
 * Represents the DragonProxy instance.
 */
public interface Proxy {

    /**
     * Returns all the connected players.
     *
     * @return a Collection of the connected player instances.
     */
    Collection<Player> getPlayers();

    /**
     * Shutdowns the proxy.
     */
    void shutdown();

}
