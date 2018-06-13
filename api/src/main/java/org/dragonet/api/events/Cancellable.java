package org.dragonet.api.events;

public interface Cancellable {

    public void setCancelled​(boolean cancel);

    public boolean isCancelled​();

}
