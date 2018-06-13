/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network;

import co.aikar.timings.Timings;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.sessions.ISessionRegister;
import org.dragonet.api.sessions.IUpstreamSession;

public class SessionRegister implements ISessionRegister {

    private final ProxyServer proxy;
    private final Map<String, IUpstreamSession> clients = new ConcurrentHashMap();

    public SessionRegister(ProxyServer proxy) {
        this.proxy = proxy;
    }

    public void onTick() {
        Timings.connectionTimer.startTiming();
        clients.values().parallelStream().forEach((session) -> {session.onTick();});
        Timings.connectionTimer.stopTiming();
    }

    public void newSession(IUpstreamSession session) {
        clients.put(session.getRaknetID(), session);
    }

    public void removeSession(UpstreamSession session) {
        clients.remove(session.getRaknetID());
    }

    public IUpstreamSession getSession(String identifier) {
        return clients.get(identifier);
    }

    public Map<String, IUpstreamSession> getAll() {
        return Collections.unmodifiableMap(clients);
    }

    public int getOnlineCount() {
        return clients.size();
    }
}
