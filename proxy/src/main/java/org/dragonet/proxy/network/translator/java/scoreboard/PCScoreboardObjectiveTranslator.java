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

import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.scoreboard.ScoreObjective;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCScoreboardObjectiveTranslator implements PacketTranslator<ServerScoreboardObjectivePacket> {
    public static final PCScoreboardObjectiveTranslator INSTANCE = new PCScoreboardObjectiveTranslator();

    @Override
    public void translate(ProxySession session, ServerScoreboardObjectivePacket packet) {
        WorldCache worldCache = session.getWorldCache();
        Scoreboard scoreboard = new Scoreboard(session);

        if(worldCache.getScoreboard() == null) {
            worldCache.setScoreboard(scoreboard);
        } else {
            scoreboard = worldCache.getScoreboard();
        }

        switch(packet.getAction()) {
            case ADD:
                ScoreObjective objective = scoreboard.addObjective(packet.getName(), MessageTranslator.translate(packet.getDisplayName()));
                objective.setDisplaySlot(ScoreObjective.DisplaySlot.SIDEBAR);
                scoreboard.update(objective);
                log.warn("Adding objective: " + packet.getName() + " with display name: " + MessageTranslator.translate(packet.getDisplayName()));
                break;
            case UPDATE:
                ScoreObjective updateObjective = scoreboard.getObjective(packet.getName());
                //log.warn("  Updating objective: " + packet.getName() + " with display name: " + MessageTranslator.translate(packet.getDisplayName()));
                if(updateObjective == null) {
                    log.warn("UpdateObjective is null");
                    return;
                }
                updateObjective.setDisplayName(MessageTranslator.translate(packet.getDisplayName()));
                scoreboard.update(updateObjective);
                break;
        }
    }
}
