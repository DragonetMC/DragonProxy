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
package org.dragonet.proxy.network.translator.bedrock.player;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PEPacketTranslator;

@Log4j2
@PEPacketTranslator(packetClass = MovePlayerPacket.class)
public class PEMovePlayerTranslator extends PacketTranslator<MovePlayerPacket> {
    public static final PEMovePlayerTranslator INSTANCE = new PEMovePlayerTranslator();

    @Override
    public void translate(ProxySession session, MovePlayerPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByProxyId(packet.getRuntimeEntityId());
        //log.info(packet.getRuntimeEntityId() + " : " + session.getCachedEntity().getProxyEid() + " - " + session.getCachedEntity().getRemoteEid());
        if(cachedEntity == null) {
//            log.info("(debug) Cached entity is null in MovePlayerTranslator: " + packet.getRuntimeEntityId());
////            log.info(packet.getEntityType());
////            log.info(packet.getMode().name());
////            log.info(session.getEntityCache().getEntities().keySet());
            return;
        }

        ClientPlayerPositionRotationPacket playerPositionRotationPacket = new ClientPlayerPositionRotationPacket(
            packet.isOnGround(),
            packet.getPosition().getX(),
            Math.ceil(packet.getPosition().getY() - EntityType.PLAYER.getOffset() * 2) / 2,
            packet.getPosition().getZ(),
            packet.getRotation().getX(),
            packet.getRotation().getY());

        cachedEntity.moveAbsolute(packet.getPosition(), packet.getRotation());

        session.sendRemotePacket(playerPositionRotationPacket);
    }
}
