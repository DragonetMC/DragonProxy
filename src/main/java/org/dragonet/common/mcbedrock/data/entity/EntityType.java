/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.common.mcbedrock.data.entity;

import java.util.HashMap;
import java.util.Map;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;

public enum EntityType {
    NONE(0, 0),
    CHICKEN(10, 0),
    COW(11, 0),
    PIG(12, 0),
    SHEEP(13, 0),
    WOLF(14, 0),
    VILLAGER(15, 0),
    MOOSHROOM(16, 0),
    SQUID(17, 0),
    RABBIT(18, 0),
    BAT(19, 0),
    IRON_GOLEM(20, 0),
    SNOWMAN(21, 0),
    OCELOT(22, 0),
    HORSE(23, 1.6f),
    DONKEY(24, 0),
    MULE(25, 0),
    SKELETON_HORSE(26, 0),
    ZOMBIE_HORSE(27, 0),
    POLAR_BEAR(28, 0),
    LLAMA(29, 0),
    PARROT(30, 0),
    //UNKNOWN(31, 0),
    ZOMBIE(32, 0),
    GIANT_ZOMBIE(32, 0),
    CREEPER(33, 0),
    SKELETON(34, 0),
    SPIDER(35, 0),
    ZOMBIE_PIGMAN(36, 0),
    SLIME(37, 0),
    ENDERMAN(38, 0),
    SILVERFISH(39, 0),
    CAVE_SPIDER(40, 0),
    GHAST(41, 0),
    MAGMA_CUBE(42, 0),
    BLAZE(43, 0),
    ZOMBIEVILLAGER(44, 0),
    WITCH(45, 0),
    STRAY(46, 0),
    HUSK(47, 0),
    WITHER_SKELETON(48, 0),
    GUARDIAN(49, 0),
    ELDER_GUARDIAN(50, 0),
    NPC(51, 0),
    WITHER(52, 0),
    ENDER_DRAGON(53, 0),
    SHULKER(54, 0),
    ENDERMITE(55, 0),
    LEARN_TO_CODE_MASCOTE(56, 0),
    VINDICATOR(57, 0),
    //UNKNOWN(58, 0),
    //UNKNOWN(59, 0),
    //UNKNOWN(60, 0),
    ARMOR_STAND(61, 0),
    TRIPOD_CAMERA(62, 0),
    PLAYER(63, 1.6200000047683716f),
    ITEM(64, 0),
    PRIMED_TNT(65, 0),
    FALLING_BLOCK(66, 0),
    MOVING_BLOCK(67, 0),
    POTION(68, 0),
    EXP_ORB(69, 0),
    ENDER_EYE(70, 0),
    ENDER_CRYSTAL(71, 0),
    FIREWORKS_ROCKET(72, 0),
    //UNKNOWN(73, 0),
    //UNKNOWN(74, 0),
    //UNKNOWN(75, 0),
    SHULKER_BULLET(76, 0),
    FISHING_HOOK(77, 0),
    CHALKBOARD(78, 0),
    DRAGON_FIREBALL(79, 0),
    ARROW(80, 0),
    SNOW_BALL(81, 0),
    EGG(82, 0),
    PAINTING(83, 0),
    MINECART(84, 0),
    LARGE_FIREBALL(85, 0),
    SPLASH_POTION(86, 0),
    ENDER_PEARL(87, 0),
    LEASH_KNOT(88, 0),
    WITHER_SKULL(89, 0),
    BOAT(90, 0.4f),
    WITHER_SKULL_DANGEROUS(91, 0),
    //UNKNOWN(92, 0),
    LIGHTNING_BOLT(93, 0),
    SMALL_FIREBALL(94, 0),
    AREA_EFFECT_CLOUD(95, 0),
    HOPPER_MINECART(96, 0),
    TNT_MINECART(97, 0),
    CHEST_MINECART(98, 0),
    COMMAND_BLOCK_MINECART(100, 0),
    LINGERING_POTION(101, 0),
    LLAMA_SPIT(102, 0),
    EVOCATION_FANG(103, 0),
    EVOCATION_ILLAGER(104, 0),
    VEX(105, 0);

    private static final Map<Integer, EntityType> PC_TO_PE = new HashMap<>();
    private final int peType;
    private final float offset;

    static {
        for (EntityType peType : EntityType.values()) {
            try {
                MobType pcType = MobType.valueOf(peType.name());
                int pcTypeId = MagicValues.value(Integer.class, pcType);
                PC_TO_PE.put(pcTypeId, peType);
            } catch (Exception e) {
            }
        }
    }

    EntityType(int peType, float offset) {
        this.peType = peType;
        this.offset = offset;
    }

    public static EntityType convertToPE(MobType pcType) {
        
        return convertToPE(MagicValues.value(Integer.class, pcType));
    }

    public static EntityType convertToPE(int pcType) {
        if (!PC_TO_PE.containsKey(pcType)) {
            return null;
        }
        return PC_TO_PE.get(pcType);
    }

    public int getPeType() {
        return peType;
    }

    // Y offset for spawning (more flexible than utilities/Constants.java)
    public float getOffset() {
        return offset;
    }
}
