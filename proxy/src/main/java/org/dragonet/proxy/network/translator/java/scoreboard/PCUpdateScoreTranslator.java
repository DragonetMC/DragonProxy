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

import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import com.nukkitx.protocol.bedrock.packet.SetScorePacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.scoreboard.Score;
import org.dragonet.proxy.data.scoreboard.ScoreObjective;
import org.dragonet.proxy.data.scoreboard.Scoreboard;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.util.TextFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCUpdateScoreTranslator implements PacketTranslator<ServerUpdateScorePacket> {
    public static final PCUpdateScoreTranslator INSTANCE = new PCUpdateScoreTranslator();

    @Override
    public void translate(ProxySession session, ServerUpdateScorePacket packet) {
        WorldCache worldCache = session.getWorldCache();
        if(worldCache.getScoreboard() == null) {
            log.info(TextFormat.GRAY + "(debug) PCUpdateScoreTranslator: cached scoreboard is null");
            return;
        }

        Scoreboard scoreboard = worldCache.getScoreboard();
        ScoreObjective objective = scoreboard.getObjective(packet.getObjective());

        Score score = objective.getOrCreateScore(packet.getEntry());
        score.setName(packet.getEntry());
        score.setValue(packet.getValue());

        switch(packet.getAction()) {
            case ADD_OR_UPDATE:
                log.warn("Updating score: " + packet.getEntry() + ", value: " + packet.getValue());
                scoreboard.updateScores(objective, SetScorePacket.Action.SET);
                break;
            case REMOVE:
                scoreboard.updateScores(objective, SetScorePacket.Action.REMOVE);
                break;
        }
    }
}
