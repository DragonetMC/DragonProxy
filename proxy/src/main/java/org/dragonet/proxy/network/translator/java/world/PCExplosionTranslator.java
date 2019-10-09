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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.packet.ExplodePacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCExplosionTranslator implements PacketTranslator<ServerExplosionPacket> {
    public static final PCExplosionTranslator INSTANCE = new PCExplosionTranslator();

    @Override
    public void translate(ProxySession session, ServerExplosionPacket packet) {
        ExplodePacket explodePacket = new ExplodePacket();
        explodePacket.setPosition(Vector3f.from(packet.getX(), packet.getY(), packet.getZ()));
        explodePacket.setRadius(packet.getRadius());

        packet.getExploded().forEach((record) -> {
            explodePacket.getRecords().add(Vector3i.from(record.getX(), record.getY(), record.getZ()));
        });

        session.sendPacket(explodePacket);
    }
}
