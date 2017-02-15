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
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

import org.dragonet.proxy.DragonProxy;

public class SessionRegister {

    private final DragonProxy proxy;

    private final Map<UUID, ClientConnection> clients = Collections.synchronizedMap(new HashMap<UUID, ClientConnection>());
        
    public SessionRegister(DragonProxy proxy) {
        this.proxy = proxy;
    }

    public void onTick() {
        Iterator<Map.Entry<UUID, ClientConnection>> iterator = clients.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, ClientConnection> ent = iterator.next();
            ent.getValue().onTick();
        }
    }

    public void removeSession(ClientConnection session) {
        clients.remove(session.getSessionID());
    }

    public ClientConnection getSession(UUID identifier) {
        return clients.get(identifier);
    }

    public Map<UUID, ClientConnection> getAll() {
        return Collections.unmodifiableMap(clients);
    }

    public int getOnlineCount() {
        return clients.size();
    }
    
    public boolean acceptConnection(ClientConnection session){
        if(clients.size() < proxy.getConfig().getMax_players()){
            clients.put(session.getSessionID(), session);
            return true;
        }
        return false;
    }
}
