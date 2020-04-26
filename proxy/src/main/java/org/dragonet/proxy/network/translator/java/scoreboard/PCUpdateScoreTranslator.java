package org.dragonet.proxy.network.translator.java.scoreboard;

import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.scoreboard.Objective;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@Log4j2
@PacketRegisterInfo(packet = ServerUpdateScorePacket.class)
public class PCUpdateScoreTranslator extends PacketTranslator<ServerUpdateScorePacket> {

    @Override
    public void translate(ProxySession session, ServerUpdateScorePacket packet) {
        Scoreboard scoreboard = session.getWorldCache().getScoreboard();

        Objective objective = scoreboard.getObjective(packet.getObjective());
        if(objective == null) {
            log.warn("Failed to get objective named: " + packet.getObjective() + " when updating score");
            return;
        }

        switch(packet.getAction()) {
            case ADD_OR_UPDATE:

                break;
            case REMOVE:

                break;
        }
    }
}
