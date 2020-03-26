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
package org.dragonet.proxy.network.translator.types;

import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockAttributeType;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class AttributeTypeTranslator {
    // Java to Bedrock attribute map
    private static Map<AttributeType, BedrockAttributeType> attributeMap = new HashMap<>();

    static {
        // TODO: finish these
        attributeMap.put(AttributeType.GENERIC_MOVEMENT_SPEED, BedrockAttributeType.MOVEMENT_SPEED);
        //attributeMap.put(AttributeType.GENERIC_MAX_HEALTH, BedrockAttributeType.MAX_HEALTH);
    }

    /**
     * This method translates a Java attribute to a Bedrock attribute.
     */
    public static BedrockAttributeType translateToBedrock(AttributeType attribute) {
        if(attributeMap.containsKey(attribute)) {
            return attributeMap.get(attribute);
        }
        return null;
    }
}
