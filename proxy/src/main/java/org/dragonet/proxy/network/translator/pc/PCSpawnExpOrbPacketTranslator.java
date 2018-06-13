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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.SpawnExperienceOrb;
import org.dragonet.proxy.network.cache.EntityCache;

public class PCSpawnExpOrbPacketTranslator implements IPCPacketTranslator<ServerSpawnExpOrbPacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerSpawnExpOrbPacket packet) {
        ICachedEntity entity = ((EntityCache)session.getEntityCache()).newEntity(packet);

        if (entity == null)
            return null;

        if (session.isSpawned()) {
            SpawnExperienceOrb spawnXpOrb = new SpawnExperienceOrb();
            spawnXpOrb.position = new Vector3F((float) entity.getX(), (float) entity.getY() + entity.getPeType().getOffset(), (float) entity.getZ());
            spawnXpOrb.count = packet.getExp();
            entity.setSpawned(true);
            session.sendPacket(spawnXpOrb);
        }

        return null;
    }

}
