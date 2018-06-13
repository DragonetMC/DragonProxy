package org.dragonet.proxy.events.defaults.player;

import org.dragonet.api.events.HandlerList;
import org.dragonet.proxy.network.UpstreamSession;

public class PlayerQuitEvent extends PlayerEvent{

    private static final HandlerList handlerList = new HandlerList();
    
    @Override
    public HandlerList getHandlers​(){
        return handlerList;
    }
    
    public PlayerQuitEvent(UpstreamSession session) {
        super(session);
    }

}
