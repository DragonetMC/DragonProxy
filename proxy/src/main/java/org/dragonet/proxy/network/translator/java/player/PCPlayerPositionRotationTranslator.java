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
package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
public class PCPlayerPositionRotationTranslator implements PacketTranslator<ServerPlayerPositionRotationPacket> {
    public static final PCPlayerPositionRotationTranslator INSTANCE = new PCPlayerPositionRotationTranslator();

    @Override
    public void translate(ProxySession session, ServerPlayerPositionRotationPacket packet) {
        log.trace("GOT player position x=" + packet.getX() + ", y=" + packet.getY() + ", z=" + packet.getZ());

        CachedEntity cachedEntity = session.getCachedEntity();

        if (!cachedEntity.isSpawned()) {
            cachedEntity.moveAbsolute(Vector3f.from(packet.getX(), packet.getY() + EntityType.PLAYER.getOffset() + 0.1f, packet.getZ()), packet.getPitch(), packet.getYaw());

            SetEntityDataPacket entityDataPacket = new SetEntityDataPacket();
            entityDataPacket.setRuntimeEntityId(1);
            entityDataPacket.getMetadata().putAll(cachedEntity.getMetadata());
            session.sendPacket(entityDataPacket);

            MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
            movePlayerPacket.setRuntimeEntityId(1);
            movePlayerPacket.setPosition(Vector3f.from(packet.getX(), packet.getY() + EntityType.PLAYER.getOffset() + 0.1f, packet.getZ()));
            movePlayerPacket.setRotation(Vector3f.from(packet.getPitch(), packet.getYaw(), 0));
            movePlayerPacket.setMode(MovePlayerPacket.Mode.RESET);
            movePlayerPacket.setOnGround(true);
            cachedEntity.setShouldMove(false);

            session.sendPacket(movePlayerPacket);
            cachedEntity.setSpawned(true);

            log.info("Spawned player " + session.getUsername() + " at " + packet.getX() + " " + packet.getY() + " " + packet.getZ());
            return;
        }

        cachedEntity.moveAbsolute(Vector3f.from(packet.getX(), packet.getY() + EntityType.PLAYER.getOffset() + 0.1f, packet.getZ()), packet.getPitch(), packet.getYaw());

        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setRuntimeEntityId(1);
        movePlayerPacket.setPosition(Vector3f.from(packet.getX(), packet.getY() + EntityType.PLAYER.getOffset() + 0.01f, packet.getZ()));
        movePlayerPacket.setRotation(Vector3f.from(packet.getPitch(), packet.getYaw(), 0));
        movePlayerPacket.setMode(MovePlayerPacket.Mode.NORMAL);
        movePlayerPacket.setOnGround(true);

        cachedEntity.setShouldMove(false);

        session.sendPacket(movePlayerPacket);
        cachedEntity.setSpawned(true);

        ClientTeleportConfirmPacket teleportConfirmPacket = new ClientTeleportConfirmPacket(packet.getTeleportId());
        session.sendRemotePacket(teleportConfirmPacket);
    }
}
