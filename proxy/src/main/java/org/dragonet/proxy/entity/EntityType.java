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
package org.dragonet.proxy.entity;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;

public enum EntityType {

    CHICKEN(10),
    COW(11),
    PIG(12),
    SHEEP(13),
    WOLF(14),
    VILLAGER(15),
    MOOSHROOM(16),
    SQUID(17),
    RABBIT(18),
    BAT(19),
    IRONGOLEM(20),
    SNOWMAN(21),
    OCELOT(22),
    ZOMBIE(32),
    CREEPER(33),
    SKELETON(34),
    SPIDER(35),
    ZOMBIE_PIGMAN(36),
    SLIME(37),
    ENDERMAN(38),
    SILVERFISH(39),
    CAVE_SPIDER(40),
    GHAST(41),
    MAGMA_CUBE(42),
    BLAZE(43),
    ZOMBIEVILLAGER(44),
    PRIMED_TNT(65),
    FALLING_BLOCK(66),
    POTION(68),
    EXP_ORB(69),
    ARROW(80),
    SNOW_BALL(81);

    @Getter
    private final int peType;

    private EntityType(int peType) {
        this.peType = peType;
    }

    private final static Map<Integer, EntityType> PC_TO_PE = new HashMap<>();

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

    public static EntityType convertToPE(MobType pcType) {
        return convertToPE(MagicValues.value(Integer.class, pcType));
    }

    public static EntityType convertToPE(int pcType) {
        if (!PC_TO_PE.containsKey(pcType)) {
            return null;
        }
        return PC_TO_PE.get(pcType);
    }
}
