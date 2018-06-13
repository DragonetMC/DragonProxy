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
import org.dragonet.common.maths.Vector3F;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.packets.SpawnExperienceOrb;

public class PCSpawnExpOrbPacketTranslator implements IPCPacketTranslator<ServerSpawnExpOrbPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerSpawnExpOrbPacket packet) {
        CachedEntity entity = session.getEntityCache().newEntity(packet);

        if (entity == null)
            return null;

        if (session.isSpawned()) {
            SpawnExperienceOrb spawnXpOrb = new SpawnExperienceOrb();
            spawnXpOrb.position = new Vector3F((float) entity.x, (float) entity.y + entity.peType.getOffset(), (float) entity.z);
            spawnXpOrb.count = packet.getExp();
            entity.spawned = true;
            session.sendPacket(spawnXpOrb);
        }

        return null;
    }

}
