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
package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@Log4j2
@PacketRegisterInfo(packet = ServerPlayerPositionRotationPacket.class)
public class PCPlayerPositionRotationTranslator extends PacketTranslator<ServerPlayerPositionRotationPacket> {

    @Override
    public void translate(ProxySession session, ServerPlayerPositionRotationPacket packet) {
        CachedPlayer entity = session.getCachedEntity();

        if (!entity.isSpawned()) {
            entity.sendMetadata(session);

            MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
            movePlayerPacket.setRuntimeEntityId(entity.getProxyEid());
            movePlayerPacket.setPosition(Vector3f.from(packet.getX(), packet.getY() + BedrockEntityType.PLAYER.getOffset() + 0.1f, packet.getZ()));
            movePlayerPacket.setRotation(Vector3f.from(packet.getPitch(), packet.getYaw(), 0));
            movePlayerPacket.setMode(MovePlayerPacket.Mode.RESET);
            movePlayerPacket.setOnGround(true);

            session.sendPacket(movePlayerPacket);
            entity.setSpawned(true);

            session.getChunkCache().sendOrderedChunks(session);

            log.info("Spawned player " + session.getUsername() + " at " + packet.getX() + " " + packet.getY() + " " + packet.getZ());
            return;
        }

        entity.setSpawned(true);

        if(!packet.getRelative().isEmpty()) {
            entity.moveRelative(session, Vector3f.from(packet.getX(), packet.getY(), packet.getZ()), Vector3f.from(packet.getPitch(), packet.getYaw(), 0), true, true);
        } else {
            double x = Math.abs(entity.getPosition().getX() - packet.getX());
            double y = Math.abs(entity.getPosition().getY() - packet.getY());
            double z = Math.abs(entity.getPosition().getZ() - packet.getZ());

            if (x >= 1 || y >= 1 || z >= 1) {
                entity.moveAbsolute(session, Vector3f.from(packet.getX(), packet.getY(), packet.getZ()), Vector3f.from(packet.getPitch(), packet.getYaw(), 0), true, false);
            }
        }

        session.getChunkCache().sendOrderedChunks(session);

        session.sendRemotePacket(new ClientTeleportConfirmPacket(packet.getTeleportId()));
    }
}
