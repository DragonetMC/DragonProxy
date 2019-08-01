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
        effectMap.put(Effect.SPEED, BedrockEffect.SPEED);
        effectMap.put(Effect.SLOWNESS, BedrockEffect.SLOWNESS);
        effectMap.put(Effect.DIG_SPEED, BedrockEffect.HASTE);
        effectMap.put(Effect.DIG_SLOWNESS, BedrockEffect.MINING_FATIGUE);
        effectMap.put(Effect.DAMAGE_BOOST, BedrockEffect.STRENGTH);
        effectMap.put(Effect.HEAL, BedrockEffect.INSTANT_HEALTH);
        effectMap.put(Effect.DAMAGE, BedrockEffect.INSTANT_DAMAGE);
        effectMap.put(Effect.JUMP_BOOST, BedrockEffect.JUMP_BOOST);
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
        effectMap.put(Effect.WITHER_EFFECT, BedrockEffect.WITHER);
        effectMap.put(Effect.HEALTH_BOOST, BedrockEffect.HEALTH_BOOST);
        effectMap.put(Effect.ABSORPTION, BedrockEffect.ABSORPTION);
        effectMap.put(Effect.SATURATION, BedrockEffect.SATURATION);
        effectMap.put(Effect.LEVITATION, BedrockEffect.LEVITATION);

        // TODO: add this when we upgrade to MCProtocolLib 1.14
        //effectMap.put(Effect.CONDUIT_POWER, BedrockEffect.CONDUIT_POWER);
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
