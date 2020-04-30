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
package org.dragonet.proxy.network.translator.bedrock.player;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;
import com.nukkitx.math.GenericMath;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@PacketRegisterInfo(packet = MovePlayerPacket.class)
public class PEMovePlayerTranslator extends PacketTranslator<MovePlayerPacket> {

    @Override
    public void translate(ProxySession session, MovePlayerPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByProxyId(packet.getRuntimeEntityId());
        if(cachedEntity == null) {
            log.info(TextFormat.GRAY + "(debug) MovePlayer: cached entity is null");
            return;
        }

        // Handle controlling a vehicle
        if(packet.getRidingRuntimeEntityId() != 0) {
            ClientVehicleMovePacket clientVehicleMovePacket = new ClientVehicleMovePacket(packet.getPosition().getX(),
                packet.getPosition().getY() - BedrockEntityType.PLAYER.getOffset(), packet.getPosition().getZ(), packet.getRotation().getY(), packet.getRotation().getX());

            session.sendRemotePacket(clientVehicleMovePacket);
            return;
        }

        // Update the cached position and rotation
        cachedEntity.setPosition(packet.getPosition().sub(0, BedrockEntityType.PLAYER.getOffset(), 0));
        cachedEntity.setRotation(Vector3f.from(packet.getRotation().getY(), packet.getRotation().getX(), packet.getRotation().getY()));

        // Tell the remote server that we have moved
        double y = cachedEntity.getPosition().getY();
        if (packet.isOnGround()) {
            y = Math.ceil(y * 2) / 2; // If we only do this when on ground then movement isn't so buggy
        }

        // I must admit some credit to Geyser for finding the precision loss issue
        // I Googled how to not lose precision when casting a float to a double and apparently this is a good way to do it
        ClientPlayerPositionRotationPacket playerPositionRotationPacket = new ClientPlayerPositionRotationPacket(packet.isOnGround(), Double.parseDouble(Float.toString(packet.getPosition().getX())),
           y, Double.parseDouble(Float.toString(packet.getPosition().getZ())), packet.getRotation().getY(), packet.getRotation().getX());

        session.sendRemotePacket(playerPositionRotationPacket);
    }
}
