/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.scoreboard;

import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.scoreboard.ScoreTeam;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCTeamTranslator implements PacketTranslator<ServerTeamPacket> {
    public static final PCTeamTranslator INSTANCE = new PCTeamTranslator();

    @Override
    public void translate(ProxySession session, ServerTeamPacket packet) {
        WorldCache worldCache = session.getWorldCache();
        if(worldCache.getScoreboard() == null) {
            log.warn("Cached scoreboard is null in team translator");
            return;
        }

        Scoreboard scoreboard = worldCache.getScoreboard();
        ScoreTeam team = null;

        if(packet.getAction() == TeamAction.CREATE) {
            team = scoreboard.createTeam(packet.getTeamName());
        } else {
            team = scoreboard.getTeams().get(packet.getTeamName());
        }

        if(team == null) {
            return;
        }

        if(packet.getAction() == TeamAction.CREATE || packet.getAction() == TeamAction.UPDATE) {
            team.setName(packet.getTeamName());
            team.setPrefix(MessageTranslator.translate(packet.getPrefix()));
            team.setSuffix(MessageTranslator.translate(packet.getSuffix()));
            team.setColor(packet.getColor());
        }

        if(packet.getAction() == TeamAction.CREATE || packet.getAction() == TeamAction.ADD_PLAYER) {
            for(String playerName : packet.getPlayers()) {
                if(!playerName.equalsIgnoreCase(session.getUsername())) {
                    continue;
                }

                log.warn(playerName + "   :   " + session.getUsername());
                scoreboard.addPlayerToTeam(playerName, team);
            }
        }

        if (packet.getAction() == TeamAction.REMOVE_PLAYER) {
            for(String playerName : packet.getPlayers()) {
                scoreboard.removePlayerFromTeam(playerName, team);
            }
        }

        if(packet.getAction() == TeamAction.REMOVE) {
            scoreboard.removeTeam(team);
        }
    }
}
