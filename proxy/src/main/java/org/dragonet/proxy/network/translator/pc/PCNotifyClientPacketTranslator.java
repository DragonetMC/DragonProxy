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
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.pocket113.play.LevelEvent;
import sul.protocol.pocket113.play.SetPlayerGameType;
import sul.utils.Packet;

public class PCNotifyClientPacketTranslator implements PCPacketTranslator<ServerNotifyClientPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerNotifyClientPacket packet) {
        switch (packet.getNotification()) {
            case CHANGE_GAMEMODE:
                GameMode gm = (GameMode) packet.getValue();
                SetPlayerGameType pk = new SetPlayerGameType();
                if (gm == GameMode.CREATIVE) {
                    pk.gamemode = 1;
                } else {
                    pk.gamemode = 0;
                }
                return new Packet[]{pk};
            case START_RAIN:
                LevelEvent evtStartRain = new LevelEvent();
                evtStartRain.eventId = LevelEvent.START_RAIN;
                return new Packet[]{evtStartRain};
            case STOP_RAIN:
                LevelEvent evtStopRain = new LevelEvent();
                evtStopRain.eventId = LevelEvent.STOP_RAIN;
                return new Packet[]{evtStopRain};
        }
        return null;
    }

}
