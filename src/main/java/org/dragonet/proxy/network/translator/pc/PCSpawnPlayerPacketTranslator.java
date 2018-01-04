package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.dragonet.proxy.protocol.PEPacket;

public class PCSpawnPlayerPacketTranslator implements IPCPacketTranslator<ServerSpawnPlayerPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerSpawnPlayerPacket packet) {
        try {
            session.getEntityCache().newPlayer(packet).spawn(session);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
