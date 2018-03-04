package org.dragonet.proxy.events.defaults.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.proxy.events.Event;

public abstract class PEPacketEvent extends Event {

    private PEPacket packet;

    public PEPacketEvent(PEPacket packet) {
        this.packet = packet;
    }

    public PEPacket getPacket() {
        return packet;
    }

    public void setPacket(PEPacket packet) {
        this.packet = packet;
    }

}
