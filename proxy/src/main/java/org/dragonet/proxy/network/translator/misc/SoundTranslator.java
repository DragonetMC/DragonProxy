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

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class SoundTranslator {
    // Java to Bedrock sound map
    private static Map<BuiltinSound, String> soundMap = new HashMap<>();

    static {
        soundMap.put(BuiltinSound.AMBIENT_CAVE, null);
        
        soundMap.put(BuiltinSound.BLOCK_ANVIL_BREAK, "dig.stone");
        soundMap.put(BuiltinSound.BLOCK_ANVIL_DESTROY, "random.anvil_break");
        soundMap.put(BuiltinSound.BLOCK_ANVIL_FALL, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_ANVIL_HIT, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_ANVIL_LAND, "random.anvil_land");
        soundMap.put(BuiltinSound.BLOCK_ANVIL_PLACE, "random.anvil_land");
        soundMap.put(BuiltinSound.BLOCK_ANVIL_STEP, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_ANVIL_USE, "random.anvil_use");

        soundMap.put(BuiltinSound.BLOCK_BREWING_STAND_BREW, null);

        soundMap.put(BuiltinSound.BLOCK_CHEST_CLOSE, "random.chestclosed");
        soundMap.put(BuiltinSound.BLOCK_CHEST_LOCKED, "random.door_close");
        soundMap.put(BuiltinSound.BLOCK_CHEST_OPEN, "random.chestopen");

        soundMap.put(BuiltinSound.BLOCK_CHORUS_FLOWER_DEATH, "block.chorusflower.death");
        soundMap.put(BuiltinSound.BLOCK_CHORUS_FLOWER_GROW, "block.chorusflower.grow");

        soundMap.put(BuiltinSound.BLOCK_WOOL_BREAK, "dig.cloth");
        soundMap.put(BuiltinSound.BLOCK_WOOL_FALL, "step.cloth");
        soundMap.put(BuiltinSound.BLOCK_WOOL_HIT, "step.cloth");
        soundMap.put(BuiltinSound.BLOCK_WOOL_PLACE, "dig.cloth");
        soundMap.put(BuiltinSound.BLOCK_WOOL_STEP, "step.cloth");

        soundMap.put(BuiltinSound.BLOCK_COMPARATOR_CLICK, "random.click");

        soundMap.put(BuiltinSound.BLOCK_DISPENSER_DISPENSE, "random.click");
        soundMap.put(BuiltinSound.BLOCK_DISPENSER_FAIL, "random.click");
        soundMap.put(BuiltinSound.BLOCK_DISPENSER_LAUNCH, "random.bow");

        soundMap.put(BuiltinSound.BLOCK_ENCHANTMENT_TABLE_USE, null);

        soundMap.put(BuiltinSound.BLOCK_END_GATEWAY_SPAWN, "random.explode");
        soundMap.put(BuiltinSound.BLOCK_END_PORTAL_SPAWN, null);
        soundMap.put(BuiltinSound.BLOCK_END_PORTAL_FRAME_FILL, null);

        soundMap.put(BuiltinSound.BLOCK_ENDER_CHEST_CLOSE, "random.chestclosed");
        soundMap.put(BuiltinSound.BLOCK_ENDER_CHEST_OPEN, "random.chestopen");

        soundMap.put(BuiltinSound.BLOCK_FENCE_GATE_CLOSE, "random.door_close");
        soundMap.put(BuiltinSound.BLOCK_FENCE_GATE_OPEN, "random.door_open");

        soundMap.put(BuiltinSound.BLOCK_FIRE_AMBIENT, "fire.fire");
        soundMap.put(BuiltinSound.BLOCK_FIRE_EXTINGUISH, "random.fizz");

        soundMap.put(BuiltinSound.BLOCK_FURNACE_FIRE_CRACKLE, "block.furnace.lit");
        soundMap.put(BuiltinSound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, "block.blastfurnace.fire_crackle");

        soundMap.put(BuiltinSound.BLOCK_GLASS_BREAK, "random.glass");
        soundMap.put(BuiltinSound.BLOCK_GLASS_FALL, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_GLASS_HIT, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_GLASS_PLACE, "dig.stone");
        soundMap.put(BuiltinSound.BLOCK_GLASS_STEP, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_GRASS_BREAK, "dig.grass");
        soundMap.put(BuiltinSound.BLOCK_GRASS_FALL, "step.grass");
        soundMap.put(BuiltinSound.BLOCK_GRASS_HIT, "step.grass");
        soundMap.put(BuiltinSound.BLOCK_GRASS_PLACE, "dig.grass");
        soundMap.put(BuiltinSound.BLOCK_GRASS_STEP, "step.grass");

        soundMap.put(BuiltinSound.BLOCK_GRAVEL_BREAK, "dig.gravel");
        soundMap.put(BuiltinSound.BLOCK_GRAVEL_FALL, "step.gravel");
        soundMap.put(BuiltinSound.BLOCK_GRAVEL_HIT, "step.gravel");
        soundMap.put(BuiltinSound.BLOCK_GRAVEL_PLACE, "dig.grass");
        soundMap.put(BuiltinSound.BLOCK_GRAVEL_STEP, "step.gravel");

        soundMap.put(BuiltinSound.BLOCK_IRON_DOOR_CLOSE, "random.door_close");
        soundMap.put(BuiltinSound.BLOCK_IRON_DOOR_OPEN, "random.door_close");
        soundMap.put(BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE, "random.door_close");
        soundMap.put(BuiltinSound.BLOCK_IRON_TRAPDOOR_OPEN, "random.door_open");

        soundMap.put(BuiltinSound.BLOCK_LADDER_BREAK, "dig.wood");
        soundMap.put(BuiltinSound.BLOCK_LADDER_FALL, "step.ladder");
        soundMap.put(BuiltinSound.BLOCK_LADDER_HIT, "step.ladder");
        soundMap.put(BuiltinSound.BLOCK_LADDER_PLACE, "dig.wood");
        soundMap.put(BuiltinSound.BLOCK_LADDER_STEP, "step.ladder");

        soundMap.put(BuiltinSound.BLOCK_LAVA_AMBIENT, "liquid.lava");
        soundMap.put(BuiltinSound.BLOCK_LAVA_EXTINGUISH, "random.fizz");
        soundMap.put(BuiltinSound.BLOCK_LAVA_POP, "liquid.lavapop");

        soundMap.put(BuiltinSound.BLOCK_LEVER_CLICK, "random.click");

        soundMap.put(BuiltinSound.BLOCK_METAL_BREAK, "dig.stone");
        soundMap.put(BuiltinSound.BLOCK_METAL_FALL, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_METAL_HIT, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_METAL_PLACE, "dig.stone");
        soundMap.put(BuiltinSound.BLOCK_METAL_STEP, "step.stone");

        soundMap.put(BuiltinSound.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, "random.click");
        soundMap.put(BuiltinSound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, "random.click");

        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_BASEDRUM, "note.bd");
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_BASS, "note.bassattack");
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_BELL, null);
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_CHIME, null);
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_FLUTE, null);
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_GUITAR, null);
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_HARP, "note.harp");
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_HAT, "note.hat");
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_PLING, "note.pling");
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_SNARE, "note.snare");
        soundMap.put(BuiltinSound.BLOCK_NOTE_BLOCK_XYLOPHONE, null);

        soundMap.put(BuiltinSound.BLOCK_PISTON_CONTRACT, "tile.piston.in");
        soundMap.put(BuiltinSound.BLOCK_PISTON_EXTEND, "tile.piston.out");

        soundMap.put(BuiltinSound.BLOCK_PORTAL_AMBIENT, "portal.portal");
        soundMap.put(BuiltinSound.BLOCK_PORTAL_TRAVEL, "portal.travel");
        soundMap.put(BuiltinSound.BLOCK_PORTAL_TRIGGER, "portal.trigger");

        soundMap.put(BuiltinSound.BLOCK_REDSTONE_TORCH_BURNOUT, "random.fizz");

        soundMap.put(BuiltinSound.BLOCK_SAND_BREAK, "dig.sand");
        soundMap.put(BuiltinSound.BLOCK_SAND_FALL, "step.sand");
        soundMap.put(BuiltinSound.BLOCK_SAND_HIT, "step.sand");
        soundMap.put(BuiltinSound.BLOCK_SAND_PLACE, "dig.sand");
        soundMap.put(BuiltinSound.BLOCK_SAND_STEP, "step.sand");

        soundMap.put(BuiltinSound.BLOCK_SHULKER_BOX_CLOSE, "random.shulkerboxclosed");
        soundMap.put(BuiltinSound.BLOCK_SHULKER_BOX_OPEN, "random.shulkerboxopen");

        soundMap.put(BuiltinSound.BLOCK_SLIME_BLOCK_BREAK, "mob.slime.death");
        soundMap.put(BuiltinSound.BLOCK_SLIME_BLOCK_FALL, "step.slime");
        soundMap.put(BuiltinSound.BLOCK_SLIME_BLOCK_HIT, "step.slime");
        soundMap.put(BuiltinSound.BLOCK_SLIME_BLOCK_PLACE, "mob.slime.jump");
        soundMap.put(BuiltinSound.BLOCK_SLIME_BLOCK_STEP, "step.slime");

        soundMap.put(BuiltinSound.BLOCK_SNOW_BREAK, "dig.snow");
        soundMap.put(BuiltinSound.BLOCK_SNOW_FALL, "step.snow");
        soundMap.put(BuiltinSound.BLOCK_SNOW_HIT, "step.snow");
        soundMap.put(BuiltinSound.BLOCK_SNOW_PLACE, "dig.snow");
        soundMap.put(BuiltinSound.BLOCK_SNOW_STEP, "step.snow");

        soundMap.put(BuiltinSound.BLOCK_STONE_BREAK, "dig.stone");
        soundMap.put(BuiltinSound.BLOCK_STONE_FALL, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_STONE_HIT, "step.stone");
        soundMap.put(BuiltinSound.BLOCK_STONE_PLACE, "dig.stone");
        soundMap.put(BuiltinSound.BLOCK_STONE_STEP, "step.stone");

        soundMap.put(BuiltinSound.BLOCK_STONE_BUTTON_CLICK_OFF, "random.click");
        soundMap.put(BuiltinSound.BLOCK_STONE_BUTTON_CLICK_ON, "random.click");

        soundMap.put(BuiltinSound.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, "random.click");
        soundMap.put(BuiltinSound.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, "random.click");

        soundMap.put(BuiltinSound.BLOCK_TRIPWIRE_ATTACH, "random.click");
        soundMap.put(BuiltinSound.BLOCK_TRIPWIRE_CLICK_OFF, "random.click");
        soundMap.put(BuiltinSound.BLOCK_TRIPWIRE_CLICK_ON, "random.click");
        soundMap.put(BuiltinSound.BLOCK_TRIPWIRE_DETACH, "random.bow");

        soundMap.put(BuiltinSound.BLOCK_WATER_AMBIENT, "liquid.water");

        soundMap.put(BuiltinSound.BLOCK_LILY_PAD_PLACE, null);

        soundMap.put(BuiltinSound.BLOCK_WOOD_BREAK, "dig.wood");
        soundMap.put(BuiltinSound.BLOCK_WOOD_FALL, "step.wood");
        soundMap.put(BuiltinSound.BLOCK_WOOD_HIT, "step.wood");
        soundMap.put(BuiltinSound.BLOCK_WOOD_PLACE, "dig.wood");
        soundMap.put(BuiltinSound.BLOCK_WOOD_STEP, "step.wood");

        soundMap.put(BuiltinSound.BLOCK_WOODEN_BUTTON_CLICK_OFF, "random.click");
        soundMap.put(BuiltinSound.BLOCK_WOODEN_BUTTON_CLICK_ON, "random.click");

        soundMap.put(BuiltinSound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, "random.click");
        soundMap.put(BuiltinSound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, "random.click");

        soundMap.put(BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE, "random.door_close");
        soundMap.put(BuiltinSound.BLOCK_WOODEN_DOOR_OPEN, "random.door_open");

        soundMap.put(BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE, "random.door_close");
        soundMap.put(BuiltinSound.BLOCK_WOODEN_TRAPDOOR_OPEN, "random.door_open");

        soundMap.put(BuiltinSound.ENCHANT_THORNS_HIT, null);

        soundMap.put(BuiltinSound.ENTITY_ARMOR_STAND_BREAK, "mob.armor_stand.break");
        soundMap.put(BuiltinSound.ENTITY_ARMOR_STAND_FALL, "mob.armor_stand.land");
        soundMap.put(BuiltinSound.ENTITY_ARMOR_STAND_HIT, "mob.armor_stand.hit");
        soundMap.put(BuiltinSound.ENTITY_ARMOR_STAND_PLACE, "mob.armor_stand.place");

        soundMap.put(BuiltinSound.ENTITY_ARROW_HIT, "random.bowhit");
        soundMap.put(BuiltinSound.ENTITY_ARROW_HIT_PLAYER, "random.bow");
        soundMap.put(BuiltinSound.ENTITY_ARROW_SHOOT, "random.bow");

        soundMap.put(BuiltinSound.ENTITY_BAT_AMBIENT, "mob.bat.idle");
        soundMap.put(BuiltinSound.ENTITY_BAT_DEATH, "mob.bat.death");
        soundMap.put(BuiltinSound.ENTITY_BAT_HURT, "mob.bat.hurt");
        soundMap.put(BuiltinSound.ENTITY_BAT_LOOP, null);
        soundMap.put(BuiltinSound.ENTITY_BAT_TAKEOFF, "mob.bat.takeoff");

        soundMap.put(BuiltinSound.ENTITY_BLAZE_AMBIENT, "mob.blaze.breathe");
        soundMap.put(BuiltinSound.ENTITY_BLAZE_BURN, "fire.fire");
        soundMap.put(BuiltinSound.ENTITY_BLAZE_DEATH, "mob.blaze.death");
        soundMap.put(BuiltinSound.ENTITY_BLAZE_HURT, "mob.blaze.hit");
        soundMap.put(BuiltinSound.ENTITY_BLAZE_SHOOT, "mob.blaze.shoot");

        soundMap.put(BuiltinSound.ENTITY_BOAT_PADDLE_LAND, null);
        soundMap.put(BuiltinSound.ENTITY_BOAT_PADDLE_WATER, null);

        soundMap.put(BuiltinSound.ENTITY_FISHING_BOBBER_RETRIEVE, null);
        soundMap.put(BuiltinSound.ENTITY_FISHING_BOBBER_SPLASH, "random.splash");
        soundMap.put(BuiltinSound.ENTITY_FISHING_BOBBER_THROW, null);

        soundMap.put(BuiltinSound.ENTITY_CAT_AMBIENT, "mob.cat.meow");
        soundMap.put(BuiltinSound.ENTITY_CAT_STRAY_AMBIENT, "mob.cat.meow");
        soundMap.put(BuiltinSound.ENTITY_CAT_DEATH, "mob.cat.hurt");
        soundMap.put(BuiltinSound.ENTITY_CAT_HISS, "mob.cat.hiss");
        soundMap.put(BuiltinSound.ENTITY_CAT_HURT, "mob.cat.hit");
        soundMap.put(BuiltinSound.ENTITY_CAT_PURR, "mob.cat.purr");
        soundMap.put(BuiltinSound.ENTITY_CAT_PURREOW, "mob.cat.purreow");

        soundMap.put(BuiltinSound.ENTITY_CHICKEN_AMBIENT, "mob.chicken.say");
        soundMap.put(BuiltinSound.ENTITY_CHICKEN_DEATH, "mob.chicken.hurt");
        soundMap.put(BuiltinSound.ENTITY_CHICKEN_EGG, "mob.chicken.plop");
        soundMap.put(BuiltinSound.ENTITY_CHICKEN_HURT, "mob.chicken.hurt");
        soundMap.put(BuiltinSound.ENTITY_CHICKEN_STEP, "mob.chicken.step");

        soundMap.put(BuiltinSound.ENTITY_COW_AMBIENT, "mob.cow.say");
        soundMap.put(BuiltinSound.ENTITY_COW_DEATH, "mob.cow.hurt");
        soundMap.put(BuiltinSound.ENTITY_COW_HURT, "mob.cow.hurt");
        soundMap.put(BuiltinSound.ENTITY_COW_MILK, "mob.cow.milk");
        soundMap.put(BuiltinSound.ENTITY_COW_STEP, "mob.cow.step");

        soundMap.put(BuiltinSound.ENTITY_CREEPER_DEATH, "mob.creeper.death");
        soundMap.put(BuiltinSound.ENTITY_CREEPER_HURT, "mob.creeper.say");
        soundMap.put(BuiltinSound.ENTITY_CREEPER_PRIMED, "random.fuse");

        soundMap.put(BuiltinSound.ENTITY_DONKEY_AMBIENT, "mob.horse.donkey.idle");
        soundMap.put(BuiltinSound.ENTITY_DONKEY_ANGRY, "mob.horse.donkey.angry");
        soundMap.put(BuiltinSound.ENTITY_DONKEY_CHEST, "mob.chicken.plop");
        soundMap.put(BuiltinSound.ENTITY_DONKEY_DEATH, "mob.horse.donkey.death");
        soundMap.put(BuiltinSound.ENTITY_DONKEY_HURT, "mob.horse.donkey.hit");

        soundMap.put(BuiltinSound.ENTITY_EGG_THROW, "random.bow");

        soundMap.put(BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT, "mob.elderguardian.idle");
        soundMap.put(BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND, "mob.guardian.land_idle");
        soundMap.put(BuiltinSound.ENTITY_ELDER_GUARDIAN_CURSE, "mob.elderguardian.curse");
        soundMap.put(BuiltinSound.ENTITY_ELDER_GUARDIAN_DEATH, "mob.elderguardian.death");
        soundMap.put(BuiltinSound.ENTITY_ELDER_GUARDIAN_DEATH_LAND, "mob.guardian.land_death");
        soundMap.put(BuiltinSound.ENTITY_ELDER_GUARDIAN_FLOP, "mob.guardian.flop");
        soundMap.put(BuiltinSound.ENTITY_ELDER_GUARDIAN_HURT, "mob.elderguardian.hit");
        soundMap.put(BuiltinSound.ENTITY_ELDER_GUARDIAN_HURT_LAND, "mob.guardian.land_death");

        soundMap.put(BuiltinSound.ENTITY_ENDER_DRAGON_AMBIENT, "mob.enderdragon.growl");
        soundMap.put(BuiltinSound.ENTITY_ENDER_DRAGON_DEATH, "mob.enderdragon.death");
        soundMap.put(BuiltinSound.ENTITY_ENDER_DRAGON_FLAP, "mob.enderdragon.flap");
        soundMap.put(BuiltinSound.ENTITY_ENDER_DRAGON_GROWL, "mob.enderdragon.growl");
        soundMap.put(BuiltinSound.ENTITY_ENDER_DRAGON_HURT, "mob.enderdragon.hit");
        soundMap.put(BuiltinSound.ENTITY_ENDER_DRAGON_SHOOT, "mob.ghast.fireball");

        soundMap.put(BuiltinSound.ENTITY_ENDER_EYE_DEATH, null);
        soundMap.put(BuiltinSound.ENTITY_ENDER_EYE_LAUNCH, null);

        soundMap.put(BuiltinSound.ENTITY_ENDERMAN_AMBIENT, "mob.endermen.idle");
        soundMap.put(BuiltinSound.ENTITY_ENDERMAN_DEATH, "mob.endermen.death");
        soundMap.put(BuiltinSound.ENTITY_ENDERMAN_HURT, "mob.endermen.hit");
        soundMap.put(BuiltinSound.ENTITY_ENDERMAN_SCREAM, "mob.endermen.scream");
        soundMap.put(BuiltinSound.ENTITY_ENDERMAN_STARE, "mob.endermen.stare");
        soundMap.put(BuiltinSound.ENTITY_ENDERMAN_TELEPORT, "mob.endermen.portal");

        soundMap.put(BuiltinSound.ENTITY_ENDERMITE_AMBIENT, "mob.endermite.say");
        soundMap.put(BuiltinSound.ENTITY_ENDERMITE_DEATH, "mob.endermite.kill");
        soundMap.put(BuiltinSound.ENTITY_ENDERMITE_HURT, "mob.endermite.hit");
        soundMap.put(BuiltinSound.ENTITY_ENDERMITE_STEP, "mob.endermite.step");

        soundMap.put(BuiltinSound.ENTITY_ENDER_PEARL_THROW, null);

        soundMap.put(BuiltinSound.ENTITY_EVOKER_FANGS_ATTACK, "mob.evocation_fangs.attack");
        soundMap.put(BuiltinSound.ENTITY_EVOKER_AMBIENT, "mob.evocation_illager.ambient");
        soundMap.put(BuiltinSound.ENTITY_EVOKER_CAST_SPELL, "mob.evocation_illager.cast_spell");
        soundMap.put(BuiltinSound.ENTITY_EVOKER_DEATH, "mob.evocation_illager.death");
        soundMap.put(BuiltinSound.ENTITY_EVOKER_HURT, "mob.evocation_illager.hurt");
        soundMap.put(BuiltinSound.ENTITY_EVOKER_PREPARE_ATTACK, "mob.evocation_illager.prepare_attack");
        soundMap.put(BuiltinSound.ENTITY_EVOKER_PREPARE_SUMMON, "mob.evocation_illager.prepare_summon");
        soundMap.put(BuiltinSound.ENTITY_EVOKER_PREPARE_WOLOLO, "mob.evocation_illager.prepare_wololo");

        soundMap.put(BuiltinSound.ENTITY_EXPERIENCE_BOTTLE_THROW, "random.bow");
        soundMap.put(BuiltinSound.ENTITY_EXPERIENCE_ORB_PICKUP, "random.orb");

        soundMap.put(BuiltinSound.ENTITY_FIREWORK_ROCKET_BLAST, "firework.blast");
        soundMap.put(BuiltinSound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, "firework.blast");
        soundMap.put(BuiltinSound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, "firework.large_blast");
        soundMap.put(BuiltinSound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, "firework.large_blast");
        soundMap.put(BuiltinSound.ENTITY_FIREWORK_ROCKET_LAUNCH, "firework.launch");
        soundMap.put(BuiltinSound.ENTITY_FIREWORK_ROCKET_SHOOT, "firework.shoot");
        soundMap.put(BuiltinSound.ENTITY_FIREWORK_ROCKET_TWINKLE, "firework.twinkle");
        soundMap.put(BuiltinSound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, "firework.twinkle");

        soundMap.put(BuiltinSound.ENTITY_GENERIC_BIG_FALL, "damage.fallbig");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_BURN, "random.fizz");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_DEATH, "game.player.hurt");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_DRINK, "random.drink");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_EAT, "random.eat");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_EXPLODE, "random.explode");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_EXTINGUISH_FIRE, "random.fizz");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_HURT, "random.hurt");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_SMALL_FALL, "damage.fallsmall");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_SPLASH, "random.splash");
        soundMap.put(BuiltinSound.ENTITY_GENERIC_SWIM, "random.swim");

        soundMap.put(BuiltinSound.ENTITY_GHAST_AMBIENT, "mob.ghast.moan");
        soundMap.put(BuiltinSound.ENTITY_GHAST_DEATH, "mob.ghast.death");
        soundMap.put(BuiltinSound.ENTITY_GHAST_HURT, "mob.ghast.scream");
        soundMap.put(BuiltinSound.ENTITY_GHAST_SCREAM, "mob.ghast.scream");
        soundMap.put(BuiltinSound.ENTITY_GHAST_SHOOT, "mob.ghast.fireball");
        soundMap.put(BuiltinSound.ENTITY_GHAST_WARN, "mob.ghast.charge");

        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_AMBIENT, "mob.guardian.ambient");
        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_AMBIENT_LAND, "mob.guardian.land_idle");
        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_ATTACK, "mob.guardian.attack_loop");
        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_DEATH, "mob.guardian.death");
        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_DEATH_LAND, "mob.guardian.land_death");
        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_FLOP, "mob.guardian.flop");
        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_HURT, "mob.guardian.hit");
        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_HURT_LAND, "mob.guardian.land_death");

        soundMap.put(BuiltinSound.ENTITY_HORSE_AMBIENT, "mob.horse.idle");
        soundMap.put(BuiltinSound.ENTITY_HORSE_ANGRY, "mob.horse.angry");
        soundMap.put(BuiltinSound.ENTITY_HORSE_ARMOR, "mob.horse.armor");
        soundMap.put(BuiltinSound.ENTITY_HORSE_BREATHE, "mob.horse.breathe");
        soundMap.put(BuiltinSound.ENTITY_HORSE_DEATH, "mob.horse.death");
        soundMap.put(BuiltinSound.ENTITY_HORSE_EAT, "mob.horse.eat");
        soundMap.put(BuiltinSound.ENTITY_HORSE_GALLOP, "mob.horse.gallop");
        soundMap.put(BuiltinSound.ENTITY_HORSE_HURT, "mob.horse.hit");
        soundMap.put(BuiltinSound.ENTITY_HORSE_JUMP, "mob.horse.jump");
        soundMap.put(BuiltinSound.ENTITY_HORSE_LAND, "mob.horse.land");
        soundMap.put(BuiltinSound.ENTITY_HORSE_SADDLE, "mob.horse.leather");
        soundMap.put(BuiltinSound.ENTITY_HORSE_STEP, "mob.horse.soft");
        soundMap.put(BuiltinSound.ENTITY_HORSE_STEP_WOOD, "mob.horse.wood");

        soundMap.put(BuiltinSound.ENTITY_HOSTILE_BIG_FALL, "damage.fallbig");
        soundMap.put(BuiltinSound.ENTITY_HOSTILE_DEATH, "game.player.die");
        soundMap.put(BuiltinSound.ENTITY_HOSTILE_HURT, "random.hurt");
        soundMap.put(BuiltinSound.ENTITY_HOSTILE_SMALL_FALL, "damage.fallsmall");
        soundMap.put(BuiltinSound.ENTITY_HOSTILE_SPLASH, "random.splash");
        soundMap.put(BuiltinSound.ENTITY_HOSTILE_SWIM, "random.swim");

        soundMap.put(BuiltinSound.ENTITY_HUSK_AMBIENT, "mob.husk.ambient");
        soundMap.put(BuiltinSound.ENTITY_HUSK_DEATH, "mob.husk.death");
        soundMap.put(BuiltinSound.ENTITY_HUSK_HURT, "mob.husk.hurt");
        soundMap.put(BuiltinSound.ENTITY_HUSK_STEP, "mob.husk.step");

        soundMap.put(BuiltinSound.ENTITY_ILLUSIONER_AMBIENT, "mob.evocation_illager.ambient");
        soundMap.put(BuiltinSound.ENTITY_ILLUSIONER_CAST_SPELL, "mob.evocation_illager.cast_spell");
        soundMap.put(BuiltinSound.ENTITY_ILLUSIONER_DEATH, "mob.evocation_illager.death");
        soundMap.put(BuiltinSound.ENTITY_ILLUSIONER_HURT, "mob.evocation_illager.hurt");
        soundMap.put(BuiltinSound.ENTITY_ILLUSIONER_MIRROR_MOVE, null);
        soundMap.put(BuiltinSound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, null);
        soundMap.put(BuiltinSound.ENTITY_ILLUSIONER_PREPARE_MIRROR, null);

        soundMap.put(BuiltinSound.ENTITY_IRON_GOLEM_ATTACK, "mob.irongolem.throw");
        soundMap.put(BuiltinSound.ENTITY_IRON_GOLEM_DEATH, "mob.irongolem.death");
        soundMap.put(BuiltinSound.ENTITY_IRON_GOLEM_HURT, "mob.irongolem.hit");
        soundMap.put(BuiltinSound.ENTITY_IRON_GOLEM_STEP, "mob.irongolem.walk");

        soundMap.put(BuiltinSound.ENTITY_ITEM_BREAK, "random.break");
        soundMap.put(BuiltinSound.ENTITY_ITEM_PICKUP, "random.pop");

        soundMap.put(BuiltinSound.ENTITY_ITEM_FRAME_ADD_ITEM, "block.itemframe.add_item");
        soundMap.put(BuiltinSound.ENTITY_ITEM_FRAME_BREAK, "block.itemframe.break");
        soundMap.put(BuiltinSound.ENTITY_ITEM_FRAME_PLACE, "block.itemframe.place");
        soundMap.put(BuiltinSound.ENTITY_ITEM_FRAME_REMOVE_ITEM, "block.itemframe.remove_item");
        soundMap.put(BuiltinSound.ENTITY_ITEM_FRAME_ROTATE_ITEM, "block.itemframe.rotate_item");

        soundMap.put(BuiltinSound.ENTITY_LEASH_KNOT_BREAK, "leashknot.break");
        soundMap.put(BuiltinSound.ENTITY_LEASH_KNOT_PLACE, "leashknot.place");

        soundMap.put(BuiltinSound.ENTITY_LIGHTNING_BOLT_IMPACT, "ambient.weather.lightning.impact");
        soundMap.put(BuiltinSound.ENTITY_LIGHTNING_BOLT_THUNDER, "ambient.weather.thunder");

        soundMap.put(BuiltinSound.ENTITY_LINGERING_POTION_THROW, "random.bow");

        soundMap.put(BuiltinSound.ENTITY_LLAMA_AMBIENT, "mob.llama.idle");
        soundMap.put(BuiltinSound.ENTITY_LLAMA_ANGRY, "mob.llama.angry");
        soundMap.put(BuiltinSound.ENTITY_LLAMA_CHEST, "mob.chicken.plop");
        soundMap.put(BuiltinSound.ENTITY_LLAMA_DEATH, "mob.llama.death");
        soundMap.put(BuiltinSound.ENTITY_LLAMA_EAT, "mob.llama.eat");
        soundMap.put(BuiltinSound.ENTITY_LLAMA_HURT, "mob.llama.hurt");
        soundMap.put(BuiltinSound.ENTITY_LLAMA_SPIT, "mob.llama.spit");
        soundMap.put(BuiltinSound.ENTITY_LLAMA_STEP, "mob.llama.step");
        soundMap.put(BuiltinSound.ENTITY_LLAMA_SWAG, "mob.llama.swag");

        soundMap.put(BuiltinSound.ENTITY_MAGMA_CUBE_DEATH, "mob.slime.big");
        soundMap.put(BuiltinSound.ENTITY_MAGMA_CUBE_HURT, "mob.slime.big");
        soundMap.put(BuiltinSound.ENTITY_MAGMA_CUBE_JUMP, "mob.magmacube.jump");
        soundMap.put(BuiltinSound.ENTITY_MAGMA_CUBE_SQUISH, "mob.magmacube.big");

        soundMap.put(BuiltinSound.ENTITY_MINECART_INSIDE, "minecart.inside");
        soundMap.put(BuiltinSound.ENTITY_MINECART_RIDING, "minecart.base");

        soundMap.put(BuiltinSound.ENTITY_MOOSHROOM_SHEAR, "mob.sheep.shear");

        soundMap.put(BuiltinSound.ENTITY_MULE_AMBIENT, "mob.horse.donkey.idle");
        soundMap.put(BuiltinSound.ENTITY_MULE_CHEST, "mob.chicken.plop");
        soundMap.put(BuiltinSound.ENTITY_MULE_DEATH, "mob.horse.donkey.death");
        soundMap.put(BuiltinSound.ENTITY_MULE_HURT, "mob.horse.donkey.hit");

        soundMap.put(BuiltinSound.ENTITY_PAINTING_BREAK, null);
        soundMap.put(BuiltinSound.ENTITY_PAINTING_PLACE, null);

        soundMap.put(BuiltinSound.ENTITY_PARROT_AMBIENT, "mob.parrot.idle");
        soundMap.put(BuiltinSound.ENTITY_PARROT_DEATH, "mob.parrot.death");
        soundMap.put(BuiltinSound.ENTITY_PARROT_EAT, "mob.parrot.eat");
        soundMap.put(BuiltinSound.ENTITY_PARROT_FLY, "mob.parrot.fly");
        soundMap.put(BuiltinSound.ENTITY_PARROT_HURT, "mob.parrot.hurt");
        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_BLAZE, "mob.blaze.breathe");
        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_CREEPER, "random.fuse");
        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_ELDER_GUARDIAN, "mob.elderguardian.idle");
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_ENDER_DRAGON, BuiltinSound.ENTITY_ENDER_DRAGON_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_ENDERMAN, BuiltinSound.ENTITY_ENDERMAN_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_ENDERMITE, BuiltinSound.ENTITY_ENDERMITE_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_EVOKER, BuiltinSound.ENTITY_EVOCATION_ILLAGER_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_GHAST, BuiltinSound.ENTITY_GHAST_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_HUSK, BuiltinSound.ENTITY_HUSK_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_ILLUSIONER, BuiltinSound.ENTITY_ILLUSION_ILLAGER_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_MAGMA_CUBE, BuiltinSound.ENTITY_MAGMACUBE_SQUISH);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_POLAR_BEAR, BuiltinSound.ENTITY_POLAR_BEAR_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_SHULKER, BuiltinSound.ENTITY_SHULKER_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_SILVERFISH, BuiltinSound.ENTITY_SILVERFISH_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_SKELETON, BuiltinSound.ENTITY_SKELETON_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_SLIME, BuiltinSound.ENTITY_SLIME_SQUISH);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_SPIDER, BuiltinSound.ENTITY_SPIDER_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_STRAY, BuiltinSound.ENTITY_STRAY_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_VEX, BuiltinSound.ENTITY_VEX_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_VINDICATOR, BuiltinSound.ENTITY_VINDICATION_ILLAGER_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_WITCH, BuiltinSound.ENTITY_WITCH_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_WITHER, BuiltinSound.ENTITY_WITHER_SKELETON_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_WITHER_SKELETON, BuiltinSound.ENTITY_WITHER_SKELETON_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_WOLF, BuiltinSound.ENTITY_WOLF_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE, BuiltinSound.ENTITY_ZOMBIE_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE_PIGMAN, BuiltinSound.ENTITY_ZOMBIE_PIGMAN_AMBIENT);
//        soundMap.put(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER, BuiltinSound.ENTITY_ZOMBIE_VILLAGER_AMBIENT);
        soundMap.put(BuiltinSound.ENTITY_PARROT_STEP, "mob.parrot.step");

        soundMap.put(BuiltinSound.ENTITY_PIG_AMBIENT, "mob.pig.say");
        soundMap.put(BuiltinSound.ENTITY_PIG_DEATH, "mob.pig.death");
        soundMap.put(BuiltinSound.ENTITY_PIG_HURT, "mob.pig.say");
        soundMap.put(BuiltinSound.ENTITY_PIG_SADDLE, "mob.horse.leather");
        soundMap.put(BuiltinSound.ENTITY_PIG_STEP, "mob.pig.step");

        soundMap.put(BuiltinSound.ENTITY_PLAYER_ATTACK_CRIT, null);
        soundMap.put(BuiltinSound.ENTITY_PLAYER_ATTACK_KNOCKBACK, null);
        soundMap.put(BuiltinSound.ENTITY_PLAYER_ATTACK_NODAMAGE, "game.player.attack.nodamage");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_ATTACK_STRONG, "game.player.attack.strong");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_ATTACK_SWEEP, null);
        soundMap.put(BuiltinSound.ENTITY_PLAYER_ATTACK_WEAK, "game.player.attack.nodamage");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_BIG_FALL, "damage.fallbig");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_BREATH, null);
        soundMap.put(BuiltinSound.ENTITY_PLAYER_BURP, "random.burp");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_DEATH, "game.player.die");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_HURT, "random.hurt");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_HURT_DROWN, "random.hurt");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_HURT_ON_FIRE, "random.hurt");
        soundMap.put(BuiltinSound.ENTITY_PLAYER_LEVELUP, "random.levelup");
