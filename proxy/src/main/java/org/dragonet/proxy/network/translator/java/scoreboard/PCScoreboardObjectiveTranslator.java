package org.dragonet.proxy.network.translator.java.scoreboard;

import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import org.dragonet.proxy.data.scoreboard.DisplaySlot;
import org.dragonet.proxy.data.scoreboard.Objective;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = ServerScoreboardObjectivePacket.class)
public class PCScoreboardObjectiveTranslator extends PacketTranslator<ServerScoreboardObjectivePacket> {

    @Override
    public void translate(ProxySession session, ServerScoreboardObjectivePacket packet) {
        Scoreboard scoreboard = session.getWorldCache().getScoreboard();
        String displayName = packet.getName();

        if(packet.getDisplayName() != null) {
            displayName = MessageTranslator.translate(packet.getDisplayName());
        }

        switch(packet.getAction()) {
            case ADD:
                Objective newObjective = scoreboard.registerObjective(packet.getName(), displayName);
                newObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

                // TODO: send to client
                break;
            case REMOVE:
                scoreboard.removeObjective(packet.getName());
                break;
            case UPDATE:
                Objective updateObjective = scoreboard.getObjective(packet.getName());
                if(updateObjective != null && packet.getDisplayName() != null) {
                    updateObjective.setDisplayName(displayName);

                    // TODO: send to client
                }
                break;
        }
    }
}
