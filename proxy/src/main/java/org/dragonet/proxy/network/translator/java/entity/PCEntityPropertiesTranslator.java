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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.entity;

import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import com.nukkitx.protocol.bedrock.data.Attribute;
import com.nukkitx.protocol.bedrock.packet.AnimatePacket;
import com.nukkitx.protocol.bedrock.packet.PlayerActionPacket;
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket;
import com.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockAttribute;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.AttributeTypeTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PCEntityPropertiesTranslator implements PacketTranslator<ServerEntityPropertiesPacket> {
    public static final PCEntityPropertiesTranslator INSTANCE = new PCEntityPropertiesTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityPropertiesPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            log.trace("(debug) EntityProperties: Cached entity is null");
            return;
        }

        List<Attribute> attributes = new ArrayList<>();

        for(com.github.steveice10.mc.protocol.data.game.entity.attribute.Attribute attribute : packet.getAttributes()) {
            BedrockAttribute bedrockAttribute = AttributeTypeTranslator.translateToBedrock(attribute.getType());
            if(bedrockAttribute == null) {
                log.trace("Cannot translate attribute: " + attribute.getType().name());
                return;
            }

            log.trace("Translating attribute: " + bedrockAttribute.getIdentifier() + " with value " + attribute.getValue());
            attributes.add(new Attribute(bedrockAttribute.getIdentifier(), bedrockAttribute.getMinimumValue(), bedrockAttribute.getMaximumValue(), (float) attribute.getValue(), bedrockAttribute.getDefaultValue()));
        }

        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setRuntimeEntityId(cachedEntity.getProxyEid());
        updateAttributesPacket.setAttributes(attributes);

        session.getBedrockSession().sendPacket(updateAttributesPacket);
    }
}
