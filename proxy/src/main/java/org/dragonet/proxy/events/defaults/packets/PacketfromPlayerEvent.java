package org.dragonet.proxy.events.defaults.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.api.events.HandlerList;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.api.events.Cancellable;

public class PacketfromPlayerEvent extends PEPacketEvent implements Cancellable{

    private static final HandlerList handlerList = new HandlerList();
    
    @Override
    public HandlerList getHandlers​(){
        return handlerList;
    }
    
    private final UpstreamSession session;
    
    public PacketfromPlayerEvent(UpstreamSession session, PEPacket packet) {
        super(packet);
        this.session = session;
    }
    
    public UpstreamSession getSession() {
        return session;
    }
    
    private boolean cancelled = false;
    
    public void setCancelled​(boolean cancel){
        cancelled = cancel;
    }
    
    public boolean isCancelled​(){
        return cancelled;
    }

}