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

import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.scoreboard.ScoreObjective;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.util.TextFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCDisplayScoreboardTranslator implements PacketTranslator<ServerDisplayScoreboardPacket> {
    public static final PCDisplayScoreboardTranslator INSTANCE = new PCDisplayScoreboardTranslator();

    @Override
    public void translate(ProxySession session, ServerDisplayScoreboardPacket packet) {
        WorldCache worldCache = session.getWorldCache();
        if(worldCache.getScoreboard() == null) {
            log.info(TextFormat.GRAY + "(debug) PCDisplayScoreboardTranslator: cached scoreboard is null");
            return;
        }

        Scoreboard scoreboard = worldCache.getScoreboard();
        ScoreObjective objective = scoreboard.getObjective(packet.getScoreboardName());
        if(objective == null) {
            log.warn("Scoreboard objective is null in display translator");
            return;
        }

        switch(packet.getPosition()) {
            case PLAYER_LIST:
                objective.setDisplaySlot(ScoreObjective.DisplaySlot.LIST);
                break;
            case BELOW_NAME:
                objective.setDisplaySlot(ScoreObjective.DisplaySlot.BELOWNAME);
                break;
            case SIDEBAR:
            default: // Covers all sidebar team variations
                objective.setDisplaySlot(ScoreObjective.DisplaySlot.SIDEBAR);
                break;
        }

        scoreboard.update(objective);
    }
}
