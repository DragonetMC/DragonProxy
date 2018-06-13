package org.dragonet.proxy.network.translator.pc;

import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import org.dragonet.api.sessions.IUpstreamSession;

public class PCBossBarPacketTranslator implements IPCPacketTranslator<ServerBossBarPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerBossBarPacket packet) {

        return new PEPacket[]{};
    }

}
