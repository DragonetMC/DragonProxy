package org.dragonet.proxy.events.defaults.player;

import org.dragonet.api.events.HandlerList;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.api.events.Cancellable;

public class PlayerAuthenticationEvent extends PlayerEvent implements Cancellable{

    private static final HandlerList handlerList = new HandlerList();
    
    @Override
    public HandlerList getHandlers​(){
        return handlerList;
    }
    
    public PlayerAuthenticationEvent(UpstreamSession session) {
        super(session);
    }
    
    private boolean cancelled = false;
    
    public void setCancelled​(boolean cancel){
        cancelled = cancel;
    }
    
    public boolean isCancelled​(){
        return cancelled;
    }
}