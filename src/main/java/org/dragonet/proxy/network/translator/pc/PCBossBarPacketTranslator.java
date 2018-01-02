package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;

public class PCBossBarPacketTranslator implements IPCPacketTranslator<ServerBossBarPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerBossBarPacket packet) {

        return new PEPacket[]{};
    }

}
