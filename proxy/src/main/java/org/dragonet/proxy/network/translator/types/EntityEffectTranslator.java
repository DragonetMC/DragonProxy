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
package org.dragonet.proxy.network.translator.types;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class EntityEffectTranslator {
    // Java to Bedrock effect map
    private static Map<Effect, BedrockEffect> effectMap = new HashMap<>();

    static {
        effectMap.put(Effect.FASTER_MOVEMENT, BedrockEffect.SPEED);
        effectMap.put(Effect.SLOWER_MOVEMENT, BedrockEffect.SLOWNESS);
        effectMap.put(Effect.FASTER_DIG, BedrockEffect.HASTE);
        effectMap.put(Effect.SLOWER_DIG, BedrockEffect.MINING_FATIGUE);
        effectMap.put(Effect.INCREASE_DAMAGE, BedrockEffect.STRENGTH);
        effectMap.put(Effect.HEAL, BedrockEffect.INSTANT_HEALTH);
        effectMap.put(Effect.HARM, BedrockEffect.INSTANT_DAMAGE);
        effectMap.put(Effect.JUMP, BedrockEffect.JUMP_BOOST);
        effectMap.put(Effect.CONFUSION, BedrockEffect.NAUSEA);
        effectMap.put(Effect.REGENERATION, BedrockEffect.REGENERATION);
        effectMap.put(Effect.RESISTANCE, BedrockEffect.RESISTANCE);
        effectMap.put(Effect.FIRE_RESISTANCE, BedrockEffect.FIRE_RESISTANCE);
        effectMap.put(Effect.WATER_BREATHING, BedrockEffect.WATER_BREATHING);
        effectMap.put(Effect.INVISIBILITY, BedrockEffect.INVISIBILITY);
        effectMap.put(Effect.BLINDNESS, BedrockEffect.BLINDNESS);
        effectMap.put(Effect.NIGHT_VISION, BedrockEffect.NIGHT_VISION);
        effectMap.put(Effect.HUNGER, BedrockEffect.HUNGER);
        effectMap.put(Effect.WEAKNESS, BedrockEffect.WEAKNESS);
        effectMap.put(Effect.POISON, BedrockEffect.POISON);
        effectMap.put(Effect.WITHER, BedrockEffect.WITHER);
        effectMap.put(Effect.HEALTH_BOOST, BedrockEffect.HEALTH_BOOST);
        effectMap.put(Effect.ABSORBTION, BedrockEffect.ABSORPTION);
        effectMap.put(Effect.SATURATION, BedrockEffect.SATURATION);
        effectMap.put(Effect.LEVITATION, BedrockEffect.LEVITATION);
        effectMap.put(Effect.CONDUIT_POWER, BedrockEffect.CONDUIT_POWER);
    }

    /**
     * This method translates a Java effect to a Bedrock effect.
     */
    public static BedrockEffect translateToBedrock(Effect javaEffect) {
        if(effectMap.containsKey(javaEffect)) {
            return effectMap.get(javaEffect);
        }
        return null;
    }

    /**
     * These effects are specified in a specific order and must stay in that order.
     * This should be removed when and if an Effect enum is added into the protocol library.
     */
    public enum BedrockEffect {
        SPEED, SLOWNESS, HASTE, MINING_FATIGUE, STRENGTH, INSTANT_HEALTH, INSTANT_DAMAGE, JUMP_BOOST,
        NAUSEA, REGENERATION, RESISTANCE, FIRE_RESISTANCE, WATER_BREATHING, INVISIBILITY, BLINDNESS,
        NIGHT_VISION, HUNGER, WEAKNESS, POISON, WITHER, HEALTH_BOOST, ABSORPTION, SATURATION, LEVITATION,
        FATAL_POISON, CONDUIT_POWER
    }
}
