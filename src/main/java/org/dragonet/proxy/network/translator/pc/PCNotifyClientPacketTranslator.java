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
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.LevelEventPacket;
import org.dragonet.proxy.protocol.packets.SetPlayerGameTypePacket;

public class PCNotifyClientPacketTranslator implements IPCPacketTranslator<ServerNotifyClientPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerNotifyClientPacket packet) {
        switch (packet.getNotification()) {
            case CHANGE_GAMEMODE:
                GameMode gm = (GameMode) packet.getValue();
                SetPlayerGameTypePacket pk = new SetPlayerGameTypePacket();
                if (gm == GameMode.CREATIVE) {
                    pk.gamemode = 1;
                } else {
                    pk.gamemode = 0;
                }
                return new PEPacket[]{pk};
            case START_RAIN:
                LevelEventPacket evtStartRain = new LevelEventPacket();
                evtStartRain.eventId = LevelEventPacket.EVENT_START_RAIN;
                return new PEPacket[]{evtStartRain};
            case STOP_RAIN:
                LevelEventPacket evtStopRain = new LevelEventPacket();
                evtStopRain.eventId = LevelEventPacket.EVENT_STOP_RAIN;
                return new PEPacket[]{evtStopRain};
        }
        return null;
    }
}
