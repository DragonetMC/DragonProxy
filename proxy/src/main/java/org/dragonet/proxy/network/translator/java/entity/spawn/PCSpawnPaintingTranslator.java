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
package org.dragonet.proxy.network.translator.java.entity.spawn;

import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.SpawnExperienceOrbPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockPaintingType;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedPainting;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.types.EntityTypeTranslator;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@PCPacketTranslator(packetClass = ServerSpawnPaintingPacket.class)
public class PCSpawnPaintingTranslator extends PacketTranslator<ServerSpawnPaintingPacket> {

    @Override
    public void translate(ProxySession session, ServerSpawnPaintingPacket packet) {
        // TODO: may want to map this in the future but paintings are the same in both versions
        String name = BedrockPaintingType.valueOf(packet.getPaintingType().name()).getTitle();

        CachedPainting cachedEntity = session.getEntityCache().newPainting(packet.getEntityId(), name);
        cachedEntity.setHangingDirection(packet.getDirection());
        cachedEntity.setPosition(Vector3f.from(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ()));

        cachedEntity.spawn(session);
    }
}
