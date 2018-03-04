package org.dragonet.proxy.events;

public abstract class Event {

    private final boolean async;
    
    public Event(){
        this(false);
    }
    
    public Event(boolean async){
        this.async = async;
    }
    
    public boolean isAsynchronous​(){
        return async;
    }
    
    public abstract HandlerList getHandlers​();
    
}
