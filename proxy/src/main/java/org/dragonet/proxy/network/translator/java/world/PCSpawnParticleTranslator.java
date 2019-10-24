/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.SpawnParticleEffectPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCSpawnParticleTranslator implements PacketTranslator<ServerSpawnParticlePacket> {
    public static final PCSpawnParticleTranslator INSTANCE = new PCSpawnParticleTranslator();

    @Override
    public void translate(ProxySession session, ServerSpawnParticlePacket packet) {
        // This doesnt work yet, ffs Mojang
        SpawnParticleEffectPacket spawnParticlePacket = new SpawnParticleEffectPacket();
        spawnParticlePacket.setDimensionId(0);
        spawnParticlePacket.setIdentifier("minecraft:heart");
        spawnParticlePacket.setPosition(Vector3f.from(0, 48, 2));

        //session.sendPacket(spawnParticlePacket);
    }
}
