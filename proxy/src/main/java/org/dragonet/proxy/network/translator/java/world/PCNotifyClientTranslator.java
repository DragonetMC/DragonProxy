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
package org.dragonet.proxy.network.translator.java.world;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.notify.RainStrengthValue;
import com.github.steveice10.mc.protocol.data.game.world.notify.ThunderStrengthValue;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import static com.nukkitx.protocol.bedrock.packet.LevelEventPacket.Event.*;
import com.nukkitx.protocol.bedrock.packet.SetPlayerGameTypePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

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

                session.getBedrockSession().sendPacket(setGameTypePacket);
                break;
            case START_RAIN:
                session.getBedrockSession().sendPacket(createLevelEvent(START_RAIN, ThreadLocalRandom.current().nextInt(50000) + 10000));
                break;
            case RAIN_STRENGTH:
                double rainStrength = ((RainStrengthValue) packet.getValue()).getStrength();
                if(rainStrength > 0.0) {
                    session.getBedrockSession().sendPacket(createLevelEvent(START_RAIN, (int) rainStrength * 65535));
                    break;
                }
            case STOP_RAIN:
                session.getBedrockSession().sendPacket(createLevelEvent(STOP_RAIN, 0));
                break;
            case THUNDER_STRENGTH:
                double thunderStrength = ((ThunderStrengthValue) packet.getValue()).getStrength();

                log.warn("Thunder strength: " + thunderStrength);
                if(thunderStrength > 0.0) {
                    session.getBedrockSession().sendPacket(createLevelEvent(START_THUNDER, (int) thunderStrength * 65535));
                } else {
                    session.getBedrockSession().sendPacket(createLevelEvent(STOP_THUNDER, 0));
                }
                break;
            case INVALID_BED:
                log.warn("Invalid bed");
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
