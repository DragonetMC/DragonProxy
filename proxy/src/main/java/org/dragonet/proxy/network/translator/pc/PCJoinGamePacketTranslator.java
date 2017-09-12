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
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.pocket113.play.AdventureSettings;
import sul.protocol.pocket113.play.SetPlayerGameType;
import sul.utils.Packet;

public class PCJoinGamePacketTranslator implements PCPacketTranslator<ServerJoinGamePacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerJoinGamePacket packet) {
        //This packet is not fully useable, we cache it for now. 
        session.getDataCache().put(CacheKey.PLAYER_EID, packet.getEntityId());  //Stores the real entity ID

        if (session.getProxy().getAuthMode().equals("online")) {
            //Online mode already sent packets
            
            SetPlayerGameType pkSetGameMode = new SetPlayerGameType(packet.getGameMode() == GameMode.CREATIVE ? 1 : 0);
            
            AdventureSettings adv = new AdventureSettings();
            int settings = 0x1 | 0x20 | 0x40;
            adv.flags = settings;
            
            return new Packet[]{pkSetGameMode, adv};
        }

        session.getDataCache().put(CacheKey.PACKET_JOIN_GAME_PACKET, packet);
        return null;
    }

}
