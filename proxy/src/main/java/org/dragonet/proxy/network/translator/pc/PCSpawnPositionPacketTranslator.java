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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.SetSpawnPositionPacket;
import org.dragonet.common.maths.BlockPosition;

public class PCSpawnPositionPacketTranslator implements IPCPacketTranslator<ServerSpawnPositionPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerSpawnPositionPacket packet) {

        CachedEntity entity = session.getEntityCache().getClientEntity();
        entity.spawnPosition = new BlockPosition(packet.getPosition());
        SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
        pk.position = entity.spawnPosition;
        return new PEPacket[]{};
    }
}
