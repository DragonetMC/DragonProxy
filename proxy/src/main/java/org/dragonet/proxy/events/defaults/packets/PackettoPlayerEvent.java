package org.dragonet.proxy.events.defaults.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.proxy.events.Cancellable;
import org.dragonet.proxy.events.HandlerList;
import org.dragonet.proxy.network.UpstreamSession;

public class PackettoPlayerEvent extends PEPacketEvent implements Cancellable{

    private static final HandlerList handlerList = new HandlerList();
    
    @Override
    public HandlerList getHandlers​(){
        return handlerList;
    }
    
    private final UpstreamSession session;
    
    public PackettoPlayerEvent(UpstreamSession session, PEPacket packet) {
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
