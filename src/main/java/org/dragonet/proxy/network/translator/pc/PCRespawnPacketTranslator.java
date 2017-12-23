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

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.ChangeDimensionPacket;
import org.dragonet.proxy.protocol.packets.PlayStatusPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCRespawnPacketTranslator implements IPCPacketTranslator<ServerRespawnPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerRespawnPacket packet) {

//        CachedEntity entity = session.getEntityCache().getClientEntity();
//        ChangeDimensionPacket d = new ChangeDimensionPacket();
//        d.dimension = packet.getDimension();
//        d.position = entity.spawnPosition;
//        session.sendPacket(d);
        session.sendPacket(new PlayStatusPacket(PlayStatusPacket.PLAYER_SPAWN));
        return new PEPacket[]{};
    }
}
