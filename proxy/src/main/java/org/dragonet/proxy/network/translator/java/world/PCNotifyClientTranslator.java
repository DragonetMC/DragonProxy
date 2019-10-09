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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.notify.RainStrengthValue;
import com.github.steveice10.mc.protocol.data.game.world.notify.ThunderStrengthValue;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import static com.nukkitx.protocol.bedrock.packet.LevelEventPacket.Event.*;
import com.nukkitx.protocol.bedrock.packet.SetPlayerGameTypePacket;
import com.nukkitx.protocol.bedrock.packet.ShowCreditsPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.concurrent.ThreadLocalRandom;

@Log4j2
public class PCNotifyClientTranslator implements PacketTranslator<ServerNotifyClientPacket> {
    public static final PCNotifyClientTranslator INSTANCE = new PCNotifyClientTranslator();

    @Override
    public void translate(ProxySession session, ServerNotifyClientPacket packet) {
        switch(packet.getNotification()) {
            case CHANGE_GAMEMODE:
                SetPlayerGameTypePacket setGameTypePacket = new SetPlayerGameTypePacket();
                setGameTypePacket.setGamemode(MagicValues.value(Integer.class, packet.getValue()));

                session.sendPacket(setGameTypePacket);
                break;
            case START_RAIN:
                session.sendPacket(createLevelEvent(START_RAIN, ThreadLocalRandom.current().nextInt(50000) + 10000));
                break;
            case RAIN_STRENGTH:
                double rainStrength = ((RainStrengthValue) packet.getValue()).getStrength();
                if(rainStrength > 0.0) {
                    session.sendPacket(createLevelEvent(START_RAIN, (int) rainStrength * 65535));
                    break;
                }
            case STOP_RAIN:
                session.sendPacket(createLevelEvent(STOP_RAIN, 0));
                break;
            case THUNDER_STRENGTH:
                double thunderStrength = ((ThunderStrengthValue) packet.getValue()).getStrength();

                log.info(TextFormat.DARK_AQUA + "Thunder strength: " + thunderStrength);
                if(thunderStrength > 0.0) {
                    // TODO: this doesnt work?
                    session.sendPacket(createLevelEvent(START_THUNDER, (int) thunderStrength * 65535));
                } else {
                    session.sendPacket(createLevelEvent(STOP_THUNDER, 0));
                }
                break;
            case ENTER_CREDITS:
                ShowCreditsPacket showCreditsPacket = new ShowCreditsPacket();
                showCreditsPacket.setRuntimeEntityId(session.getCachedEntity().getProxyEid());
                showCreditsPacket.setStatus(ShowCreditsPacket.Status.START_CREDITS);

                session.sendPacket(showCreditsPacket);
                break;
            case DEMO_MESSAGE:
                log.info(TextFormat.AQUA + "Demo message received");
                break;
            case INVALID_BED:
                log.info(TextFormat.AQUA + "Invalid bed");
                break;
            case ARROW_HIT_PLAYER:
                log.info(TextFormat.AQUA + "Arrow hit player!");
                break;
        }
    }

    private LevelEventPacket createLevelEvent(LevelEventPacket.Event event, int data) {
        LevelEventPacket packet = new LevelEventPacket();
        packet.setEvent(event);
        packet.setData(data);
        packet.setPosition(Vector3f.ZERO);
        return packet;
    }
}
