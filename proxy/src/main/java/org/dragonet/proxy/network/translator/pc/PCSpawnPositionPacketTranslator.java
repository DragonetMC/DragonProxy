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
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.SetSpawnPositionPacket;
import org.dragonet.common.maths.BlockPosition;

public class PCSpawnPositionPacketTranslator implements IPCPacketTranslator<ServerSpawnPositionPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerSpawnPositionPacket packet) {

        ICachedEntity entity = session.getEntityCache().getClientEntity();
        entity.setSpawnPosition(new BlockPosition(packet.getPosition()));
        SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
        pk.position = entity.getSpawnPosition();
        return new PEPacket[]{};
    }
}
