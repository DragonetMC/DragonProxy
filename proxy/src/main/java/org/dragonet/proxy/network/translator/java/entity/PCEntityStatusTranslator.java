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
import com.nukkitx.protocol.bedrock.packet.EntityEventPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@PacketRegisterInfo(packet = ServerEntityStatusPacket.class)
public class PCEntityStatusTranslator extends PacketTranslator<ServerEntityStatusPacket> {
    private static Object2ObjectMap<EntityStatus, EntityEventType> entityEventMap = new Object2ObjectOpenHashMap<>();

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
        CachedEntity entity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(entity == null) {
            //log.info("(debug) Cached entity is null");
            return;
        }

        switch(packet.getStatus()) {
            case PLAYER_ENABLE_REDUCED_DEBUG:
                ((CachedPlayer) entity).setReducedDebugInfo(session, true);
                return;
            case PLAYER_DISABLE_REDUCED_DEBUG:
                ((CachedPlayer) entity).setReducedDebugInfo(session, false);
                return;
            case PLAYER_OP_PERMISSION_LEVEL_0:
                ((CachedPlayer) entity).setOpPermissionLevel(0);
                ((CachedPlayer) entity).sendAdventureSettings(session);
                return;
            case PLAYER_OP_PERMISSION_LEVEL_1:
                ((CachedPlayer) entity).setOpPermissionLevel(1);
                ((CachedPlayer) entity).sendAdventureSettings(session);
                return;
            case PLAYER_OP_PERMISSION_LEVEL_2:
                ((CachedPlayer) entity).setOpPermissionLevel(2);
                ((CachedPlayer) entity).sendAdventureSettings(session);
                return;
            case PLAYER_OP_PERMISSION_LEVEL_3:
                ((CachedPlayer) entity).setOpPermissionLevel(3);
                ((CachedPlayer) entity).sendAdventureSettings(session);
                return;
            case PLAYER_OP_PERMISSION_LEVEL_4:
                ((CachedPlayer) entity).setOpPermissionLevel(4);
                ((CachedPlayer) entity).sendAdventureSettings(session);
                return;

            case LIVING_BURN:
            case SHEEP_GRAZE_OR_TNT_CART_EXPLODE:
            case SQUID_RESET_ROTATION: // TODO
            case LIVING_TELEPORT: // TODO
                return;
        }

        EntityEventPacket entityEventPacket = new EntityEventPacket();
        entityEventPacket.setRuntimeEntityId(entity.getProxyEid());

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
