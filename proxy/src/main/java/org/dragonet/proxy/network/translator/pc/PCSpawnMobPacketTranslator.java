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

import org.dragonet.api.translators.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.proxy.network.cache.EntityCache;

public class PCSpawnMobPacketTranslator implements IPCPacketTranslator<ServerSpawnMobPacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerSpawnMobPacket packet) {
        try {
            ICachedEntity entity = ((EntityCache) session.getEntityCache()).newEntity(packet);
            if (entity == null)
                return null;

            entity.spawn(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
