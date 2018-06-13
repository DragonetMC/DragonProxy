package org.dragonet.proxy.network.translator.pc;

import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.dragonet.api.sessions.IUpstreamSession;

public class PCDisconnectPacketTranslator implements IPCPacketTranslator<ServerDisconnectPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerDisconnectPacket packet) {
        session.disconnect(packet.getReason().getFullText());
        return null;
    }

}
