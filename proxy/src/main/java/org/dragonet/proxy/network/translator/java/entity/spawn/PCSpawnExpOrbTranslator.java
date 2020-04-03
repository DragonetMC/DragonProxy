/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.entity.spawn;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.SpawnExperienceOrbPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@Log4j2
@PCPacketTranslator(packetClass = ServerSpawnExpOrbPacket.class)
public class PCSpawnExpOrbTranslator extends PacketTranslator<ServerSpawnExpOrbPacket> {

    @Override
    public void translate(ProxySession session, ServerSpawnExpOrbPacket packet) {
        SpawnExperienceOrbPacket spawnExperienceOrbPacket = new SpawnExperienceOrbPacket();
        spawnExperienceOrbPacket.setPosition(Vector3f.from(packet.getX(), packet.getY(), packet.getZ()));
        spawnExperienceOrbPacket.setAmount(packet.getEntityId());

        session.sendPacket(spawnExperienceOrbPacket);

        CachedEntity cachedEntity = session.getEntityCache().newEntity(BedrockEntityType.EXPERIENCE_ORB, packet.getEntityId());
        cachedEntity.setSpawned(true);
    }
}
