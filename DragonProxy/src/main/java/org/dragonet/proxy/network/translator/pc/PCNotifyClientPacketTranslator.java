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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.LevelEvent;
import sul.protocol.pocket101.play.SetPlayerGametype;
import sul.utils.Tuples;

public class PCNotifyClientPacketTranslator implements PCPacketTranslator<ServerNotifyClientPacket> {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, ServerNotifyClientPacket packet) {
        switch (packet.getNotification()) {
        case CHANGE_GAMEMODE:
            GameMode gm = (GameMode) packet.getValue();
            SetPlayerGametype pk = new SetPlayerGametype();
            if (gm == GameMode.CREATIVE) {
                pk.gametype = 1;
            } else {
                pk.gametype = 0;
            }
            return new sul.utils.Packet[] {pk};
        case START_RAIN:
            LevelEvent evtStartRain = new LevelEvent();
            evtStartRain.eventId = LevelEvent.START_RAIN;
            evtStartRain.position = new Tuples.FloatXYZ(0.0f, 0.0f, 0.0f);
            evtStartRain.data = 0;
            return new sul.utils.Packet[] {evtStartRain};
        case STOP_RAIN:
            LevelEvent evtStopRain = new LevelEvent();
            evtStopRain.eventId = LevelEvent.STOP_RAIN;
            evtStopRain.position = new Tuples.FloatXYZ(0.0f, 0.0f, 0.0f);
            evtStopRain.data = 0;
            return new sul.utils.Packet[] {evtStopRain};
		default:
			System.err.println("Error while handling NotifyClientPacket; Unhandled notification type: " + packet.getNotification());
			break;
        }
        return new sul.utils.Packet[0];
    }

}
