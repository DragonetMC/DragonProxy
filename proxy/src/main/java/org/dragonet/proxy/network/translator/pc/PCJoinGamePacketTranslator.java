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
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AdventureSettingsPacket;
import org.dragonet.proxy.protocol.packets.SetPlayerGameTypePacket;

public class PCJoinGamePacketTranslator implements PCPacketTranslator<ServerJoinGamePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerJoinGamePacket packet) {
        //This packet is not fully useable, we cache it for now. 
        session.getDataCache().put(CacheKey.PLAYER_EID, packet.getEntityId());  //Stores the real entity ID

        if (session.getProxy().getAuthMode().equals("online")) {
            //Online mode already sent packets
            
            SetPlayerGameTypePacket pkSetGameMode = new SetPlayerGameTypePacket();
            pkSetGameMode.gamemode = packet.getGameMode() == GameMode.CREATIVE ? 1 : 0;
            
            AdventureSettingsPacket adv = new AdventureSettingsPacket();
            adv.setFlag(AdventureSettingsPacket.AUTO_JUMP, true);
            adv.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, true);
            
            return new PEPacket[]{pkSetGameMode, adv};
        }

        session.getDataCache().put(CacheKey.PACKET_JOIN_GAME_PACKET, packet);
        return null;
    }

}
