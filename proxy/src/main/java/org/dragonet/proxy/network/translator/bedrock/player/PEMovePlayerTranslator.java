/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.bedrock.player;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
public class PEMovePlayerTranslator implements PacketTranslator<MovePlayerPacket> {
    public static final PEMovePlayerTranslator INSTANCE = new PEMovePlayerTranslator();

    @Override
    public void translate(ProxySession session, MovePlayerPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getById(packet.getRuntimeEntityId());
        if(cachedEntity == null) {
            log.warn("Cached entity is null in MovePlayerTranslator");
            return;
        }
        log.warn("Cached entity is NOT null");

        ClientPlayerPositionPacket playerPositionPacket = new ClientPlayerPositionPacket(
            packet.isOnGround(),
            packet.getPosition().getX(),
            packet.getPosition().getY(),
            packet.getPosition().getZ()
        );

        session.getDownstream().getSession().send(playerPositionPacket);
    }
}