//        soundMap.put(BuiltinSound.ENTITY_PLAYER_SMALL_FALL, BuiltinSound.ENTITY_GENERIC_SMALL_FALL);
//        soundMap.put(BuiltinSound.ENTITY_PLAYER_SPLASH, BuiltinSound.ENTITY_GENERIC_SPLASH);
//        soundMap.put(BuiltinSound.ENTITY_PLAYER_SWIM, BuiltinSound.ENTITY_GENERIC_SWIM);

        soundMap.put(BuiltinSound.ENTITY_POLAR_BEAR_AMBIENT, "mob.polarbear.idle");
        soundMap.put(BuiltinSound.ENTITY_POLAR_BEAR_AMBIENT_BABY, "mob.polarbear_baby.idle");
        soundMap.put(BuiltinSound.ENTITY_POLAR_BEAR_DEATH, "mob.polarbear.death");
        soundMap.put(BuiltinSound.ENTITY_POLAR_BEAR_HURT, "mob.polarbear.hurt");
        soundMap.put(BuiltinSound.ENTITY_POLAR_BEAR_STEP, "mob.polarbear.step");
        soundMap.put(BuiltinSound.ENTITY_POLAR_BEAR_WARNING, "mob.polarbear.warning");

        soundMap.put(BuiltinSound.ENTITY_RABBIT_AMBIENT, "mob.rabbit.idle");
        soundMap.put(BuiltinSound.ENTITY_RABBIT_ATTACK, null);
        soundMap.put(BuiltinSound.ENTITY_RABBIT_DEATH, "mob.rabbit.death");
        soundMap.put(BuiltinSound.ENTITY_RABBIT_HURT, "mob.rabbit.hurt");
        soundMap.put(BuiltinSound.ENTITY_RABBIT_JUMP, "mob.rabbit.hop");

        soundMap.put(BuiltinSound.ENTITY_SHEEP_AMBIENT, "mob.sheep.say");
        soundMap.put(BuiltinSound.ENTITY_SHEEP_DEATH, "mob.sheep.say");
        soundMap.put(BuiltinSound.ENTITY_SHEEP_HURT, "mob.sheep.say");
        soundMap.put(BuiltinSound.ENTITY_SHEEP_SHEAR, "mob.sheep.shear");
        soundMap.put(BuiltinSound.ENTITY_SHEEP_STEP, "mob.sheep.step");

        soundMap.put(BuiltinSound.ENTITY_SHULKER_AMBIENT, "mob.shulker.ambient");
        soundMap.put(BuiltinSound.ENTITY_SHULKER_CLOSE, "mob.shulker.close");
        soundMap.put(BuiltinSound.ENTITY_SHULKER_DEATH, "mob.shulker.death");
        soundMap.put(BuiltinSound.ENTITY_SHULKER_HURT, "mob.shulker.hurt");
        soundMap.put(BuiltinSound.ENTITY_SHULKER_HURT_CLOSED, "mob.shulker.close.hurt");
        soundMap.put(BuiltinSound.ENTITY_SHULKER_OPEN, "mob.shulker.open");
        soundMap.put(BuiltinSound.ENTITY_SHULKER_SHOOT, "mob.shulker.shoot");
        soundMap.put(BuiltinSound.ENTITY_SHULKER_TELEPORT, "mob.shulker.teleport");
        soundMap.put(BuiltinSound.ENTITY_SHULKER_BULLET_HIT, "mob.shulker.bullet.hit");
