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

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.RespawnPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@Log4j2
@PCPacketTranslator(packetClass = ServerPlayerPositionRotationPacket.class)
public class PCPlayerPositionRotationTranslator extends PacketTranslator<ServerPlayerPositionRotationPacket> {
    public static final PCPlayerPositionRotationTranslator INSTANCE = new PCPlayerPositionRotationTranslator();

    @Override
    public void translate(ProxySession session, ServerPlayerPositionRotationPacket packet) {
        CachedPlayer player = session.getCachedEntity();

        if (!player.isSpawned()) {
            player.setPosition(Vector3f.from(packet.getX(), packet.getY(), packet.getZ()));
            player.setRotation(Vector3f.from(packet.getPitch(), packet.getYaw(), 0));

            // Tell the client we are ready to spawn
            RespawnPacket respawnPacket = new RespawnPacket();
            respawnPacket.setRuntimeEntityId(player.getProxyEid());
            respawnPacket.setPosition(player.getOffsetPosition());
            respawnPacket.setState(RespawnPacket.State.SERVER_READY);
            session.sendPacket(respawnPacket);

            // Send entity metadata
            player.sendMetadata(session);

            MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
            movePlayerPacket.setRuntimeEntityId(player.getProxyEid());
            movePlayerPacket.setPosition(player.getOffsetPosition().add(0, 0.1f, 0));
            movePlayerPacket.setRotation(player.getRotation());
            movePlayerPacket.setMode(MovePlayerPacket.Mode.RESET);
            movePlayerPacket.setOnGround(true);

            session.sendPacket(movePlayerPacket);
            player.setSpawned(true);

            log.info("Spawned player " + session.getUsername() + " at " + packet.getX() + " " + packet.getY() + " " + packet.getZ());
            return;
        }

        player.setSpawned(true);

        player.moveAbsolute(session, Vector3f.from(packet.getX(), packet.getY() + BedrockEntityType.PLAYER.getOffset() + 0.1f, packet.getZ()),
            Vector3f.from(packet.getPitch(), packet.getYaw(), 0), true, false);

        session.sendRemotePacket(new ClientTeleportConfirmPacket(packet.getTeleportId()));
    }
}
