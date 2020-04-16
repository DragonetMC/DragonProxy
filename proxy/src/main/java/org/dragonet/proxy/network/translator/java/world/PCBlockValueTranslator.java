package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@PCPacketTranslator(packetClass = ServerBlockValuePacket.class)
public class PCBlockValueTranslator extends PacketTranslator<ServerBlockValuePacket> {

    @Override
    public void translate(ProxySession session, ServerBlockValuePacket packet) {

    }
}
