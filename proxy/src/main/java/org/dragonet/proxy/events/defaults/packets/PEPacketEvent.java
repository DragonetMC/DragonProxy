package org.dragonet.proxy.events.defaults.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.api.events.Event;

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
