/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import org.dragonet.api.translators.IPCPacketTranslator;

import java.util.concurrent.ThreadLocalRandom;

import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.AdventureSettingsPacket;
import org.dragonet.protocol.packets.LevelEventPacket;
import org.dragonet.protocol.packets.SetPlayerGameTypePacket;

public class PCNotifyClientPacketTranslator implements IPCPacketTranslator<ServerNotifyClientPacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerNotifyClientPacket packet) {
        switch (packet.getNotification()) {
            case CHANGE_GAMEMODE:
                GameMode gm = (GameMode) packet.getValue();
                SetPlayerGameTypePacket pkgm = new SetPlayerGameTypePacket();
                pkgm.gamemode = gm == GameMode.CREATIVE ? 1 : 0;

                AdventureSettingsPacket adv = new AdventureSettingsPacket();
                adv.setFlag(AdventureSettingsPacket.WORLD_IMMUTABLE, gm.equals(GameMode.ADVENTURE));
                adv.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, gm.equals(GameMode.CREATIVE) || gm.equals(GameMode.SPECTATOR));
                adv.setFlag(AdventureSettingsPacket.NO_CLIP, gm.equals(GameMode.SPECTATOR));
                adv.setFlag(AdventureSettingsPacket.WORLD_BUILDER, !gm.equals(GameMode.SPECTATOR) || !gm.equals(GameMode.ADVENTURE));
                adv.setFlag(AdventureSettingsPacket.FLYING, gm.equals(GameMode.SPECTATOR));
                adv.setFlag(AdventureSettingsPacket.MUTED, false);
                adv.eid = session.getEntityCache().getClientEntity().getProxyEid();
                adv.commandsPermission = AdventureSettingsPacket.PERMISSION_NORMAL;
                adv.playerPermission = AdventureSettingsPacket.LEVEL_PERMISSION_MEMBER;

                session.sendPacket(pkgm);
                session.sendPacket(adv);
                if (gm == GameMode.CREATIVE)
                    session.sendCreativeInventory();
                break;
            case START_RAIN:
                LevelEventPacket evtStartRain = new LevelEventPacket();
                evtStartRain.eventId = LevelEventPacket.EVENT_START_RAIN;
                evtStartRain.data = ThreadLocalRandom.current().nextInt(50000) + 10000; //Some from Nukkit, but ... :D
                return new PEPacket[]{evtStartRain};
            case STOP_RAIN:
                LevelEventPacket evtStopRain = new LevelEventPacket();
                evtStopRain.eventId = LevelEventPacket.EVENT_STOP_RAIN;
                return new PEPacket[]{evtStopRain};
            case ARROW_HIT_PLAYER:
                break;
            case AFFECTED_BY_ELDER_GUARDIAN:
                break;
            case DEMO_MESSAGE:
                break;
            case ENTER_CREDITS:
                break;
            case INVALID_BED:
                break;
            case RAIN_STRENGTH:
                break;
            case THUNDER_STRENGTH:
                break;
        }
        return null;
    }
}
