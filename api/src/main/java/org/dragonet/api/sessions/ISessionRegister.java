package org.dragonet.api.sessions;

import java.util.Map;

/**
 * Represents the session register.
 */
public interface ISessionRegister {

    /**
     * The session register tick handler.
     */
    void onTick();

    /**
     * Registers a new upstream session.
     *
     * @param session the upstream session.
     */
    void newSession(IUpstreamSession session);

    /**
     * Removes a registered upstream session.
     *
     * @param session the upstream session.
     */
    void removeSession(IUpstreamSession session);

    /**
     * Returns registered new upstream session.
     *
     * @param identifier the session identifier string.
     */
    IUpstreamSession getSession(String identifier);

    /**
     * Returns a copy of the registered sessions.
     * TODO: actually check if implementation returns a copy
     *
     * @return a copy of the registered session map.
     */
    Map<String, IUpstreamSession> getAll();

    /**
     * Returns the count of registered sessions.
     *
     * @return the connected session count.
     */
    int getOnlineCount();
}
