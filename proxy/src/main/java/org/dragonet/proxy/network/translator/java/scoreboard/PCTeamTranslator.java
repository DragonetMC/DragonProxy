package org.dragonet.proxy.network.translator.java.scoreboard;

import com.github.steveice10.mc.protocol.data.game.scoreboard.NameTagVisibility;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamColor;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.data.scoreboard.Team;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = ServerTeamPacket.class)
public class PCTeamTranslator extends PacketTranslator<ServerTeamPacket> {

    @Override
    public void translate(ProxySession session, ServerTeamPacket packet) {
        Scoreboard scoreboard = session.getWorldCache().getScoreboard();
        Team team = scoreboard.getTeam(packet.getTeamName());

        if(packet.getAction() == TeamAction.CREATE) {
            team = scoreboard.registerTeam(packet.getTeamName());
        }

        // Handle team data being updated which is also done when its created
        if(packet.getAction() == TeamAction.CREATE || packet.getAction() == TeamAction.UPDATE) {
            team.setDisplayName(MessageTranslator.translate(packet.getDisplayName()));
            team.setPrefix(MessageTranslator.translate(packet.getPrefix()));
            team.setSuffix(MessageTranslator.translate(packet.getSuffix()));

            // Currently unused
            team.setNameTagVisibility(packet.getNameTagVisibility());
            team.setFriendlyFire(packet.isFriendlyFire());
            team.setSeeFriendlyInvisibles(packet.isSeeFriendlyInvisibles());
        }

        // Handle entities being added to the team
        if(packet.getAction() == TeamAction.CREATE || packet.getAction() == TeamAction.ADD_PLAYER) {
            team.addPlayers(packet.getPlayers());
        }

        // Handle entities being removed
        if(packet.getAction() == TeamAction.REMOVE_PLAYER) {
            team.removePlayers(packet.getPlayers());
        }

        // Handle the team being deleted
        if(packet.getAction() == TeamAction.REMOVE) {
            scoreboard.removeTeam(packet.getTeamName());
        }
    }
}
