package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.common.mcbedrock.protocol.PEPacket;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;

public class PCDisconnectPacketTranslator implements IPCPacketTranslator<ServerDisconnectPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerDisconnectPacket packet) {
        session.disconnect(packet.getReason().getFullText());
        return null;
    }

}
