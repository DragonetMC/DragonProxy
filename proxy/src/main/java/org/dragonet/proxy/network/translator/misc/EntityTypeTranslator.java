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
package org.dragonet.proxy.network.translator.misc;

import com.github.steveice10.mc.protocol.data.game.entity.type.GlobalEntityType;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockEntityType;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class EntityTypeTranslator {
    // Java to Bedrock entity type map
    private static Map<MobType, BedrockEntityType> entityMap = new HashMap<>();
    private static Map<GlobalEntityType, BedrockEntityType> globalEntityMap = new HashMap<>();
    private static Map<ObjectType, BedrockEntityType> objectMap = new HashMap<>();

    static {
        // Normal entities
        entityMap.put(MobType.CHICKEN, BedrockEntityType.CHICKEN);
        entityMap.put(MobType.COW, BedrockEntityType.COW);
        entityMap.put(MobType.PIG, BedrockEntityType.PIG);
        entityMap.put(MobType.SHEEP, BedrockEntityType.SHEEP);
        entityMap.put(MobType.WOLF, BedrockEntityType.WOLF);
        entityMap.put(MobType.VILLAGER, BedrockEntityType.VILLAGER);
        entityMap.put(MobType.MOOSHROOM, BedrockEntityType.MOOSHROOM);
        entityMap.put(MobType.SQUID, BedrockEntityType.SQUID);
        entityMap.put(MobType.RABBIT, BedrockEntityType.RABBIT);
        entityMap.put(MobType.BAT, BedrockEntityType.BAT);
        entityMap.put(MobType.IRON_GOLEM, BedrockEntityType.IRON_GOLEM);
        entityMap.put(MobType.SNOW_GOLEM, BedrockEntityType.SNOW_GOLEM);
        entityMap.put(MobType.OCELOT, BedrockEntityType.OCELOT);
        entityMap.put(MobType.HORSE, BedrockEntityType.HORSE);
        entityMap.put(MobType.DONKEY, BedrockEntityType.DONKEY);
        entityMap.put(MobType.MULE, BedrockEntityType.MULE);
        entityMap.put(MobType.SKELETON_HORSE, BedrockEntityType.SKELETON_HORSE);
        entityMap.put(MobType.ZOMBIE_HORSE, BedrockEntityType.ZOMBIE_HORSE);
        entityMap.put(MobType.POLAR_BEAR, BedrockEntityType.POLAR_BEAR);
        entityMap.put(MobType.LLAMA, BedrockEntityType.LLAMA);
        entityMap.put(MobType.PARROT, BedrockEntityType.PARROT);
        entityMap.put(MobType.DOLPHIN, BedrockEntityType.DOLPHIN);
        entityMap.put(MobType.ZOMBIE, BedrockEntityType.ZOMBIE);
        entityMap.put(MobType.CREEPER, BedrockEntityType.CREEPER);
        entityMap.put(MobType.SKELETON, BedrockEntityType.SKELETON);
        entityMap.put(MobType.SPIDER, BedrockEntityType.SPIDER);
        entityMap.put(MobType.ZOMBIE_PIGMAN, BedrockEntityType.ZOMBIE_PIGMAN);
        entityMap.put(MobType.SLIME, BedrockEntityType.SLIME);
        entityMap.put(MobType.ENDERMAN, BedrockEntityType.ENDERMAN);
        entityMap.put(MobType.SILVERFISH, BedrockEntityType.SILVERFISH);
        entityMap.put(MobType.CAVE_SPIDER, BedrockEntityType.CAVE_SPIDER);
        entityMap.put(MobType.GHAST, BedrockEntityType.GHAST);
        entityMap.put(MobType.MAGMA_CUBE, BedrockEntityType.MAGMA_CUBE);
        entityMap.put(MobType.BLAZE, BedrockEntityType.BLAZE);
        entityMap.put(MobType.ZOMBIE_VILLAGER, BedrockEntityType.ZOMBIE_VILLAGER);
        entityMap.put(MobType.WITCH, BedrockEntityType.WITCH);
        entityMap.put(MobType.STRAY, BedrockEntityType.STRAY);
        entityMap.put(MobType.HUSK, BedrockEntityType.HUSK);
        entityMap.put(MobType.WITHER_SKELETON, BedrockEntityType.WITHER_SKELETON);
        entityMap.put(MobType.GUARDIAN, BedrockEntityType.GUARDIAN);
        entityMap.put(MobType.ELDER_GUARDIAN, BedrockEntityType.ELDER_GUARDIAN);
        //entityMap.put(MobType., BedrockEntityType.NPC);
        entityMap.put(MobType.WITHER, BedrockEntityType.WITHER);
        entityMap.put(MobType.ENDER_DRAGON, BedrockEntityType.ENDER_DRAGON);
        entityMap.put(MobType.SHULKER, BedrockEntityType.SHULKER);
        entityMap.put(MobType.ENDERMITE, BedrockEntityType.ENDERMITE);
        //entityMap.put(MobType.AGENT, BedrockEntityType.AGENT);
        entityMap.put(MobType.VINDICATOR, BedrockEntityType.VINDICATOR);
        entityMap.put(MobType.PILLAGER, BedrockEntityType.PILLAGER);
        entityMap.put(MobType.WANDERING_TRADER, BedrockEntityType.WANDERING_TRADER);
        entityMap.put(MobType.PHANTOM, BedrockEntityType.PHANTOM);
        entityMap.put(MobType.RAVAGER, BedrockEntityType.RAVAGER);
        entityMap.put(MobType.PANDA, BedrockEntityType.PANDA);
        entityMap.put(MobType.PAINTING, BedrockEntityType.PAINTING);

        entityMap.put(MobType.ARMOR_STAND, BedrockEntityType.ARMOR_STAND);
        //entityMap.put(MobType.TRIPOD_CAMERA, BedrockEntityType.TRIPOD_CAMERA);
        entityMap.put(MobType.PLAYER, BedrockEntityType.PLAYER);
        entityMap.put(MobType.ITEM, BedrockEntityType.ITEM);
        entityMap.put(MobType.PRIMED_TNT, BedrockEntityType.PRIMED_TNT);
        entityMap.put(MobType.FALLING_BLOCK, BedrockEntityType.FALLING_BLOCK);
        //entityMap.put(MobType.MOVING_BLOCK, BedrockEntityType.MOVING_BLOCK);
        entityMap.put(MobType.THROWN_EXP_BOTTLE, BedrockEntityType.EXPERIENCE_BOTTLE);
        entityMap.put(MobType.EXPERIENCE_ORB, BedrockEntityType.EXPERIENCE_ORB);
        entityMap.put(MobType.EYE_OF_ENDER, BedrockEntityType.EYE_OF_ENDER);
        entityMap.put(MobType.END_CRYSTAL, BedrockEntityType.ENDER_CRYSTAL);
        entityMap.put(MobType.FIREWORK_ROCKET, BedrockEntityType.FIREWORK_ROCKET);
        entityMap.put(MobType.TRIDENT, BedrockEntityType.TRIDENT);

        entityMap.put(MobType.SHULKER_BULLET, BedrockEntityType.SHULKER_BULLET);
        entityMap.put(MobType.FISHING_BOBBER, BedrockEntityType.FISHING_HOOK);
        //entityMap.put(MobType.CHALKBOARD, BedrockEntityType.CHALKBOARD);
        entityMap.put(MobType.DRAGON_FIREBALL, BedrockEntityType.DRAGON_FIREBALL);
        entityMap.put(MobType.ARROW, BedrockEntityType.ARROW);
        entityMap.put(MobType.SNOWBALL, BedrockEntityType.SNOWBALL);
        entityMap.put(MobType.THROWN_EGG, BedrockEntityType.EGG);
        entityMap.put(MobType.MINECART, BedrockEntityType.MINECART);
        entityMap.put(MobType.FIREBALL, BedrockEntityType.LARGE_FIREBALL);
        entityMap.put(MobType.THROWN_POTION, BedrockEntityType.SPLASH_POTION);
        entityMap.put(MobType.THROWN_ENDERPEARL, BedrockEntityType.ENDER_PEARL);
        entityMap.put(MobType.LEASH_KNOT, BedrockEntityType.LEASH_KNOT);
        entityMap.put(MobType.WITHER_SKULL, BedrockEntityType.WITHER_SKULL);
        entityMap.put(MobType.BOAT, BedrockEntityType.BOAT);
        //entityMap.put(MobType.WITHER_SKULL_DANGEROUS, BedrockEntityType.WITHER_SKULL_DANGEROUS);
        entityMap.put(MobType.SMALL_FIREBALL, BedrockEntityType.SMALL_FIREBALL);
        entityMap.put(MobType.AREA_EFFECT_CLOUD, BedrockEntityType.AREA_EFFECT_CLOUD);
        entityMap.put(MobType.MINECART_HOPPER, BedrockEntityType.HOPPER_MINECART);
        entityMap.put(MobType.MINECART_TNT, BedrockEntityType.TNT_MINECART);
        entityMap.put(MobType.MINECART_CHEST, BedrockEntityType.CHEST_MINECART);
        entityMap.put(MobType.TRADER_LLAMA, BedrockEntityType.LLAMA); // todo: check

        entityMap.put(MobType.MINECART_COMMAND_BLOCK, BedrockEntityType.COMMAND_BLOCK_MINECART);
        //entityMap.put(MobType.LINGERING_POTION, BedrockEntityType.LINGERING_POTION);
        entityMap.put(MobType.LLAMA_SPIT, BedrockEntityType.LLAMA_SPIT);
        entityMap.put(MobType.EVOKER_FANGS, BedrockEntityType.EVOKER_FANGS);
        entityMap.put(MobType.EVOKER, BedrockEntityType.EVOKER);
        entityMap.put(MobType.VEX, BedrockEntityType.VEX);
        //entityMap.put(MobType.ICE_BOMB, BedrockEntityType.ICE_BOMB);
        //entityMap.put(MobType.BALLOON, BedrockEntityType.BALLOON);
        entityMap.put(MobType.PUFFERFISH, BedrockEntityType.PUFFERFISH);
        entityMap.put(MobType.SALMON, BedrockEntityType.SALMON);
        entityMap.put(MobType.TROPICAL_FISH, BedrockEntityType.TROPICAL_FISH);
        entityMap.put(MobType.COD, BedrockEntityType.COD);
        entityMap.put(MobType.CAT, BedrockEntityType.CAT);
        entityMap.put(MobType.BEE, BedrockEntityType.BEE);
        entityMap.put(MobType.TURTLE, BedrockEntityType.TURTLE);
        entityMap.put(MobType.DROWNED, BedrockEntityType.DROWNED);

        // Giant zombie!
        entityMap.put(MobType.GIANT, BedrockEntityType.ZOMBIE);


        // Global entities
        globalEntityMap.put(GlobalEntityType.LIGHTNING_BOLT, BedrockEntityType.LIGHTNING_BOLT);


        // Objects
        objectMap.put(ObjectType.EVOKER_FANGS, BedrockEntityType.EVOKER_FANGS);
        objectMap.put(ObjectType.AREA_EFFECT_CLOUD, BedrockEntityType.AREA_EFFECT_CLOUD);
        objectMap.put(ObjectType.ARROW, BedrockEntityType.ARROW);
        objectMap.put(ObjectType.ARMOR_STAND, BedrockEntityType.ARMOR_STAND);
        objectMap.put(ObjectType.BOAT, BedrockEntityType.BOAT);
        objectMap.put(ObjectType.DRAGON_FIREBALL, BedrockEntityType.DRAGON_FIREBALL);
        objectMap.put(ObjectType.END_CRYSTAL, BedrockEntityType.ENDER_CRYSTAL);
        objectMap.put(ObjectType.EXPERIENCE_ORB, BedrockEntityType.EXPERIENCE_ORB);
        objectMap.put(ObjectType.EYE_OF_ENDER, BedrockEntityType.EYE_OF_ENDER);
        objectMap.put(ObjectType.FALLING_BLOCK, BedrockEntityType.FALLING_BLOCK);
        objectMap.put(ObjectType.FIREWORK_ROCKET, BedrockEntityType.FIREWORK_ROCKET);
        objectMap.put(ObjectType.ITEM, BedrockEntityType.ITEM);
        //objectMap.put(ObjectType.ITEM_FRAME, BedrockEntityType.ITEM_FRAME);
        //objectMap.put(ObjectType.FIREBALL, BedrockEntityType.FIREBALL);
        objectMap.put(ObjectType.LEASH_KNOT, BedrockEntityType.LEASH_KNOT);
        objectMap.put(ObjectType.LLAMA_SPIT, BedrockEntityType.LLAMA_SPIT);
        objectMap.put(ObjectType.MINECART, BedrockEntityType.MINECART);
        objectMap.put(ObjectType.CHEST_MINECART, BedrockEntityType.CHEST_MINECART);
        objectMap.put(ObjectType.COMMAND_BLOCK_MINECART, BedrockEntityType.COMMAND_BLOCK_MINECART);
        //objectMap.put(ObjectType.FURNACE_MINECART, BedrockEntityType.FURNACE_MINECART);
        objectMap.put(ObjectType.HOPPER_MINECART, BedrockEntityType.HOPPER_MINECART);
        //objectMap.put(ObjectType.SPAWNER_MINECART, BedrockEntityType.SPAWNER_MINECART);
        objectMap.put(ObjectType.TNT_MINECART, BedrockEntityType.TNT_MINECART);
        objectMap.put(ObjectType.TNT, BedrockEntityType.PRIMED_TNT);
        objectMap.put(ObjectType.SMALL_FIREBALL, BedrockEntityType.SMALL_FIREBALL);
        objectMap.put(ObjectType.SNOWBALL, BedrockEntityType.SNOWBALL);
        //objectMap.put(ObjectType.SPECTRAL_ARROW, BedrockEntityType.SPECTRAL_ARROW);
        objectMap.put(ObjectType.SHULKER_BULLET, BedrockEntityType.SHULKER_BULLET);
        objectMap.put(ObjectType.EGG, BedrockEntityType.EGG);
        objectMap.put(ObjectType.ENDER_PEARL, BedrockEntityType.ENDER_PEARL);
        objectMap.put(ObjectType.EXPERIENCE_BOTTLE, BedrockEntityType.EXPERIENCE_BOTTLE);
        objectMap.put(ObjectType.POTION, BedrockEntityType.SPLASH_POTION);
        objectMap.put(ObjectType.TRIDENT, BedrockEntityType.TRIDENT);
        objectMap.put(ObjectType.WITHER_SKULL, BedrockEntityType.WITHER_SKULL);
        objectMap.put(ObjectType.FISHING_BOBBER, BedrockEntityType.FISHING_HOOK);

    }

    /**
     * This method translates a Java mob type to a Bedrock entity type.
     */
    public static BedrockEntityType translateToBedrock(MobType mobType) {
        if(entityMap.containsKey(mobType)) {
            return entityMap.get(mobType);
        }
        return null;
    }

    /**
     * This method translates a Java global entity type to a Bedrock entity type.
     * The only current global entity is lightning.
     */
    public static BedrockEntityType translateToBedrock(GlobalEntityType globalEntityType) {
        if(globalEntityMap.containsKey(globalEntityType)) {
            return globalEntityMap.get(globalEntityType);
        }
        return null;
    }

    public static BedrockEntityType translateToBedrock(ObjectType objectType) {
        if(objectMap.containsKey(objectType)) {
            return objectMap.get(objectType);
        }
        return null;
    }
}
