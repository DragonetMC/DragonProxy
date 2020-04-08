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
package org.dragonet.proxy.network.translator.java.entity;

import com.github.steveice10.mc.protocol.data.game.entity.EntityStatus;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import com.nukkitx.protocol.bedrock.data.EntityEventType;
import com.nukkitx.protocol.bedrock.data.LevelEventType;
import com.nukkitx.protocol.bedrock.packet.EntityEventPacket;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@PCPacketTranslator(packetClass = ServerEntityStatusPacket.class)
public class PCEntityStatusTranslator extends PacketTranslator<ServerEntityStatusPacket> {
    private static Map<EntityStatus, EntityEventType> entityEventMap = new HashMap<>();

    static {
        entityEventMap.put(EntityStatus.LIVING_HURT, EntityEventType.HURT_ANIMATION);
        entityEventMap.put(EntityStatus.LIVING_HURT_SWEET_BERRY_BUSH, EntityEventType.HURT_ANIMATION);
        entityEventMap.put(EntityStatus.LIVING_HURT_THORNS, EntityEventType.HURT_ANIMATION);
        entityEventMap.put(EntityStatus.LIVING_DEATH, EntityEventType.DEATH_ANIMATION);
        entityEventMap.put(EntityStatus.FIREWORK_EXPLODE, EntityEventType.FIREWORK_PARTICLES);
        entityEventMap.put(EntityStatus.WITCH_EMIT_PARTICLES, EntityEventType.WITCH_SPELL_PARTICLES);
        entityEventMap.put(EntityStatus.WOLF_SHAKE_WATER, EntityEventType.SHAKE_WET);
        entityEventMap.put(EntityStatus.TAMEABLE_TAMING_SUCCEEDED, EntityEventType.TAME_SUCCESS);
        entityEventMap.put(EntityStatus.TAMEABLE_TAMING_FAILED, EntityEventType.TAME_FAIL);
        entityEventMap.put(EntityStatus.OCELOT_TAMING_SUCCEEDED, EntityEventType.TAME_SUCCESS);
        entityEventMap.put(EntityStatus.OCELOT_TAMING_FAILED, EntityEventType.TAME_FAIL);
        entityEventMap.put(EntityStatus.VILLAGER_ANGRY, EntityEventType.VILLAGER_HURT); // TODO: check
    }

    @Override
    public void translate(ProxySession session, ServerEntityStatusPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            //log.info("(debug) Cached entity is null");
            return;
        }

        switch(packet.getStatus()) {
            case LIVING_BURN:
            case SHEEP_GRAZE_OR_TNT_CART_EXPLODE:
            case SQUID_RESET_ROTATION: // TODO
            case LIVING_TELEPORT: // TODO
                return;
        }

        EntityEventPacket entityEventPacket = new EntityEventPacket();
        entityEventPacket.setRuntimeEntityId(cachedEntity.getProxyEid());

        EntityEventType bedrockEvent = entityEventMap.get(packet.getStatus());
        if(bedrockEvent == null) {
            log.info(TextFormat.GRAY + "(debug) Unhandled entity status: " + packet.getStatus().name());
            return;
        }

        entityEventPacket.setType(bedrockEvent);
        entityEventPacket.setData(0);

        session.sendPacket(entityEventPacket);
    }
}
