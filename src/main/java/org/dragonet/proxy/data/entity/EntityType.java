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
package org.dragonet.proxy.data.entity;

import java.util.HashMap;
import java.util.Map;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;

public enum EntityType {
        NONE(0),

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
        HORSE(23),
        DONKEY(24),
        MULE(25),
        SKELETON_HORSE(26),
        ZOMBIE_HORSE(27),
        POLAR_BEAR(28),
        LLAMA(29),
        PARROT(30),

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
        NPC(51),
        LEARN_TO_CODE_MASCOTE(56),
        ARMOR_STAND(61),
        TRIPOD_CAMERA(62),
        PLAYER(63),
        ITEM(64),
        PRIMED_TNT(65),
        FALLING_BLOCK(66),
        MOVING_BLOCK(67),
        POTION(68),
        EXP_ORB(69),
        ENDER_EYE(70),
        ENDER_CRYSTAL(71),
        FIREWORKS_ROCKET(72),

        SHULKER_BULLET(76),
        FISHING_HOOK(77),
        CHALKBOARD(78),
        DRAGON_FIREBALL(79),
        ARROW(80),
        SNOW_BALL(81),
        EGG(82),
        PAINTING(83),
        MINECART(84),
        LARGE_FIREBALL(85),
        SPLASH_POTION(86),
        ENDER_PEARL(87),
        LEASH_KNOT(88),
        WITHER_SKULL(89),
        BOAT(90),
        WITHER_SKULL_DANGEROUS(91),

        LIGHTNING_BOLT(93),
        SMALL_FIREBALL(94),
        AREA_EFFECT_CLOUD(95),
        HOPPER_MINECART(96),
        TNT_MINECART(97),
        CHEST_MINECART(98),
        COMMAND_BLOCK_MINECART(100),
        LINGERING_POTION(101),
        LLAMA_SPIT(102),
        EVOCATION_FANG(103),
        EVOCATION_ILLAGER(104),
        VEX(105);

	private static final Map<Integer, EntityType> PC_TO_PE = new HashMap<>();
	private final int peType;

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

	EntityType(int peType) {
		this.peType = peType;
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
}