//        soundMap.put(BuiltinSound.ENTITY_SHULKER_BULLET_HURT, BuiltinSound.ENTITY_SHULKER_BULLET_HIT);

        soundMap.put(BuiltinSound.ENTITY_SILVERFISH_AMBIENT, "mob.silverfish.say");
        soundMap.put(BuiltinSound.ENTITY_SILVERFISH_DEATH, "mob.silverfish.kill");
        soundMap.put(BuiltinSound.ENTITY_SILVERFISH_HURT, "mob.silverfish.hit");
        soundMap.put(BuiltinSound.ENTITY_SILVERFISH_STEP, "mob.silverfish.step");

        soundMap.put(BuiltinSound.ENTITY_FISH_SWIM, "mob.fish.step");
        soundMap.put(BuiltinSound.ENTITY_PUFFER_FISH_HURT, "mob.fish.hurt");
        soundMap.put(BuiltinSound.ENTITY_TROPICAL_FISH_HURT, "mob.fish.hurt");
        soundMap.put(BuiltinSound.ENTITY_COD_FLOP, "mob.fish.flop");
        soundMap.put(BuiltinSound.ENTITY_PUFFER_FISH_FLOP, "mob.fish.flop");
        soundMap.put(BuiltinSound.ENTITY_SALMON_FLOP, "mob.fish.flop");
        soundMap.put(BuiltinSound.ENTITY_TROPICAL_FISH_FLOP, "mob.fish.flop");
        soundMap.put(BuiltinSound.ENTITY_GUARDIAN_FLOP, "mob.guardian.flop");

        soundMap.put(BuiltinSound.ENTITY_SKELETON_AMBIENT, "mob.skeleton.say");
        soundMap.put(BuiltinSound.ENTITY_SKELETON_DEATH, "mob.skeleton.death");
        soundMap.put(BuiltinSound.ENTITY_SKELETON_HURT, "mob.skeleton.hurt");
        soundMap.put(BuiltinSound.ENTITY_SKELETON_SHOOT, "random.bow");
        soundMap.put(BuiltinSound.ENTITY_SKELETON_STEP, "mob.skeleton.step");

        soundMap.put(BuiltinSound.ENTITY_SKELETON_HORSE_AMBIENT, "mob.horse.skeleton.idle");
        soundMap.put(BuiltinSound.ENTITY_SKELETON_HORSE_DEATH, "mob.horse.skeleton.death");
        soundMap.put(BuiltinSound.ENTITY_SKELETON_HORSE_HURT, "mob.horse.skeleton.hit");

        soundMap.put(BuiltinSound.ENTITY_SLIME_ATTACK, "mob.slime.attack");
        soundMap.put(BuiltinSound.ENTITY_SLIME_DEATH, "mob.slime.death");
        soundMap.put(BuiltinSound.ENTITY_SLIME_HURT, "mob.slime.hurt");
        soundMap.put(BuiltinSound.ENTITY_SLIME_JUMP, "mob.slime.jump");
        soundMap.put(BuiltinSound.ENTITY_SLIME_SQUISH, "mob.slime.squish");

        soundMap.put(BuiltinSound.ENTITY_MAGMA_CUBE_DEATH_SMALL, "mob.slime.small");
        soundMap.put(BuiltinSound.ENTITY_MAGMA_CUBE_HURT_SMALL, "mob.slime.small");
        soundMap.put(BuiltinSound.ENTITY_MAGMA_CUBE_SQUISH_SMALL, "mob.magmacube.small");

        soundMap.put(BuiltinSound.ENTITY_SLIME_DEATH_SMALL, "mob.slime.small");
        soundMap.put(BuiltinSound.ENTITY_SLIME_HURT_SMALL, "mob.slime.small");
        soundMap.put(BuiltinSound.ENTITY_SLIME_JUMP_SMALL, null);
        soundMap.put(BuiltinSound.ENTITY_SLIME_SQUISH_SMALL, null);

        soundMap.put(BuiltinSound.ENTITY_SNOWBALL_THROW, "random.bow");

        soundMap.put(BuiltinSound.ENTITY_SNOW_GOLEM_AMBIENT, null);
        soundMap.put(BuiltinSound.ENTITY_SNOW_GOLEM_DEATH, null);
        soundMap.put(BuiltinSound.ENTITY_SNOW_GOLEM_HURT, null);
        soundMap.put(BuiltinSound.ENTITY_SNOW_GOLEM_SHOOT, "random.bow");

        soundMap.put(BuiltinSound.ENTITY_SPIDER_AMBIENT, "mob.spider.say");
        soundMap.put(BuiltinSound.ENTITY_SPIDER_DEATH, "mob.spider.death");
        soundMap.put(BuiltinSound.ENTITY_SPIDER_HURT, "mob.spider.say");
        soundMap.put(BuiltinSound.ENTITY_SPIDER_STEP, "mob.spider.step");

        soundMap.put(BuiltinSound.ENTITY_SPLASH_POTION_BREAK, "random.glass");
        soundMap.put(BuiltinSound.ENTITY_SPLASH_POTION_THROW, "random.bow");

        soundMap.put(BuiltinSound.ENTITY_SQUID_AMBIENT, "mob.squid.ambient");
        soundMap.put(BuiltinSound.ENTITY_SQUID_DEATH, "mob.squid.death");
        soundMap.put(BuiltinSound.ENTITY_SQUID_HURT, "mob.squid.hurt");

        soundMap.put(BuiltinSound.ENTITY_STRAY_AMBIENT, "mob.stray.ambient");
        soundMap.put(BuiltinSound.ENTITY_STRAY_DEATH, "mob.stray.death");
        soundMap.put(BuiltinSound.ENTITY_STRAY_HURT, "mob.stray.hurt");
        soundMap.put(BuiltinSound.ENTITY_STRAY_STEP, "mob.stray.step");

        soundMap.put(BuiltinSound.ENTITY_TNT_PRIMED, "random.fuse");

        soundMap.put(BuiltinSound.ENTITY_VEX_AMBIENT, "mob.vex.ambient");
        soundMap.put(BuiltinSound.ENTITY_VEX_CHARGE, "mob.vex.charge");
        soundMap.put(BuiltinSound.ENTITY_VEX_DEATH, "mob.vex.death");
        soundMap.put(BuiltinSound.ENTITY_VEX_HURT, "mob.vex.hurt");

        soundMap.put(BuiltinSound.ENTITY_VILLAGER_AMBIENT, "mob.villager.idle");
        soundMap.put(BuiltinSound.ENTITY_VILLAGER_DEATH, "mob.villager.death");
        soundMap.put(BuiltinSound.ENTITY_VILLAGER_HURT, "mob.villager.hit");
        soundMap.put(BuiltinSound.ENTITY_VILLAGER_NO, "mob.villager.no");
        soundMap.put(BuiltinSound.ENTITY_VILLAGER_TRADE, "mob.villager.haggle");
        soundMap.put(BuiltinSound.ENTITY_VILLAGER_YES, "mob.villager.yes");

        soundMap.put(BuiltinSound.ENTITY_VINDICATOR_AMBIENT, "mob.vindicator.idle");
        soundMap.put(BuiltinSound.ENTITY_VINDICATOR_DEATH, "mob.vindicator.death");
        soundMap.put(BuiltinSound.ENTITY_VINDICATOR_HURT, "mob.vindicator.hurt");

        soundMap.put(BuiltinSound.ENTITY_WITCH_AMBIENT, "mob.witch.ambient");
        soundMap.put(BuiltinSound.ENTITY_WITCH_DEATH, "mob.witch.death");
        soundMap.put(BuiltinSound.ENTITY_WITCH_DRINK, "mob.witch.drink");
        soundMap.put(BuiltinSound.ENTITY_WITCH_HURT, "mob.witch.hurt");
        soundMap.put(BuiltinSound.ENTITY_WITCH_THROW, "mob.witch.throw");

        soundMap.put(BuiltinSound.ENTITY_WITHER_AMBIENT, "mob.wither.ambient");
        soundMap.put(BuiltinSound.ENTITY_WITHER_BREAK_BLOCK, "mob.wither.break_block");
        soundMap.put(BuiltinSound.ENTITY_WITHER_DEATH, "mob.wither.death");
        soundMap.put(BuiltinSound.ENTITY_WITHER_HURT, "mob.wither.hurt");
        soundMap.put(BuiltinSound.ENTITY_WITHER_SHOOT, "mob.wither.shoot");
        soundMap.put(BuiltinSound.ENTITY_WITHER_SPAWN, "mob.wither.spawn");

        soundMap.put(BuiltinSound.ENTITY_WITHER_SKELETON_AMBIENT, "mob.skeleton.say");
        soundMap.put(BuiltinSound.ENTITY_WITHER_SKELETON_DEATH, "mob.skeleton.death");
        soundMap.put(BuiltinSound.ENTITY_WITHER_SKELETON_HURT, "mob.skeleton.hurt");
        soundMap.put(BuiltinSound.ENTITY_WITHER_SKELETON_STEP, "mob.skeleton.step");

        soundMap.put(BuiltinSound.ENTITY_WOLF_AMBIENT, "mob.wolf.bark");
        soundMap.put(BuiltinSound.ENTITY_WOLF_DEATH, "mob.wolf.death");
        soundMap.put(BuiltinSound.ENTITY_WOLF_GROWL, "mob.wolf.growl");
        soundMap.put(BuiltinSound.ENTITY_WOLF_HOWL, null);
        soundMap.put(BuiltinSound.ENTITY_WOLF_HURT, "mob.wolf.hurt");
        soundMap.put(BuiltinSound.ENTITY_WOLF_PANT, "mob.wolf.panting");
        soundMap.put(BuiltinSound.ENTITY_WOLF_SHAKE, "mob.wolf.shake");
        soundMap.put(BuiltinSound.ENTITY_WOLF_STEP, "mob.wolf.step");
        soundMap.put(BuiltinSound.ENTITY_WOLF_WHINE, "mob.wolf.whine");

        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_AMBIENT, "mob.zombie.say");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, "mob.zombie.wood");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, "mob.zombie.wood");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, "mob.zombie.woodbreak");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_DEATH, "mob.zombie.death");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_HURT, "mob.zombie.hurt");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_INFECT, "mob.zombie.unfect");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_STEP, "mob.zombie.step");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_HORSE_AMBIENT, "mob.horse.zombie.idle");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_HORSE_DEATH, "mob.horse.zombie.death");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_HORSE_HURT, "mob.horse.zombie.hit");

        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_PIGMAN_AMBIENT, "mob.zombiepig.zpig");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_PIGMAN_ANGRY, "mob.zombiepig.zpigangry");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_PIGMAN_DEATH, "mob.zombiepig.zpigdeath");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_PIGMAN_HURT, "mob.zombiepig.zpighurt");

        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, "mob.zombie_villager.say");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, "mob.zombie.unfect");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_CURE, "mob.zombie.remedy");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_DEATH, "mob.zombie_villager.death");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_HURT, "mob.zombie_villager.hurt");
        soundMap.put(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_STEP, "mob.zombie.step");

        soundMap.put(BuiltinSound.ITEM_ARMOR_EQUIP_CHAIN, null);
        soundMap.put(BuiltinSound.ITEM_ARMOR_EQUIP_DIAMOND, null);
        soundMap.put(BuiltinSound.ITEM_ARMOR_EQUIP_ELYTRA, null);
        soundMap.put(BuiltinSound.ITEM_ARMOR_EQUIP_GENERIC, null);
        soundMap.put(BuiltinSound.ITEM_ARMOR_EQUIP_GOLD, null);
        soundMap.put(BuiltinSound.ITEM_ARMOR_EQUIP_IRON, null);
        soundMap.put(BuiltinSound.ITEM_ARMOR_EQUIP_LEATHER, null);

        soundMap.put(BuiltinSound.ITEM_BOTTLE_EMPTY, "bucket.empty_water");
        soundMap.put(BuiltinSound.ITEM_BOTTLE_FILL, "bucket.fill_water");
        soundMap.put(BuiltinSound.ITEM_BOTTLE_FILL_DRAGONBREATH, null);

        soundMap.put(BuiltinSound.ITEM_BUCKET_EMPTY, "bucket.empty_water");
        soundMap.put(BuiltinSound.ITEM_BUCKET_EMPTY_LAVA, "bucket.empty_lava");
        soundMap.put(BuiltinSound.ITEM_BUCKET_FILL, "bucket.fill_water");
        soundMap.put(BuiltinSound.ITEM_BUCKET_FILL_LAVA, "bucket.fill_lava");

        soundMap.put(BuiltinSound.ITEM_CHORUS_FRUIT_TELEPORT, "mob.enderman.portal");

        soundMap.put(BuiltinSound.ITEM_ELYTRA_FLYING, "elytra.loop");

        soundMap.put(BuiltinSound.ITEM_FIRECHARGE_USE, "mob.ghast.fireball");

        soundMap.put(BuiltinSound.ITEM_FLINTANDSTEEL_USE, "fire.ignit");

        soundMap.put(BuiltinSound.ITEM_HOE_TILL, null);

        soundMap.put(BuiltinSound.ITEM_SHIELD_BLOCK, null);

        soundMap.put(BuiltinSound.ITEM_SHIELD_BREAK, "random.break");

        soundMap.put(BuiltinSound.ITEM_SHOVEL_FLATTEN, null);

        soundMap.put(BuiltinSound.ITEM_TOTEM_USE, "random.totem");

        soundMap.put(BuiltinSound.MUSIC_CREATIVE, "music.game.creative");
        soundMap.put(BuiltinSound.MUSIC_CREDITS, "music.game.credits");
        soundMap.put(BuiltinSound.MUSIC_DRAGON, "music.game.endboss");
        soundMap.put(BuiltinSound.MUSIC_END, "music.game.end");
        soundMap.put(BuiltinSound.MUSIC_GAME, "music.game");
        soundMap.put(BuiltinSound.MUSIC_MENU, "music.menu");
        soundMap.put(BuiltinSound.MUSIC_NETHER, "music.game.nether");

        soundMap.put(BuiltinSound.MUSIC_DISC_11, "record.11");
        soundMap.put(BuiltinSound.MUSIC_DISC_13, "record.13");
        soundMap.put(BuiltinSound.MUSIC_DISC_BLOCKS, "record.blocks");
        soundMap.put(BuiltinSound.MUSIC_DISC_CAT, "record.cat");
        soundMap.put(BuiltinSound.MUSIC_DISC_CHIRP, "record.chirp");
        soundMap.put(BuiltinSound.MUSIC_DISC_FAR, "record.far");
        soundMap.put(BuiltinSound.MUSIC_DISC_MALL, "record.mall");
        soundMap.put(BuiltinSound.MUSIC_DISC_MELLOHI, "record.mellohi");
        soundMap.put(BuiltinSound.MUSIC_DISC_STAL, "record.stal");
        soundMap.put(BuiltinSound.MUSIC_DISC_STRAD, "record.strad");
        soundMap.put(BuiltinSound.MUSIC_DISC_WAIT, "record.wait");
        soundMap.put(BuiltinSound.MUSIC_DISC_WARD, "record.ward");

        soundMap.put(BuiltinSound.UI_BUTTON_CLICK, "random.click");
        soundMap.put(BuiltinSound.UI_TOAST_IN, "random.toast");
        soundMap.put(BuiltinSound.UI_TOAST_OUT, "random.toast");
        soundMap.put(BuiltinSound.UI_TOAST_CHALLENGE_COMPLETE, "random.toast");

        soundMap.put(BuiltinSound.WEATHER_RAIN, "ambient.weather.rain");
        soundMap.put(BuiltinSound.WEATHER_RAIN_ABOVE, "ambient.weather.rain");

        soundMap.put(BuiltinSound.ENTITY_DROWNED_AMBIENT, "mob.drowned.say");
        soundMap.put(BuiltinSound.ENTITY_DROWNED_AMBIENT_WATER, "mob.drowned.say_water");
        soundMap.put(BuiltinSound.ENTITY_DROWNED_SWIM, "mob.drowned.swim");
        soundMap.put(BuiltinSound.ENTITY_DROWNED_STEP, "mob.drowned.step");
        soundMap.put(BuiltinSound.ENTITY_DROWNED_HURT, "mob.drowned.hurt");
        soundMap.put(BuiltinSound.ENTITY_DROWNED_DEATH, "mob.drowned.death");
        soundMap.put(BuiltinSound.ENTITY_DROWNED_DEATH_WATER, "mob.drowned.death_water");

        soundMap.put(BuiltinSound.ENTITY_WANDERING_TRADER_AMBIENT, "mob.wanderingtrader.idle");
        // TODO: more wandering trader sounds

        soundMap.put(BuiltinSound.BLOCK_BEACON_AMBIENT, "beacon.ambient");
        soundMap.put(BuiltinSound.BLOCK_BEACON_ACTIVATE, "beacon.activate");
        soundMap.put(BuiltinSound.BLOCK_BEACON_DEACTIVATE, "beacon.deactivate");
        soundMap.put(BuiltinSound.BLOCK_BEACON_POWER_SELECT, "beacon.power");

        soundMap.put(BuiltinSound.ENTITY_PHANTOM_AMBIENT, "mob.phantom.idle");
        soundMap.put(BuiltinSound.ENTITY_PHANTOM_BITE, "mob.phantom.bite");
        soundMap.put(BuiltinSound.ENTITY_PHANTOM_HURT, "mob.phantom.hurt");
        soundMap.put(BuiltinSound.ENTITY_PHANTOM_DEATH, "mob.phantom.death");
        soundMap.put(BuiltinSound.ENTITY_PHANTOM_SWOOP, "mob.phantom.swoop");
    }

    /**
     * This method translates a Java sound to a Bedrock sound.
     */
    public static String translateToBedrock(BuiltinSound javaSound) {
        if(soundMap.containsKey(javaSound)) {
            return soundMap.get(javaSound);
        }
        return null;
    }
}
