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
package org.dragonet.proxy.network.translator.java.entity;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
public class PCEntityTeleportTranslator implements PacketTranslator<ServerEntityTeleportPacket> {
    public static final PCEntityTeleportTranslator INSTANCE = new PCEntityTeleportTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityTeleportPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            //log.info("(debug) EntityTeleport: Cached entity is null");
            return;
        }

        cachedEntity.moveAbsolute(new Vector3f(packet.getX(), packet.getY(), packet.getZ()), packet.getPitch(), packet.getYaw());

        if(cachedEntity.isShouldMove()) {
            MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
            moveEntityPacket.setRuntimeEntityId(packet.getEntityId());
            moveEntityPacket.setPosition(cachedEntity.getPosition());
            moveEntityPacket.setRotation(cachedEntity.getRotation());
            moveEntityPacket.setOnGround(packet.isOnGround());
            moveEntityPacket.setTeleported(true);

            session.sendPacket(moveEntityPacket);

            cachedEntity.setShouldMove(false);
        }
    }
}
