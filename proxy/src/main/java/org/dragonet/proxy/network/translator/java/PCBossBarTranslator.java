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
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.nukkitx.protocol.bedrock.packet.BossEventPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCBossBarTranslator implements PacketTranslator<ServerBossBarPacket> {
    public static final PCBossBarTranslator INSTANCE = new PCBossBarTranslator();

    @Override
    public void translate(ProxySession session, ServerBossBarPacket packet) {
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setColor(1);
        bossEventPacket.setOverlay(1);
        bossEventPacket.setDarkenSky(1);
        bossEventPacket.setPlayerUniqueEntityId(((Integer) session.getDataCache().get("player_eid")).longValue());
        bossEventPacket.setBossUniqueEntityId(((Integer) session.getDataCache().get("player_eid")).longValue()); // TODO

        switch(packet.getAction()) {
            case ADD:
                bossEventPacket.setTitle(MessageTranslator.translate(packet.getTitle().getText()));
                bossEventPacket.setType(BossEventPacket.Type.SHOW);
                bossEventPacket.setHealthPercentage(packet.getHealth());
                break;
            case REMOVE:
                bossEventPacket.setType(BossEventPacket.Type.HIDE);
                break;
            case UPDATE_HEALTH:
                bossEventPacket.setType(BossEventPacket.Type.HEALTH_PERCENTAGE);
                bossEventPacket.setHealthPercentage(packet.getHealth());
                break;
            case UPDATE_TITLE:
                bossEventPacket.setType(BossEventPacket.Type.TITLE);
                bossEventPacket.setTitle(MessageTranslator.translate(packet.getTitle().getText()));
                break;
            case UPDATE_STYLE:
                //bossEventPacket.setType(BossEventPacket.Type.OVERLAY);
                //bossEventPacket.setOverlay(0);
                break;
            default:
                log.warn("Unhandled boss bar action: " + packet.getAction().name());
        }

        session.getBedrockSession().sendPacket(bossEventPacket);
    }
}
