package org.dragonet.proxy.network.translator.pc;

import org.dragonet.api.translators.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.proxy.network.cache.EntityCache;

public class PCSpawnPlayerPacketTranslator implements IPCPacketTranslator<ServerSpawnPlayerPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerSpawnPlayerPacket packet) {
        try {
            ((EntityCache)session.getEntityCache()).newPlayer(packet).spawn(session);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
