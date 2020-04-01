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
package org.dragonet.proxy.network.translator.java.entity.movement;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@PCPacketTranslator(packetClass = ServerEntityTeleportPacket.class)
public class PCEntityTeleportTranslator extends PacketTranslator<ServerEntityTeleportPacket> {

    @Override
    public void translate(ProxySession session, ServerEntityTeleportPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            //log.info(TextFormat.GRAY + "(debug) EntityTeleport: Cached entity is null");
            return;
        }

        cachedEntity.moveAbsolute(session, Vector3f.from(packet.getX(), packet.getY(), packet.getZ()),
            Vector3f.from(packet.getPitch(), packet.getYaw(), packet.getYaw()), packet.isOnGround(), true);
    }
}
