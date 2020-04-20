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

import com.github.steveice10.mc.protocol.data.game.entity.attribute.Attribute;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockAttributeType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@Log4j2
@PacketRegisterInfo(packet = ServerEntityPropertiesPacket.class)
public class PCEntityPropertiesTranslator extends PacketTranslator<ServerEntityPropertiesPacket> {
    private static Object2ObjectMap<AttributeType, BedrockAttributeType> attributeMap = new Object2ObjectOpenHashMap<>();

    static {
        //attributeMap.put(AttributeType.GENERIC_FLYING_SPEED, BedrockAttributeType.MOVEMENT_SPEED); Causes issue with sprinting
        //attributeMap.put(AttributeType.GENERIC_MOVEMENT_SPEED, BedrockAttributeType.MOVEMENT_SPEED); Causes issue with sprinting
        attributeMap.put(AttributeType.GENERIC_ATTACK_DAMAGE, BedrockAttributeType.ATTACK_DAMAGE);
        attributeMap.put(AttributeType.GENERIC_FOLLOW_RANGE, BedrockAttributeType.FOLLOW_RANGE);
        attributeMap.put(AttributeType.GENERIC_KNOCKBACK_RESISTANCE, BedrockAttributeType.KNOCKBACK_RESISTANCE);
    }

    @Override
    public void translate(ProxySession session, ServerEntityPropertiesPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            log.trace("(debug) EntityProperties: Cached entity is null");
            return;
        }

        for(Attribute attribute : packet.getAttributes()) {
            BedrockAttributeType bedrockAttribute = attributeMap.get(attribute.getType());
            if(bedrockAttribute == null) {
                log.trace("Cannot translate attribute: " + attribute.getType().name());
                return;
            }
            // TODO: modifiers
            cachedEntity.getAttributes().put(bedrockAttribute, bedrockAttribute.create((float) attribute.getValue()));
        }

        cachedEntity.sendAttributes(session);
    }
}
