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

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;

public class PCJoinGamePacketTranslator implements IPCPacketTranslator<ServerJoinGamePacket> {

    public PEPacket[] translate(UpstreamSession session, ServerJoinGamePacket packet) {
        session.getDataCache().put(CacheKey.PLAYER_EID, packet.getEntityId()); // Stores the real entity ID
        session.getEntityCache().updateClientEntity(packet);

        // This packet is not fully useable, we cache it for now.
        session.getDataCache().put(CacheKey.PACKET_JOIN_GAME_PACKET, packet);
        return null;
    }
}
