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
        bossEventPacket.setDarkenSky(packet.getDarkenSky() ? 1 : 0);
        bossEventPacket.setHealthPercentage(packet.getHealth());
        bossEventPacket.setTitle(packet.getTitle().getText());
        bossEventPacket.setPlayerUniqueEntityId(((Integer) session.getDataCache().get("player_eid")).longValue());
        bossEventPacket.setBossUniqueEntityId(0); // TODO

        switch(packet.getAction()) {
            case ADD:
                bossEventPacket.setType(BossEventPacket.Type.SHOW);
                break;
            default:
                log.warn("Unhandled boss bar action: " + packet.getAction().name());
        }

        log.warn("Sending boss event packet");

        session.getBedrockSession().sendPacket(bossEventPacket);
    }
}
