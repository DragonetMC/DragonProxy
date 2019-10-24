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

import com.github.steveice10.mc.protocol.data.game.entity.type.GlobalEntityType;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class EntityTypeTranslator {
    // Java to Bedrock entity type map
    private static Map<MobType, EntityType> entityMap = new HashMap<>();
    private static Map<GlobalEntityType, EntityType> globalEntityMap = new HashMap<>();
    private static Map<ObjectType, EntityType> objectMap = new HashMap<>();

    static {
        // Normal entities
        entityMap.put(MobType.CHICKEN, EntityType.CHICKEN);
        entityMap.put(MobType.COW, EntityType.COW);
        entityMap.put(MobType.PIG, EntityType.PIG);
        entityMap.put(MobType.SHEEP, EntityType.SHEEP);
        entityMap.put(MobType.WOLF, EntityType.WOLF);
        entityMap.put(MobType.VILLAGER, EntityType.VILLAGER);
        entityMap.put(MobType.MOOSHROOM, EntityType.MOOSHROOM);
        entityMap.put(MobType.SQUID, EntityType.SQUID);
        entityMap.put(MobType.RABBIT, EntityType.RABBIT);
        entityMap.put(MobType.BAT, EntityType.BAT);
        entityMap.put(MobType.IRON_GOLEM, EntityType.IRON_GOLEM);
        entityMap.put(MobType.SNOW_GOLEM, EntityType.SNOW_GOLEM);
        entityMap.put(MobType.OCELOT, EntityType.OCELOT);
        entityMap.put(MobType.HORSE, EntityType.HORSE);
        entityMap.put(MobType.DONKEY, EntityType.DONKEY);
        entityMap.put(MobType.MULE, EntityType.MULE);
        entityMap.put(MobType.SKELETON_HORSE, EntityType.SKELETON_HORSE);
        entityMap.put(MobType.ZOMBIE_HORSE, EntityType.ZOMBIE_HORSE);
        entityMap.put(MobType.POLAR_BEAR, EntityType.POLAR_BEAR);
        entityMap.put(MobType.LLAMA, EntityType.LLAMA);
        entityMap.put(MobType.PARROT, EntityType.PARROT);
        entityMap.put(MobType.DOLPHIN, EntityType.DOLPHIN);
        entityMap.put(MobType.ZOMBIE, EntityType.ZOMBIE);
        entityMap.put(MobType.CREEPER, EntityType.CREEPER);
        entityMap.put(MobType.SKELETON, EntityType.SKELETON);
        entityMap.put(MobType.SPIDER, EntityType.SPIDER);
        entityMap.put(MobType.ZOMBIE_PIGMAN, EntityType.ZOMBIE_PIGMAN);
        entityMap.put(MobType.SLIME, EntityType.SLIME);
        entityMap.put(MobType.ENDERMAN, EntityType.ENDERMAN);
        entityMap.put(MobType.SILVERFISH, EntityType.SILVERFISH);
        entityMap.put(MobType.CAVE_SPIDER, EntityType.CAVE_SPIDER);
        entityMap.put(MobType.GHAST, EntityType.GHAST);
        entityMap.put(MobType.MAGMA_CUBE, EntityType.MAGMA_CUBE);
        entityMap.put(MobType.BLAZE, EntityType.BLAZE);
        entityMap.put(MobType.ZOMBIE_VILLAGER, EntityType.ZOMBIE_VILLAGER);
        entityMap.put(MobType.WITCH, EntityType.WITCH);
        entityMap.put(MobType.STRAY, EntityType.STRAY);
        entityMap.put(MobType.HUSK, EntityType.HUSK);
        entityMap.put(MobType.WITHER_SKELETON, EntityType.WITHER_SKELETON);
        entityMap.put(MobType.GUARDIAN, EntityType.GUARDIAN);
        entityMap.put(MobType.ELDER_GUARDIAN, EntityType.ELDER_GUARDIAN);
        //entityMap.put(MobType., EntityType.NPC);
        entityMap.put(MobType.WITHER, EntityType.WITHER);
        entityMap.put(MobType.ENDER_DRAGON, EntityType.ENDER_DRAGON);
        entityMap.put(MobType.SHULKER, EntityType.SHULKER);
        entityMap.put(MobType.ENDERMITE, EntityType.ENDERMITE);
        //entityMap.put(MobType.AGENT, EntityType.AGENT);
        entityMap.put(MobType.VINDICATOR, EntityType.VINDICATOR);
        entityMap.put(MobType.PILLAGER, EntityType.PILLAGER);
        entityMap.put(MobType.WANDERING_TRADER, EntityType.WANDERING_TRADER);
        entityMap.put(MobType.PHANTOM, EntityType.PHANTOM);
        entityMap.put(MobType.RAVAGER, EntityType.RAVAGER);

        entityMap.put(MobType.ARMOR_STAND, EntityType.ARMOR_STAND);
        //entityMap.put(MobType.TRIPOD_CAMERA, EntityType.TRIPOD_CAMERA);
        entityMap.put(MobType.PLAYER, EntityType.PLAYER);
        entityMap.put(MobType.ITEM, EntityType.ITEM);
        entityMap.put(MobType.PRIMED_TNT, EntityType.PRIMED_TNT);
        entityMap.put(MobType.FALLING_BLOCK, EntityType.FALLING_BLOCK);
        //entityMap.put(MobType.MOVING_BLOCK, EntityType.MOVING_BLOCK);
        entityMap.put(MobType.THROWN_EXP_BOTTLE, EntityType.EXPERIENCE_BOTTLE);
        entityMap.put(MobType.EXPERIENCE_ORB, EntityType.EXPERIENCE_ORB);
        entityMap.put(MobType.EYE_OF_ENDER, EntityType.EYE_OF_ENDER);
        entityMap.put(MobType.END_CRYSTAL, EntityType.ENDER_CRYSTAL);
        entityMap.put(MobType.FIREWORK_ROCKET, EntityType.FIREWORK_ROCKET);
        entityMap.put(MobType.TRIDENT, EntityType.TRIDENT);

        entityMap.put(MobType.SHULKER_BULLET, EntityType.SHULKER_BULLET);
        entityMap.put(MobType.FISHING_BOBBER, EntityType.FISHING_HOOK);
        //entityMap.put(MobType.CHALKBOARD, EntityType.CHALKBOARD);
        entityMap.put(MobType.DRAGON_FIREBALL, EntityType.DRAGON_FIREBALL);
        entityMap.put(MobType.ARROW, EntityType.ARROW);
        entityMap.put(MobType.SNOWBALL, EntityType.SNOWBALL);
        entityMap.put(MobType.THROWN_EGG, EntityType.EGG);
        //entityMap.put(MobType.PAINING, EntityType.PAINTING);
        entityMap.put(MobType.MINECART, EntityType.MINECART);
        entityMap.put(MobType.FIREBALL, EntityType.LARGE_FIREBALL);
        entityMap.put(MobType.THROWN_POTION, EntityType.SPLASH_POTION);
        entityMap.put(MobType.THROWN_ENDERPEARL, EntityType.ENDER_PEARL);
        entityMap.put(MobType.LEASH_KNOT, EntityType.LEASH_KNOT);
        entityMap.put(MobType.WITHER_SKULL, EntityType.WITHER_SKULL);
        entityMap.put(MobType.BOAT, EntityType.BOAT);
        //entityMap.put(MobType.WITHER_SKULL_DANGEROUS, EntityType.WITHER_SKULL_DANGEROUS);
        entityMap.put(MobType.SMALL_FIREBALL, EntityType.SMALL_FIREBALL);
        entityMap.put(MobType.AREA_EFFECT_CLOUD, EntityType.AREA_EFFECT_CLOUD);
        entityMap.put(MobType.MINECART_HOPPER, EntityType.HOPPER_MINECART);
        entityMap.put(MobType.MINECART_TNT, EntityType.TNT_MINECART);
        entityMap.put(MobType.MINECART_CHEST, EntityType.CHEST_MINECART);

        entityMap.put(MobType.MINECART_COMMAND_BLOCK, EntityType.COMMAND_BLOCK_MINECART);
        //entityMap.put(MobType.LINGERING_POTION, EntityType.LINGERING_POTION);
        entityMap.put(MobType.LLAMA_SPIT, EntityType.LLAMA_SPIT);
        entityMap.put(MobType.EVOKER_FANGS, EntityType.EVOKER_FANGS);
        entityMap.put(MobType.EVOKER, EntityType.EVOKER);
        entityMap.put(MobType.VEX, EntityType.VEX);
        //entityMap.put(MobType.ICE_BOMB, EntityType.ICE_BOMB);
        //entityMap.put(MobType.BALLOON, EntityType.BALLOON);
        entityMap.put(MobType.PUFFERFISH, EntityType.PUFFERFISH);
        entityMap.put(MobType.SALMON, EntityType.SALMON);
        entityMap.put(MobType.TROPICAL_FISH, EntityType.TROPICAL_FISH);
        entityMap.put(MobType.COD, EntityType.COD);


        // Global entities
        globalEntityMap.put(GlobalEntityType.LIGHTNING_BOLT, EntityType.LIGHTNING_BOLT);


        // Objects
        objectMap.put(ObjectType.EVOKER_FANGS, EntityType.EVOKER_FANGS);
        objectMap.put(ObjectType.AREA_EFFECT_CLOUD, EntityType.AREA_EFFECT_CLOUD);
        objectMap.put(ObjectType.ARROW, EntityType.ARROW);
        objectMap.put(ObjectType.ARMOR_STAND, EntityType.ARMOR_STAND);
        objectMap.put(ObjectType.BOAT, EntityType.BOAT);
        objectMap.put(ObjectType.DRAGON_FIREBALL, EntityType.DRAGON_FIREBALL);
        objectMap.put(ObjectType.END_CRYSTAL, EntityType.ENDER_CRYSTAL);
        objectMap.put(ObjectType.EXPERIENCE_ORB, EntityType.EXPERIENCE_ORB);
        objectMap.put(ObjectType.EYE_OF_ENDER, EntityType.EYE_OF_ENDER);
        objectMap.put(ObjectType.FALLING_BLOCK, EntityType.FALLING_BLOCK);
        objectMap.put(ObjectType.FIREWORK_ROCKET, EntityType.FIREWORK_ROCKET);
        objectMap.put(ObjectType.ITEM, EntityType.ITEM);
        //objectMap.put(ObjectType.ITEM_FRAME, EntityType.ITEM_FRAME);
        //objectMap.put(ObjectType.FIREBALL, EntityType.FIREBALL);
        objectMap.put(ObjectType.LEASH_KNOT, EntityType.LEASH_KNOT);
        objectMap.put(ObjectType.LLAMA_SPIT, EntityType.LLAMA_SPIT);
        objectMap.put(ObjectType.MINECART, EntityType.MINECART);
        objectMap.put(ObjectType.CHEST_MINECART, EntityType.CHEST_MINECART);
        objectMap.put(ObjectType.COMMAND_BLOCK_MINECART, EntityType.COMMAND_BLOCK_MINECART);
        //objectMap.put(ObjectType.FURNACE_MINECART, EntityType.FURNACE_MINECART);
        objectMap.put(ObjectType.HOPPER_MINECART, EntityType.HOPPER_MINECART);
        //objectMap.put(ObjectType.SPAWNER_MINECART, EntityType.SPAWNER_MINECART);
        objectMap.put(ObjectType.TNT_MINECART, EntityType.TNT_MINECART);
        objectMap.put(ObjectType.TNT, EntityType.PRIMED_TNT);
        objectMap.put(ObjectType.SMALL_FIREBALL, EntityType.SMALL_FIREBALL);
        objectMap.put(ObjectType.SNOWBALL, EntityType.SNOWBALL);
        //objectMap.put(ObjectType.SPECTRAL_ARROW, EntityType.SPECTRAL_ARROW);
        objectMap.put(ObjectType.SHULKER_BULLET, EntityType.SHULKER_BULLET);
        objectMap.put(ObjectType.EGG, EntityType.EGG);
        objectMap.put(ObjectType.ENDER_PEARL, EntityType.ENDER_PEARL);
        objectMap.put(ObjectType.EXPERIENCE_BOTTLE, EntityType.EXPERIENCE_BOTTLE);
        objectMap.put(ObjectType.POTION, EntityType.SPLASH_POTION);
        objectMap.put(ObjectType.TRIDENT, EntityType.TRIDENT);
        objectMap.put(ObjectType.WITHER_SKULL, EntityType.WITHER_SKULL);
        objectMap.put(ObjectType.FISHING_BOBBER, EntityType.FISHING_HOOK);

    }

    /**
     * This method translates a Java mob type to a Bedrock entity type.
     */
    public static EntityType translateToBedrock(MobType mobType) {
        if(entityMap.containsKey(mobType)) {
            return entityMap.get(mobType);
        }
        return null;
    }

    /**
     * This method translates a Java global entity type to a Bedrock entity type.
     * The only current global entity is lightning.
     */
    public static EntityType translateToBedrock(GlobalEntityType globalEntityType) {
        if(globalEntityMap.containsKey(globalEntityType)) {
            return globalEntityMap.get(globalEntityType);
        }
        return null;
    }

    public static EntityType translateToBedrock(ObjectType objectType) {
        if(objectMap.containsKey(objectType)) {
            return objectMap.get(objectType);
        }
        return null;
    }
}
