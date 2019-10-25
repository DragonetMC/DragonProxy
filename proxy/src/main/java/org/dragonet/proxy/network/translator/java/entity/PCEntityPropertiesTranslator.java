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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import com.nukkitx.protocol.bedrock.data.Attribute;
import com.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockAttributeType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.types.AttributeTypeTranslator;

import java.util.ArrayList;

@Log4j2
@PCPacketTranslator(packetClass = ServerEntityPropertiesPacket.class)
public class PCEntityPropertiesTranslator extends PacketTranslator<ServerEntityPropertiesPacket> {
    public static final PCEntityPropertiesTranslator INSTANCE = new PCEntityPropertiesTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityPropertiesPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            log.trace("(debug) EntityProperties: Cached entity is null");
            return;
        }

        for(com.github.steveice10.mc.protocol.data.game.entity.attribute.Attribute attribute : packet.getAttributes()) {
            BedrockAttributeType bedrockAttribute = AttributeTypeTranslator.translateToBedrock(attribute.getType());
            if(bedrockAttribute == null) {
                log.trace("Cannot translate attribute: " + attribute.getType().name());
                return;
            }

            log.trace("Translating attribute: " + bedrockAttribute.getIdentifier() + " with value " + attribute.getValue());
            if(cachedEntity.getAttributes().containsKey(bedrockAttribute)) {
                cachedEntity.getAttributes().replace(bedrockAttribute, new Attribute(bedrockAttribute.getIdentifier(), bedrockAttribute.getMinimumValue(), bedrockAttribute.getMaximumValue(), (float) attribute.getValue(), bedrockAttribute.getDefaultValue()));
            } else {
                cachedEntity.getAttributes().remove(bedrockAttribute); // TODO: is this correct?
            }
        }

        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setRuntimeEntityId(cachedEntity.getProxyEid());
        updateAttributesPacket.setAttributes(new ArrayList<>(cachedEntity.getAttributes().values()));

        session.sendPacket(updateAttributesPacket);
    }
}
