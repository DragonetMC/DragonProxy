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

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.nukkitx.protocol.bedrock.data.EntityDataDictionary;
import com.nukkitx.protocol.bedrock.packet.PlayerSkinPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.types.EntityTypeTranslator;
import org.dragonet.proxy.util.SkinUtils;

import java.nio.charset.StandardCharsets;

@Log4j2
public class PCSpawnMobTranslator implements PacketTranslator<ServerSpawnMobPacket> {
    public static final PCSpawnMobTranslator INSTANCE = new PCSpawnMobTranslator();

    @Override
    public void translate(ProxySession session, ServerSpawnMobPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity != null) {
            log.trace("Cached entity already exists, cant spawn a new one");
            return;
        }

        EntityType entityType = EntityTypeTranslator.translateToBedrock(packet.getType());
        if(entityType == null) {
            log.warn("Cannot translate mob type: " + packet.getType().name());
            return;
        }

        cachedEntity = session.getEntityCache().newEntity(entityType, packet.getEntityId());

        EntityDataDictionary metadata = EntityMetaTranslator.translateToBedrock(packet.getMetadata());
        cachedEntity.getMetadata().putAll(metadata);

        cachedEntity.setPosition(new Vector3f(packet.getX(), packet.getY(), packet.getZ()));
        cachedEntity.setMotion(new Vector3f(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ()));
        cachedEntity.setRotation(new Vector3f(packet.getPitch(), packet.getHeadYaw(), packet.getYaw()));

        cachedEntity.spawn(session);
    }
}
