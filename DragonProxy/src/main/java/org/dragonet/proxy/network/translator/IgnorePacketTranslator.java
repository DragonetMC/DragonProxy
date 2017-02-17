package org.dragonet.proxy.network.translator;

import org.dragonet.proxy.network.ClientConnection;
import org.spacehq.packetlib.packet.Packet;

import net.marfgamer.jraknet.RakNetPacket;
import org.dragonet.proxy.DragonProxy;

public class IgnorePacketTranslator implements PEPacketTranslator, PCPacketTranslator {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, Packet packet) {
        DragonProxy.getLogger().debug("[PC to PE] Ignoring packet: " + packet.getClass().getCanonicalName());
        return new sul.utils.Packet[0];
    }

    @Override
    public Packet[] translate(ClientConnection session, sul.utils.Packet packet) {
        DragonProxy.getLogger().debug("[PE to PC] Ignoring packet: " + packet.getClass().getCanonicalName());
        return new Packet[0];
    }

}
