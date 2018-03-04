package org.dragonet.proxy.events;

public interface Cancellable {

    public void setCancelled​(boolean cancel);
    public boolean isCancelled​();
    
}
