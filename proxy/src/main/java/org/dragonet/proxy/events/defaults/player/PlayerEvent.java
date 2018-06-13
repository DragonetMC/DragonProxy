package org.dragonet.proxy.events.defaults.player;

import org.dragonet.api.events.Event;
import org.dragonet.proxy.network.UpstreamSession;

public abstract class PlayerEvent extends Event {

    private final UpstreamSession session;
    
    public PlayerEvent(UpstreamSession session){
        this.session = session;
    }

    public UpstreamSession getSession() {
        return session;
    }
    
}
