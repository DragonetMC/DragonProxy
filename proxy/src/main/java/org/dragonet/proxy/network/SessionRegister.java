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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dragonet.proxy.DragonProxy;

public class SessionRegister {

    private final DragonProxy proxy;

    private final Map<String, UpstreamSession> clients = Collections.synchronizedMap(new HashMap<String, UpstreamSession>());

    public SessionRegister(DragonProxy proxy) {
        this.proxy = proxy;
    }

    public void onTick() {
        Iterator<Map.Entry<String, UpstreamSession>> iterator = clients.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, UpstreamSession> ent = iterator.next();
            ent.getValue().onTick();
        }
    }

    public void newSession(UpstreamSession session) {
        clients.put(session.getRaknetID(), session);
    }

    public void removeSession(UpstreamSession session) {
        clients.remove(session.getRaknetID());
    }

    public UpstreamSession getSession(String identifier) {
        return clients.get(identifier);
    }

    public Map<String, UpstreamSession> getAll() {
        return Collections.unmodifiableMap(clients);
    }

    public int getOnlineCount() {
        return clients.size();
    }
}
