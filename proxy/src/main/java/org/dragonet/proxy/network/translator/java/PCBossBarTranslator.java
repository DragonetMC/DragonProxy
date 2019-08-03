/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.nukkitx.protocol.bedrock.packet.BossEventPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

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
                bossEventPacket.setTitle(packet.getTitle().getText());
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
                bossEventPacket.setTitle(packet.getTitle().getText());
                break;
            default:
                log.warn("Unhandled boss bar action: " + packet.getAction().name());
        }

        session.getBedrockSession().sendPacket(bossEventPacket);
    }
}
