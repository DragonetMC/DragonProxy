package org.dragonet.api;

import java.util.Optional;

/**
 * Represent a player connected to the proxy.
 */
public interface Player {

    /**
     * Returns the player's name.
     *
     * @return the player's name.
     */
    String getName();

    /**
     * Returns the Optional frontend connection (Client -> Proxy).
     *
     * @return the Optional frontend connection instance.
     */
    Optional<FrontendConnection> getFrontendConnection();

    /**
     * Returns the Optional backend connection (Proxy -> Server).
     *
     * @return the Optional backend connection instance.
     */
    Optional<BackendConnection> getBackendConnection();

    /**
     * Returns the total connection ping of the player (frontend + backend).
     *
     * @return the ping time in milliseconds.
     */
    default int getPing() {
        int ping = 0;
        if(getBackendConnection().isPresent()) {
            ping += getBackendConnection().get().getPing();
        }
        if(getFrontendConnection().isPresent()) {
            ping += getFrontendConnection().get().getPing();
        }
        return ping;
    }

}
