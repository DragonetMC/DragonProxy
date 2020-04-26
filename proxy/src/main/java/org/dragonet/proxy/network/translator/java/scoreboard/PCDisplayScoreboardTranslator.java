package org.dragonet.proxy.network.translator.java.scoreboard;

import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.scoreboard.DisplaySlot;
import org.dragonet.proxy.data.scoreboard.Objective;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@Log4j2
@PacketRegisterInfo(packet = ServerDisplayScoreboardPacket.class)
public class PCDisplayScoreboardTranslator extends PacketTranslator<ServerDisplayScoreboardPacket> {

    @Override
    public void translate(ProxySession session, ServerDisplayScoreboardPacket packet) {
        Scoreboard scoreboard = session.getWorldCache().getScoreboard();

        Objective objective = scoreboard.getObjective(packet.getName());
        if(objective == null) {
            log.warn("Failed to get objective named: " + packet.getName() + " when displaying scoreboard");
            return;
        }

        switch(packet.getPosition()) {
            case PLAYER_LIST:
                objective.setDisplaySlot(DisplaySlot.LIST);
                break;
            case BELOW_NAME:
                objective.setDisplaySlot(DisplaySlot.BELOWNAME);
                break;
            default: // This covers all team variations of the sidebar too
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                break;
        }

        // TODO: send to client
    }
}
