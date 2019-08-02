package org.dragonet.proxy.network.session.data;

public enum AuthState {
    NONE,
    NOT_AUTHENTICATED, // Offline mode
    AUTHENTICATING, // Online mode, before logging in
    AUTHENTICATED // Online mode, logged in
}
