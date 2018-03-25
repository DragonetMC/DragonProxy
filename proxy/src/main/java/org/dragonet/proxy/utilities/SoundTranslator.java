package org.dragonet.proxy.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.dragonet.proxy.DragonProxy;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;

public class SoundTranslator {


	private HashMap<BuiltinSound, String> pctope = new HashMap<BuiltinSound, String>();
	private HashMap<BuiltinSound, BuiltinSound> pceq = new HashMap<BuiltinSound, BuiltinSound>();
	private List<BuiltinSound> ignore = new ArrayList<BuiltinSound>();
	
	public boolean isIgnored(BuiltinSound sound) {
		return ignore.contains(sound);
	}
	
	public boolean isTranslatable(BuiltinSound sound) {
		return pctope.containsKey(sound);
	}
	
	public String translate(BuiltinSound sound) {
		return pctope.get(sound);
	}

	public SoundTranslator() {
		
		// This list is created by site https://minecraft.gamepedia.com/Sounds.json
		
		register(BuiltinSound.AMBIENT_CAVE, null);
		
		register(BuiltinSound.BLOCK_ANVIL_BREAK, BuiltinSound.BLOCK_METAL_BREAK);
		register(BuiltinSound.BLOCK_ANVIL_DESTROY, "random.anvil_break");
		register(BuiltinSound.BLOCK_ANVIL_FALL, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_ANVIL_HIT, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_ANVIL_LAND, "random.anvil_land");
		register(BuiltinSound.BLOCK_ANVIL_PLACE, BuiltinSound.BLOCK_ANVIL_LAND);
		register(BuiltinSound.BLOCK_ANVIL_STEP, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_ANVIL_USE, "random.anvil_use");
		
		register(BuiltinSound.BLOCK_BREWING_STAND_BREW, null);
		
		register(BuiltinSound.BLOCK_CHEST_CLOSE, "random.chestclosed");
		register(BuiltinSound.BLOCK_CHEST_LOCKED, BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE);
		register(BuiltinSound.BLOCK_CHEST_OPEN, "random.chestopen");
		
		register(BuiltinSound.BLOCK_CHORUS_FLOWER_DEATH, "block.chorusflower.death");
		register(BuiltinSound.BLOCK_CHORUS_FLOWER_GROW, "block.chorusflower.grow");
		
		register(BuiltinSound.BLOCK_CLOTH_BREAK, "dig.cloth");
		register(BuiltinSound.BLOCK_CLOTH_FALL, BuiltinSound.BLOCK_CLOTH_STEP);
		register(BuiltinSound.BLOCK_CLOTH_HIT, BuiltinSound.BLOCK_CLOTH_STEP);
		register(BuiltinSound.BLOCK_CLOTH_PLACE, BuiltinSound.BLOCK_CLOTH_BREAK);
		register(BuiltinSound.BLOCK_CLOTH_STEP, "step.cloth");
		
		register(BuiltinSound.BLOCK_COMPARATOR_CLICK, BuiltinSound.UI_BUTTON_CLICK);
		
		register(BuiltinSound.BLOCK_DISPENSER_DISPENSE, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_DISPENSER_FAIL, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_DISPENSER_LAUNCH, BuiltinSound.ENTITY_ARROW_SHOOT);
		
		register(BuiltinSound.BLOCK_ENCHANTMENT_TABLE_USE, null);
		
		register(BuiltinSound.BLOCK_END_GATEWAY_SPAWN, BuiltinSound.ENTITY_GENERIC_EXPLODE);
		register(BuiltinSound.BLOCK_END_PORTAL_SPAWN, null);
		register(BuiltinSound.BLOCK_END_PORTAL_FRAME_FILL, null);
		
		register(BuiltinSound.BLOCK_ENDERCHEST_CLOSE, BuiltinSound.BLOCK_CHEST_CLOSE);
		register(BuiltinSound.BLOCK_ENDERCHEST_OPEN, BuiltinSound.BLOCK_CHEST_OPEN);
		
		register(BuiltinSound.BLOCK_FENCE_GATE_CLOSE, BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE);
		register(BuiltinSound.BLOCK_FENCE_GATE_OPEN, BuiltinSound.BLOCK_WOODEN_DOOR_OPEN);
		
		register(BuiltinSound.BLOCK_FIRE_AMBIENT, "fire.fire");
		register(BuiltinSound.BLOCK_FIRE_EXTINGUISH, BuiltinSound.ENTITY_GENERIC_BURN);
		
		register(BuiltinSound.BLOCK_FURNACE_FIRE_CRACKLE, null);
		
		register(BuiltinSound.BLOCK_GLASS_BREAK, "random.glass");
		register(BuiltinSound.BLOCK_GLASS_FALL, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_GLASS_HIT, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_GLASS_PLACE, BuiltinSound.BLOCK_METAL_BREAK);
		register(BuiltinSound.BLOCK_GLASS_STEP, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_GRASS_BREAK, "dig.grass");
		register(BuiltinSound.BLOCK_GRASS_FALL, BuiltinSound.BLOCK_GRASS_STEP);
		register(BuiltinSound.BLOCK_GRASS_HIT, BuiltinSound.BLOCK_GRASS_STEP);
		register(BuiltinSound.BLOCK_GRASS_PLACE, BuiltinSound.BLOCK_GRASS_BREAK);
		register(BuiltinSound.BLOCK_GRASS_STEP, "step.grass");
		
		register(BuiltinSound.BLOCK_GRAVEL_BREAK, "dig.gravel");
		register(BuiltinSound.BLOCK_GRAVEL_FALL, BuiltinSound.BLOCK_GRAVEL_STEP);
		register(BuiltinSound.BLOCK_GRAVEL_HIT, BuiltinSound.BLOCK_GRAVEL_STEP);
		register(BuiltinSound.BLOCK_GRAVEL_PLACE, BuiltinSound.BLOCK_GRASS_BREAK);
		register(BuiltinSound.BLOCK_GRAVEL_STEP, "step.gravel");
		
		register(BuiltinSound.BLOCK_IRON_DOOR_CLOSE, BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE);
		register(BuiltinSound.BLOCK_IRON_DOOR_OPEN, BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE);
		register(BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE, BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE);
		register(BuiltinSound.BLOCK_IRON_TRAPDOOR_OPEN, BuiltinSound.BLOCK_WOODEN_DOOR_OPEN);
		
		register(BuiltinSound.BLOCK_LADDER_BREAK, BuiltinSound.BLOCK_WOOD_BREAK);
		register(BuiltinSound.BLOCK_LADDER_FALL, BuiltinSound.BLOCK_LADDER_STEP);
		register(BuiltinSound.BLOCK_LADDER_HIT, BuiltinSound.BLOCK_LADDER_STEP);
		register(BuiltinSound.BLOCK_LADDER_PLACE, BuiltinSound.BLOCK_WOOD_BREAK);
		register(BuiltinSound.BLOCK_LADDER_STEP, "step.ladder");
		
		register(BuiltinSound.BLOCK_LAVA_AMBIENT, "liquid.lava");
		register(BuiltinSound.BLOCK_LAVA_EXTINGUISH, BuiltinSound.ENTITY_GENERIC_BURN);
		register(BuiltinSound.BLOCK_LAVA_POP, "liquid.lavapop");
		
		register(BuiltinSound.BLOCK_LEVER_CLICK, BuiltinSound.UI_BUTTON_CLICK);
		
		register(BuiltinSound.BLOCK_METAL_BREAK, "dig.stone");
		register(BuiltinSound.BLOCK_METAL_FALL, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_METAL_HIT, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_METAL_PLACE, BuiltinSound.BLOCK_METAL_BREAK);
		register(BuiltinSound.BLOCK_METAL_STEP, "step.stone");
		
		register(BuiltinSound.BLOCK_METAL_PRESSUREPLATE_CLICK_OFF, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, BuiltinSound.UI_BUTTON_CLICK);
		
		register(BuiltinSound.BLOCK_NOTE_BASEDRUM, "note.bd");
		register(BuiltinSound.BLOCK_NOTE_BASS, "note.bassattack");
		register(BuiltinSound.BLOCK_NOTE_BELL, null);
		register(BuiltinSound.BLOCK_NOTE_CHIME, null);
		register(BuiltinSound.BLOCK_NOTE_FLUTE, null);
		register(BuiltinSound.BLOCK_NOTE_GUITAR, null);
		register(BuiltinSound.BLOCK_NOTE_HARP, "note.harp");
		register(BuiltinSound.BLOCK_NOTE_HAT, "note.hat");
		register(BuiltinSound.BLOCK_NOTE_PLING, "note.pling");
		register(BuiltinSound.BLOCK_NOTE_SNARE, "note.snare");
		register(BuiltinSound.BLOCK_NOTE_XYLOPHONE, null);
		
		register(BuiltinSound.BLOCK_PISTON_CONTRACT, "tile.piston.in");
		register(BuiltinSound.BLOCK_PISTON_EXTEND, "tile.piston.out");
		
		register(BuiltinSound.BLOCK_PORTAL_AMBIENT, "portal.portal");
		register(BuiltinSound.BLOCK_PORTAL_TRAVEL, null);
		register(BuiltinSound.BLOCK_PORTAL_TRIGGER, "portal.trigger");
		
		register(BuiltinSound.BLOCK_REDSTONE_TORCH_BURNOUT, BuiltinSound.ENTITY_GENERIC_BURN);
		
		register(BuiltinSound.BLOCK_SAND_BREAK, "dig.sand");
		register(BuiltinSound.BLOCK_SAND_FALL, BuiltinSound.BLOCK_SAND_STEP);
		register(BuiltinSound.BLOCK_SAND_HIT, BuiltinSound.BLOCK_SAND_STEP);
		register(BuiltinSound.BLOCK_SAND_PLACE, BuiltinSound.BLOCK_SAND_BREAK);
		register(BuiltinSound.BLOCK_SAND_STEP, "step.sand");
		
		register(BuiltinSound.BLOCK_SHULKER_BOX_CLOSE, "random.shulkerboxclosed");
		register(BuiltinSound.BLOCK_SHULKER_BOX_OPEN, "random.shulkerboxopen");
		
		register(BuiltinSound.BLOCK_SLIME_BREAK, BuiltinSound.ENTITY_SLIME_DEATH);
		register(BuiltinSound.BLOCK_SLIME_FALL, BuiltinSound.BLOCK_SLIME_STEP);
		register(BuiltinSound.BLOCK_SLIME_HIT, BuiltinSound.BLOCK_SLIME_STEP);
		register(BuiltinSound.BLOCK_SLIME_PLACE, BuiltinSound.ENTITY_SLIME_JUMP);
		register(BuiltinSound.BLOCK_SLIME_STEP, "step.slime");
		
		register(BuiltinSound.BLOCK_SNOW_BREAK, "dig.snow");
		register(BuiltinSound.BLOCK_SNOW_FALL, "step.snow");
		register(BuiltinSound.BLOCK_SNOW_HIT, "step.snow");
		register(BuiltinSound.BLOCK_SNOW_PLACE, "dig.snow");
		register(BuiltinSound.BLOCK_SNOW_STEP, "step.snow");
		
		register(BuiltinSound.BLOCK_STONE_BREAK, BuiltinSound.BLOCK_METAL_BREAK);
		register(BuiltinSound.BLOCK_STONE_FALL, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_STONE_HIT, BuiltinSound.BLOCK_METAL_STEP);
		register(BuiltinSound.BLOCK_STONE_PLACE, BuiltinSound.BLOCK_METAL_BREAK);
		register(BuiltinSound.BLOCK_STONE_STEP, BuiltinSound.BLOCK_METAL_STEP);
		
		register(BuiltinSound.BLOCK_STONE_BUTTON_CLICK_OFF, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_STONE_BUTTON_CLICK_ON, BuiltinSound.UI_BUTTON_CLICK);
		
		register(BuiltinSound.BLOCK_STONE_PRESSUREPLATE_CLICK_OFF, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_STONE_PRESSUREPLATE_CLICK_ON, BuiltinSound.UI_BUTTON_CLICK);
		
		register(BuiltinSound.BLOCK_TRIPWIRE_ATTACH, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_TRIPWIRE_CLICK_OFF, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_TRIPWIRE_CLICK_ON, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_TRIPWIRE_DETACH, BuiltinSound.ENTITY_ARROW_HIT);
		
		register(BuiltinSound.BLOCK_WATER_AMBIENT, "liquid.water");
		
		register(BuiltinSound.BLOCK_WATERLILY_PLACE, null);
		
		register(BuiltinSound.BLOCK_WOOD_BREAK, "dig.wood");
		register(BuiltinSound.BLOCK_WOOD_FALL, BuiltinSound.BLOCK_WOOD_STEP);
		register(BuiltinSound.BLOCK_WOOD_HIT, BuiltinSound.BLOCK_WOOD_STEP);
		register(BuiltinSound.BLOCK_WOOD_PLACE, BuiltinSound.BLOCK_WOOD_BREAK);
		register(BuiltinSound.BLOCK_WOOD_STEP, "step.wood");
		
		register(BuiltinSound.BLOCK_WOOD_BUTTON_CLICK_OFF, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_WOOD_BUTTON_CLICK_ON, BuiltinSound.UI_BUTTON_CLICK);
		
		register(BuiltinSound.BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF, BuiltinSound.UI_BUTTON_CLICK);
		register(BuiltinSound.BLOCK_WOOD_PRESSUREPLATE_CLICK_ON, BuiltinSound.UI_BUTTON_CLICK);
		
		register(BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE, "random.door_close");
		register(BuiltinSound.BLOCK_WOODEN_DOOR_OPEN, "random.door_open");
		
		register(BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE, BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE);
		register(BuiltinSound.BLOCK_WOODEN_TRAPDOOR_OPEN, BuiltinSound.BLOCK_WOODEN_DOOR_OPEN);
		
		register(BuiltinSound.ENCHANT_THORNS_HIT, null);
		
		register(BuiltinSound.ENTITY_ARMORSTAND_BREAK, "mob.armor_stand.break");
		register(BuiltinSound.ENTITY_ARMORSTAND_FALL, "mob.armor_stand.land");
		register(BuiltinSound.ENTITY_ARMORSTAND_HIT, "mob.armor_stand.hit");
		register(BuiltinSound.ENTITY_ARMORSTAND_PLACE, "mob.armor_stand.place");
		
		register(BuiltinSound.ENTITY_ARROW_HIT, "random.bowhit");
		register(BuiltinSound.ENTITY_ARROW_HIT_PLAYER, BuiltinSound.ENTITY_ARROW_HIT);
		register(BuiltinSound.ENTITY_ARROW_SHOOT, "random.bow");
		
		register(BuiltinSound.ENTITY_BAT_AMBIENT, "mob.bat.idle");
		register(BuiltinSound.ENTITY_BAT_DEATH, "mob.bat.death");
		register(BuiltinSound.ENTITY_BAT_HURT, "mob.bat.hurt");
		register(BuiltinSound.ENTITY_BAT_LOOP, null);
		register(BuiltinSound.ENTITY_BAT_TAKEOFF, "mob.bat.takeoff");
		
		register(BuiltinSound.ENTITY_BLAZE_AMBIENT, "mob.blaze.breathe");
		register(BuiltinSound.ENTITY_BLAZE_BURN, BuiltinSound.BLOCK_FIRE_AMBIENT);
		register(BuiltinSound.ENTITY_BLAZE_DEATH, "mob.blaze.death");
		register(BuiltinSound.ENTITY_BLAZE_HURT, "mob.blaze.hit");
		register(BuiltinSound.ENTITY_BLAZE_SHOOT, "mob.blaze.shoot");
		
		register(BuiltinSound.ENTITY_BOAT_PADDLE_LAND, null);
		register(BuiltinSound.ENTITY_BOAT_PADDLE_WATER, null);
		
		register(BuiltinSound.ENTITY_BOBBER_RETRIEVE, null);
		register(BuiltinSound.ENTITY_BOBBER_SPLASH, BuiltinSound.ENTITY_GENERIC_SPLASH);
		register(BuiltinSound.ENTITY_BOBBER_THROW, null);
		
		register(BuiltinSound.ENTITY_CAT_AMBIENT, "mob.cat.meow");
		register(BuiltinSound.ENTITY_CAT_DEATH, BuiltinSound.ENTITY_CAT_HURT);
		register(BuiltinSound.ENTITY_CAT_HISS, "mob.cat.hiss");
		register(BuiltinSound.ENTITY_CAT_HURT, "mob.cat.hit");
		register(BuiltinSound.ENTITY_CAT_PURR, "mob.cat.purr");
		register(BuiltinSound.ENTITY_CAT_PURREOW, "mob.cat.purreow");
		
		register(BuiltinSound.ENTITY_CHICKEN_AMBIENT, "mob.chicken.say");
		register(BuiltinSound.ENTITY_CHICKEN_DEATH, BuiltinSound.ENTITY_CHICKEN_HURT);
		register(BuiltinSound.ENTITY_CHICKEN_EGG, "mob.chicken.plop");
		register(BuiltinSound.ENTITY_CHICKEN_HURT, "mob.chicken.hurt");
		register(BuiltinSound.ENTITY_CHICKEN_STEP, "mob.chicken.step");
		
		register(BuiltinSound.ENTITY_COW_AMBIENT, "mob.cow.say");
		register(BuiltinSound.ENTITY_COW_DEATH, BuiltinSound.ENTITY_COW_HURT);
		register(BuiltinSound.ENTITY_COW_HURT, "mob.cow.hurt");
		register(BuiltinSound.ENTITY_COW_MILK, "mob.cow.milk");
		register(BuiltinSound.ENTITY_COW_STEP, "mob.cow.step");
		
		register(BuiltinSound.ENTITY_CREEPER_DEATH, "mob.creeper.death");
		register(BuiltinSound.ENTITY_CREEPER_HURT, "mob.creeper.say");
		register(BuiltinSound.ENTITY_CREEPER_PRIMED, "random.fuse");
		
		register(BuiltinSound.ENTITY_DONKEY_AMBIENT, "mob.horse.donkey.idle");
		register(BuiltinSound.ENTITY_DONKEY_ANGRY, "mob.horse.donkey.angry");
		register(BuiltinSound.ENTITY_DONKEY_CHEST, BuiltinSound.ENTITY_CHICKEN_EGG);
		register(BuiltinSound.ENTITY_DONKEY_DEATH, "mob.horse.donkey.death");
		register(BuiltinSound.ENTITY_DONKEY_HURT, "mob.horse.donkey.hit");
		
		register(BuiltinSound.ENTITY_EGG_THROW, BuiltinSound.ENTITY_ARROW_SHOOT);
		
		register(BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT, "mob.elderguardian.idle");
		register(BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND, BuiltinSound.ENTITY_GUARDIAN_AMBIENT_LAND);
		register(BuiltinSound.ENTITY_ELDER_GUARDIAN_CURSE, "mob.elderguardian.curse");
		register(BuiltinSound.ENTITY_ELDER_GUARDIAN_DEATH, "mob.elderguardian.death");
		register(BuiltinSound.ENTITY_ELDER_GUARDIAN_DEATH_LAND, BuiltinSound.ENTITY_GUARDIAN_DEATH_LAND);
		register(BuiltinSound.ENTITY_ELDER_GUARDIAN_FLOP, BuiltinSound.ENTITY_GUARDIAN_FLOP);
		register(BuiltinSound.ENTITY_ELDER_GUARDIAN_HURT, "mob.elderguardian.hit");
		register(BuiltinSound.ENTITY_ELDER_GUARDIAN_HURT_LAND, BuiltinSound.ENTITY_GUARDIAN_HURT_LAND);
		
		register(BuiltinSound.ENTITY_ENDERDRAGON_AMBIENT, BuiltinSound.ENTITY_ENDERDRAGON_GROWL);
		register(BuiltinSound.ENTITY_ENDERDRAGON_DEATH, "mob.enderdragon.death");
		register(BuiltinSound.ENTITY_ENDERDRAGON_FLAP, "mob.enderdragon.flap");
		register(BuiltinSound.ENTITY_ENDERDRAGON_GROWL, "mob.enderdragon.growl");
		register(BuiltinSound.ENTITY_ENDERDRAGON_HURT, "mob.enderdragon.hit");
		register(BuiltinSound.ENTITY_ENDERDRAGON_SHOOT, BuiltinSound.ENTITY_GHAST_SHOOT);
		register(BuiltinSound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, BuiltinSound.ENTITY_GENERIC_EXPLODE);
		
		register(BuiltinSound.ENTITY_ENDEREYE_DEATH, null);
		register(BuiltinSound.ENTITY_ENDEREYE_LAUNCH, null);
		
		register(BuiltinSound.ENTITY_ENDERMEN_AMBIENT, "mob.endermen.idle");
		register(BuiltinSound.ENTITY_ENDERMEN_DEATH, "mob.endermen.death");
		register(BuiltinSound.ENTITY_ENDERMEN_HURT, "mob.endermen.hit");
		register(BuiltinSound.ENTITY_ENDERMEN_SCREAM, "mob.endermen.scream");
		register(BuiltinSound.ENTITY_ENDERMEN_STARE, "mob.endermen.stare");
		register(BuiltinSound.ENTITY_ENDERMEN_TELEPORT, "mob.endermen.portal");
		
		register(BuiltinSound.ENTITY_ENDERMITE_AMBIENT, "mob.endermite.say");
		register(BuiltinSound.ENTITY_ENDERMITE_DEATH, "mob.endermite.kill");
		register(BuiltinSound.ENTITY_ENDERMITE_HURT, "mob.endermite.hit");
		register(BuiltinSound.ENTITY_ENDERMITE_STEP, "mob.endermite.step");
		
		register(BuiltinSound.ENTITY_ENDERPEARL_THROW, null);
		
		register(BuiltinSound.ENTITY_EVOCATION_FANGS_ATTACK, "mob.evocation_fangs.attack");
		register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_AMBIENT, "mob.evocation_illager.ambient");
		register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, "mob.evocation_illager.cast_spell");
		register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_DEATH, "mob.evocation_illager.death");
		register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_HURT, "mob.evocation_illager.hurt");
		register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_PREPARE_ATTACK, "mob.evocation_illager.prepare_attack");
		register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_PREPARE_SUMMON, "mob.evocation_illager.prepare_summon");
		register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO, "mob.evocation_illager.prepare_wololo");
		
		register(BuiltinSound.ENTITY_EXPERIENCE_BOTTLE_THROW, BuiltinSound.ENTITY_ARROW_SHOOT);
		register(BuiltinSound.ENTITY_EXPERIENCE_ORB_PICKUP, "random.orb");
		
		register(BuiltinSound.ENTITY_FIREWORK_BLAST, "firework.blast");
		register(BuiltinSound.ENTITY_FIREWORK_BLAST_FAR, BuiltinSound.ENTITY_FIREWORK_BLAST);
		register(BuiltinSound.ENTITY_FIREWORK_LARGE_BLAST, "firework.large_blast");
		register(BuiltinSound.ENTITY_FIREWORK_LARGE_BLAST_FAR, BuiltinSound.ENTITY_FIREWORK_LARGE_BLAST);
		register(BuiltinSound.ENTITY_FIREWORK_LAUNCH, "firework.launch");
		register(BuiltinSound.ENTITY_FIREWORK_SHOOT, "firework.shoot");
		register(BuiltinSound.ENTITY_FIREWORK_TWINKLE, "firework.twinkle");
		register(BuiltinSound.ENTITY_FIREWORK_TWINKLE_FAR, BuiltinSound.ENTITY_FIREWORK_TWINKLE);
		
		register(BuiltinSound.ENTITY_GENERIC_BIG_FALL, "damage.fallbig");
		register(BuiltinSound.ENTITY_GENERIC_BURN, "random.fizz");
		register(BuiltinSound.ENTITY_GENERIC_DEATH, "game.player.hurt");
		register(BuiltinSound.ENTITY_GENERIC_DRINK, "random.drink");
		register(BuiltinSound.ENTITY_GENERIC_EAT, "random.eat");
		register(BuiltinSound.ENTITY_GENERIC_EXPLODE, "random.explode");
		register(BuiltinSound.ENTITY_GENERIC_EXTINGUISH_FIRE, BuiltinSound.ENTITY_GENERIC_BURN);
		register(BuiltinSound.ENTITY_GENERIC_HURT, "random.hurt");
		register(BuiltinSound.ENTITY_GENERIC_SMALL_FALL, "damage.fallsmall");
		register(BuiltinSound.ENTITY_GENERIC_SPLASH, "random.splash");
		register(BuiltinSound.ENTITY_GENERIC_SWIM, "random.swim");
		
		register(BuiltinSound.ENTITY_GHAST_AMBIENT, "mob.ghast.moan");
		register(BuiltinSound.ENTITY_GHAST_DEATH, "mob.ghast.death");
		register(BuiltinSound.ENTITY_GHAST_HURT, BuiltinSound.ENTITY_GHAST_SCREAM);
		register(BuiltinSound.ENTITY_GHAST_SCREAM, "mob.ghast.scream");
		register(BuiltinSound.ENTITY_GHAST_SHOOT, "mob.ghast.fireball");
		register(BuiltinSound.ENTITY_GHAST_WARN, "mob.ghast.charge");
		
		register(BuiltinSound.ENTITY_GUARDIAN_AMBIENT, "mob.guardian.ambient");
		register(BuiltinSound.ENTITY_GUARDIAN_AMBIENT_LAND, "mob.guardian.land_idle");
		register(BuiltinSound.ENTITY_GUARDIAN_ATTACK, "mob.guardian.attack_loop");
		register(BuiltinSound.ENTITY_GUARDIAN_DEATH, "mob.guardian.death");
		register(BuiltinSound.ENTITY_GUARDIAN_DEATH_LAND, "mob.guardian.land_death");
		register(BuiltinSound.ENTITY_GUARDIAN_FLOP, "mob.guardian.flop");
		register(BuiltinSound.ENTITY_GUARDIAN_HURT, "mob.guardian.hit");
		register(BuiltinSound.ENTITY_GUARDIAN_HURT_LAND, "mob.guardian.land_death");
		
		register(BuiltinSound.ENTITY_HORSE_AMBIENT, "mob.horse.idle");
		register(BuiltinSound.ENTITY_HORSE_ANGRY, "mob.horse.angry");
		register(BuiltinSound.ENTITY_HORSE_ARMOR, "mob.horse.armor");
		register(BuiltinSound.ENTITY_HORSE_BREATHE, "mob.horse.breathe");
		register(BuiltinSound.ENTITY_HORSE_DEATH, "mob.horse.death");
		register(BuiltinSound.ENTITY_HORSE_EAT, "mob.horse.eat");
		register(BuiltinSound.ENTITY_HORSE_GALLOP, "mob.horse.gallop");
		register(BuiltinSound.ENTITY_HORSE_HURT, "mob.horse.hit");
		register(BuiltinSound.ENTITY_HORSE_JUMP, "mob.horse.jump");
		register(BuiltinSound.ENTITY_HORSE_LAND, "mob.horse.land");
		register(BuiltinSound.ENTITY_HORSE_SADDLE, "mob.horse.leather");
		register(BuiltinSound.ENTITY_HORSE_STEP, "mob.horse.soft");
		register(BuiltinSound.ENTITY_HORSE_STEP_WOOD, "mob.horse.wood");
		
		register(BuiltinSound.ENTITY_HOSTILE_BIG_FALL, BuiltinSound.ENTITY_GENERIC_BIG_FALL);
		register(BuiltinSound.ENTITY_HOSTILE_DEATH, BuiltinSound.ENTITY_PLAYER_DEATH);
		register(BuiltinSound.ENTITY_HOSTILE_HURT, BuiltinSound.ENTITY_GENERIC_HURT);
		register(BuiltinSound.ENTITY_HOSTILE_SMALL_FALL, BuiltinSound.ENTITY_GENERIC_SMALL_FALL);
		register(BuiltinSound.ENTITY_HOSTILE_SPLASH, BuiltinSound.ENTITY_GENERIC_SPLASH);
		register(BuiltinSound.ENTITY_HOSTILE_SWIM, BuiltinSound.ENTITY_GENERIC_SWIM);
		
		register(BuiltinSound.ENTITY_HUSK_AMBIENT, "mob.husk.ambient");
		register(BuiltinSound.ENTITY_HUSK_DEATH, "mob.husk.death");
		register(BuiltinSound.ENTITY_HUSK_HURT, "mob.husk.hurt");
		register(BuiltinSound.ENTITY_HUSK_STEP, "mob.husk.step");
		
		register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_AMBIENT, BuiltinSound.ENTITY_EVOCATION_ILLAGER_AMBIENT);
		register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_CAST_SPELL, "mob.evocation_illager.cast_spell");
		register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_DEATH, BuiltinSound.ENTITY_EVOCATION_ILLAGER_DEATH);
		register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_HURT, BuiltinSound.ENTITY_EVOCATION_ILLAGER_HURT);
		register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_MIRROR_MOVE, null);
		register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_PREPARE_BLINDNESS, null);
		register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_PREPARE_MIRROR, null);
		
		register(BuiltinSound.ENTITY_IRONGOLEM_ATTACK, "mob.irongolem.throw");
		register(BuiltinSound.ENTITY_IRONGOLEM_DEATH, "mob.irongolem.death");
		register(BuiltinSound.ENTITY_IRONGOLEM_HURT, "mob.irongolem.hit");
		register(BuiltinSound.ENTITY_IRONGOLEM_STEP, "mob.irongolem.walk");
		
		register(BuiltinSound.ENTITY_ITEM_BREAK, "random.break");
		register(BuiltinSound.ENTITY_ITEM_PICKUP, "random.pop");
		
		register(BuiltinSound.ENTITY_ITEMFRAME_ADD_ITEM, "block.itemframe.add_item");
		register(BuiltinSound.ENTITY_ITEMFRAME_BREAK, "block.itemframe.break");
		register(BuiltinSound.ENTITY_ITEMFRAME_PLACE, "block.itemframe.place");
		register(BuiltinSound.ENTITY_ITEMFRAME_REMOVE_ITEM, "block.itemframe.remove_item");
		register(BuiltinSound.ENTITY_ITEMFRAME_ROTATE_ITEM, "block.itemframe.rotate_item");
		
		register(BuiltinSound.ENTITY_LEASHKNOT_BREAK, "leashknot.break");
		register(BuiltinSound.ENTITY_LEASHKNOT_PLACE, "leashknot.place");
		
		register(BuiltinSound.ENTITY_LIGHTNING_IMPACT, "ambient.weather.lightning.impact");
		register(BuiltinSound.ENTITY_LIGHTNING_THUNDER, "ambient.weather.thunder");
		
		register(BuiltinSound.ENTITY_LINGERINGPOTION_THROW, BuiltinSound.ENTITY_ARROW_SHOOT);
		
		register(BuiltinSound.ENTITY_LLAMA_AMBIENT, "mob.llama.idle");
		register(BuiltinSound.ENTITY_LLAMA_ANGRY, "mob.llama.angry");
		register(BuiltinSound.ENTITY_LLAMA_CHEST, BuiltinSound.ENTITY_CHICKEN_EGG);
		register(BuiltinSound.ENTITY_LLAMA_DEATH, "mob.llama.death");
		register(BuiltinSound.ENTITY_LLAMA_EAT, "mob.llama.eat");
		register(BuiltinSound.ENTITY_LLAMA_HURT, "mob.llama.hurt");
		register(BuiltinSound.ENTITY_LLAMA_SPIT, "mob.llama.spit");
		register(BuiltinSound.ENTITY_LLAMA_STEP, "mob.llama.step");
		register(BuiltinSound.ENTITY_LLAMA_SWAG, "mob.llama.swag");
		
		register(BuiltinSound.ENTITY_MAGMACUBE_DEATH, "mob.slime.big");
		register(BuiltinSound.ENTITY_MAGMACUBE_HURT, BuiltinSound.ENTITY_MAGMACUBE_DEATH);
		register(BuiltinSound.ENTITY_MAGMACUBE_JUMP, "mob.magmacube.jump");
		register(BuiltinSound.ENTITY_MAGMACUBE_SQUISH, "mob.magmacube.big");
		
		register(BuiltinSound.ENTITY_MINECART_INSIDE, "minecart.inside");
		register(BuiltinSound.ENTITY_MINECART_RIDING, "minecart.base");
		
		register(BuiltinSound.ENTITY_MOOSHROOM_SHEAR, "mob.sheep.shear");
		
		register(BuiltinSound.ENTITY_MULE_AMBIENT, "mob.horse.donkey.idle");
		register(BuiltinSound.ENTITY_MULE_CHEST, BuiltinSound.ENTITY_CHICKEN_EGG);
		register(BuiltinSound.ENTITY_MULE_DEATH, "mob.horse.donkey.death");
		register(BuiltinSound.ENTITY_MULE_HURT, "mob.horse.donkey.hit");
		
		register(BuiltinSound.ENTITY_PAINTING_BREAK, null);
		register(BuiltinSound.ENTITY_PAINTING_PLACE, null);
		
		register(BuiltinSound.ENTITY_PARROT_AMBIENT, "mob.parrot.idle");
		register(BuiltinSound.ENTITY_PARROT_DEATH, "mob.parrot.death");
		register(BuiltinSound.ENTITY_PARROT_EAT, "mob.parrot.eat");
		register(BuiltinSound.ENTITY_PARROT_FLY, "mob.parrot.fly");
		register(BuiltinSound.ENTITY_PARROT_HURT, "mob.parrot.hurt");
		register(BuiltinSound.ENTITY_PARROT_IMITATE_BLAZE, BuiltinSound.ENTITY_BLAZE_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_CREEPER, BuiltinSound.ENTITY_CREEPER_PRIMED);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_ELDER_GUARDIAN, BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_ENDERDRAGON, BuiltinSound.ENTITY_ENDERDRAGON_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_ENDERMAN, BuiltinSound.ENTITY_ENDERMEN_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_ENDERMITE, BuiltinSound.ENTITY_ENDERMITE_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_EVOCATION_ILLAGER, BuiltinSound.ENTITY_EVOCATION_ILLAGER_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_GHAST, BuiltinSound.ENTITY_GHAST_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_HUSK, BuiltinSound.ENTITY_HUSK_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_ILLUSION_ILLAGER, BuiltinSound.ENTITY_ILLUSION_ILLAGER_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_MAGMACUBE, BuiltinSound.ENTITY_MAGMACUBE_SQUISH);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_POLAR_BEAR, BuiltinSound.ENTITY_POLAR_BEAR_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_SHULKER, BuiltinSound.ENTITY_SHULKER_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_SILVERFISH, BuiltinSound.ENTITY_SILVERFISH_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_SKELETON, BuiltinSound.ENTITY_SKELETON_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_SLIME, BuiltinSound.ENTITY_SLIME_SQUISH);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_SPIDER, BuiltinSound.ENTITY_SPIDER_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_STRAY, BuiltinSound.ENTITY_STRAY_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_VEX, BuiltinSound.ENTITY_VEX_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_VINDICATION_ILLAGER, BuiltinSound.ENTITY_VINDICATION_ILLAGER_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_WITCH, BuiltinSound.ENTITY_WITCH_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_WITHER, BuiltinSound.ENTITY_WITHER_SKELETON_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_WITHER_SKELETON, BuiltinSound.ENTITY_WITHER_SKELETON_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_WOLF, BuiltinSound.ENTITY_WOLF_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE, BuiltinSound.ENTITY_ZOMBIE_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE_PIGMAN, BuiltinSound.ENTITY_ZOMBIE_PIG_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER, BuiltinSound.ENTITY_ZOMBIE_VILLAGER_AMBIENT);
		register(BuiltinSound.ENTITY_PARROT_STEP, "mob.parrot.step");
		
		register(BuiltinSound.ENTITY_PIG_AMBIENT, "mob.pig.say");
		register(BuiltinSound.ENTITY_PIG_DEATH, "mob.pig.death");
		register(BuiltinSound.ENTITY_PIG_HURT, "mob.pig.say");
		register(BuiltinSound.ENTITY_PIG_SADDLE, BuiltinSound.ENTITY_HORSE_SADDLE);
		register(BuiltinSound.ENTITY_PIG_STEP, "mob.pig.step");
		
		register(BuiltinSound.ENTITY_PLAYER_ATTACK_CRIT, null);
		register(BuiltinSound.ENTITY_PLAYER_ATTACK_KNOCKBACK, null);
		register(BuiltinSound.ENTITY_PLAYER_ATTACK_NODAMAGE, "game.player.attack.nodamage");
		register(BuiltinSound.ENTITY_PLAYER_ATTACK_STRONG, null);
		register(BuiltinSound.ENTITY_PLAYER_ATTACK_SWEEP, null);
		register(BuiltinSound.ENTITY_PLAYER_ATTACK_WEAK, BuiltinSound.ENTITY_PLAYER_ATTACK_NODAMAGE);
		register(BuiltinSound.ENTITY_PLAYER_BIG_FALL, BuiltinSound.ENTITY_GENERIC_BIG_FALL);
		register(BuiltinSound.ENTITY_PLAYER_BREATH, null);
		register(BuiltinSound.ENTITY_PLAYER_BURP, "random.burp");
		register(BuiltinSound.ENTITY_PLAYER_DEATH, "game.player.die");
		register(BuiltinSound.ENTITY_PLAYER_HURT, BuiltinSound.ENTITY_GENERIC_HURT);
		register(BuiltinSound.ENTITY_PLAYER_HURT_DROWN, BuiltinSound.ENTITY_PLAYER_HURT);
		register(BuiltinSound.ENTITY_PLAYER_HURT_ON_FIRE, BuiltinSound.ENTITY_PLAYER_HURT);
		register(BuiltinSound.ENTITY_PLAYER_LEVELUP, "random.levelup");
		register(BuiltinSound.ENTITY_PLAYER_SMALL_FALL, BuiltinSound.ENTITY_GENERIC_SMALL_FALL);
		register(BuiltinSound.ENTITY_PLAYER_SPLASH, BuiltinSound.ENTITY_GENERIC_SPLASH);
		register(BuiltinSound.ENTITY_PLAYER_SWIM, BuiltinSound.ENTITY_GENERIC_SWIM);
		
		register(BuiltinSound.ENTITY_POLAR_BEAR_AMBIENT, "mob.polarbear.idle");
		register(BuiltinSound.ENTITY_POLAR_BEAR_BABY_AMBIENT, "mob.polarbear_baby.idle");
		register(BuiltinSound.ENTITY_POLAR_BEAR_DEATH, "mob.polarbear.death");
		register(BuiltinSound.ENTITY_POLAR_BEAR_HURT, "mob.polarbear.hurt");
		register(BuiltinSound.ENTITY_POLAR_BEAR_STEP, "mob.polarbear.step");
		register(BuiltinSound.ENTITY_POLAR_BEAR_WARNING, "mob.polarbear.warning");
		
		register(BuiltinSound.ENTITY_RABBIT_AMBIENT, "mob.rabbit.idle");
		register(BuiltinSound.ENTITY_RABBIT_ATTACK, null);
		register(BuiltinSound.ENTITY_RABBIT_DEATH, "mob.rabbit.death");
		register(BuiltinSound.ENTITY_RABBIT_HURT, "mob.rabbit.hurt");
		register(BuiltinSound.ENTITY_RABBIT_JUMP, "mob.rabbit.hop");
		
		register(BuiltinSound.ENTITY_SHEEP_AMBIENT, "mob.sheep.say");
		register(BuiltinSound.ENTITY_SHEEP_DEATH, BuiltinSound.ENTITY_SHEEP_AMBIENT);
		register(BuiltinSound.ENTITY_SHEEP_HURT, BuiltinSound.ENTITY_SHEEP_AMBIENT);
		register(BuiltinSound.ENTITY_SHEEP_SHEAR, "mob.sheep.shear");
		register(BuiltinSound.ENTITY_SHEEP_STEP, "mob.sheep.step");
		
		register(BuiltinSound.ENTITY_SHULKER_AMBIENT, "mob.shulker.ambient");
		register(BuiltinSound.ENTITY_SHULKER_CLOSE, "mob.shulker.close");
		register(BuiltinSound.ENTITY_SHULKER_DEATH, "mob.shulker.death");
		register(BuiltinSound.ENTITY_SHULKER_HURT, "mob.shulker.hurt");
		register(BuiltinSound.ENTITY_SHULKER_HURT_CLOSED, "mob.shulker.close.hurt");
		register(BuiltinSound.ENTITY_SHULKER_OPEN, "mob.shulker.open");
		register(BuiltinSound.ENTITY_SHULKER_SHOOT, "mob.shulker.shoot");
		register(BuiltinSound.ENTITY_SHULKER_TELEPORT, "mob.shulker.teleport");
		register(BuiltinSound.ENTITY_SHULKER_BULLET_HIT, "mob.shulker.bullet.hit");
		register(BuiltinSound.ENTITY_SHULKER_BULLET_HURT, BuiltinSound.ENTITY_SHULKER_BULLET_HIT);
		
		register(BuiltinSound.ENTITY_SILVERFISH_AMBIENT, "mob.silverfish.say");
		register(BuiltinSound.ENTITY_SILVERFISH_DEATH, "mob.silverfish.kill");
		register(BuiltinSound.ENTITY_SILVERFISH_HURT, "mob.silverfish.hit");
		register(BuiltinSound.ENTITY_SILVERFISH_STEP, "mob.silverfish.step");
		
		register(BuiltinSound.ENTITY_SKELETON_AMBIENT, "mob.skeleton.say");
		register(BuiltinSound.ENTITY_SKELETON_DEATH, "mob.skeleton.death");
		register(BuiltinSound.ENTITY_SKELETON_HURT, "mob.skeleton.hurt");
		register(BuiltinSound.ENTITY_SKELETON_SHOOT, BuiltinSound.ENTITY_ARROW_SHOOT);
		register(BuiltinSound.ENTITY_SKELETON_STEP, "mob.skeleton.step");
		
		register(BuiltinSound.ENTITY_SKELETON_HORSE_AMBIENT, "mob.horse.skeleton.idle");
		register(BuiltinSound.ENTITY_SKELETON_HORSE_DEATH, "mob.horse.skeleton.death");
		register(BuiltinSound.ENTITY_SKELETON_HORSE_HURT, "mob.horse.skeleton.hit");
		
		register(BuiltinSound.ENTITY_SLIME_ATTACK, "mob.slime.attack");
		register(BuiltinSound.ENTITY_SLIME_DEATH, "mob.slime.death");
		register(BuiltinSound.ENTITY_SLIME_HURT, "mob.slime.hurt");
		register(BuiltinSound.ENTITY_SLIME_JUMP, "mob.slime.jump");
		register(BuiltinSound.ENTITY_SLIME_SQUISH, "mob.slime.squish");
		
		register(BuiltinSound.ENTITY_SMALL_MAGMACUBE_DEATH, BuiltinSound.ENTITY_SMALL_SLIME_HURT);
		register(BuiltinSound.ENTITY_SMALL_MAGMACUBE_HURT, BuiltinSound.ENTITY_SMALL_SLIME_HURT);
		register(BuiltinSound.ENTITY_SMALL_MAGMACUBE_SQUISH, "mob.magmacube.small");
		
		register(BuiltinSound.ENTITY_SMALL_SLIME_DEATH, BuiltinSound.ENTITY_SMALL_SLIME_HURT);
		register(BuiltinSound.ENTITY_SMALL_SLIME_HURT, "mob.slime.small");
		register(BuiltinSound.ENTITY_SMALL_SLIME_JUMP, false);
		register(BuiltinSound.ENTITY_SMALL_SLIME_SQUISH, false);
		
		register(BuiltinSound.ENTITY_SNOWBALL_THROW, BuiltinSound.ENTITY_ARROW_SHOOT);
		
		register(BuiltinSound.ENTITY_SNOWMAN_AMBIENT, false);
		register(BuiltinSound.ENTITY_SNOWMAN_DEATH, null);
		register(BuiltinSound.ENTITY_SNOWMAN_HURT, null);
		register(BuiltinSound.ENTITY_SNOWMAN_SHOOT, BuiltinSound.ENTITY_ARROW_SHOOT);
		
		register(BuiltinSound.ENTITY_SPIDER_AMBIENT, "mob.spider.say");
		register(BuiltinSound.ENTITY_SPIDER_DEATH, "mob.spider.death");
		register(BuiltinSound.ENTITY_SPIDER_HURT, BuiltinSound.ENTITY_SPIDER_AMBIENT);
		register(BuiltinSound.ENTITY_SPIDER_STEP, "mob.spider.step");
		
		register(BuiltinSound.ENTITY_SPLASH_POTION_BREAK, BuiltinSound.BLOCK_GLASS_BREAK);
		register(BuiltinSound.ENTITY_SPLASH_POTION_THROW, BuiltinSound.ENTITY_ARROW_SHOOT);
		
		register(BuiltinSound.ENTITY_SQUID_AMBIENT, "mob.squid.ambient");
		register(BuiltinSound.ENTITY_SQUID_DEATH, "mob.squid.death");
		register(BuiltinSound.ENTITY_SQUID_HURT, "mob.squid.hurt");
		
		register(BuiltinSound.ENTITY_STRAY_AMBIENT, "mob.stray.ambient");
		register(BuiltinSound.ENTITY_STRAY_DEATH, "mob.stray.death");
		register(BuiltinSound.ENTITY_STRAY_HURT, "mob.stray.hurt");
		register(BuiltinSound.ENTITY_STRAY_STEP, "mob.stray.step");
		
		register(BuiltinSound.ENTITY_TNT_PRIMED, BuiltinSound.ENTITY_CREEPER_PRIMED);
		
		register(BuiltinSound.ENTITY_VEX_AMBIENT, "mob.vex.ambient");
		register(BuiltinSound.ENTITY_VEX_CHARGE, "mob.vex.charge");
		register(BuiltinSound.ENTITY_VEX_DEATH, "mob.vex.death");
		register(BuiltinSound.ENTITY_VEX_HURT, "mob.vex.hurt");
		
		register(BuiltinSound.ENTITY_VILLAGER_AMBIENT, "mob.villager.idle");
		register(BuiltinSound.ENTITY_VILLAGER_DEATH, "mob.villager.death");
		register(BuiltinSound.ENTITY_VILLAGER_HURT, "mob.villager.hit");
		register(BuiltinSound.ENTITY_VILLAGER_NO, "mob.villager.no");
		register(BuiltinSound.ENTITY_VILLAGER_TRADING, "mob.villager.haggle");
		register(BuiltinSound.ENTITY_VILLAGER_YES, "mob.villager.yes");
		
		register(BuiltinSound.ENTITY_VINDICATION_ILLAGER_AMBIENT, "mob.vindicator.idle");
		register(BuiltinSound.ENTITY_VINDICATION_ILLAGER_DEATH, "mob.vindicator.death");
		register(BuiltinSound.ENTITY_VINDICATION_ILLAGER_HURT, "mob.vindicator.hurt");
		
		register(BuiltinSound.ENTITY_WITCH_AMBIENT, "mob.witch.ambient");
		register(BuiltinSound.ENTITY_WITCH_DEATH, "mob.witch.death");
		register(BuiltinSound.ENTITY_WITCH_DRINK, "mob.witch.drink");
		register(BuiltinSound.ENTITY_WITCH_HURT, "mob.witch.hurt");
		register(BuiltinSound.ENTITY_WITCH_THROW, "mob.witch.throw");
		
		register(BuiltinSound.ENTITY_WITHER_AMBIENT, "mob.wither.ambient");
		register(BuiltinSound.ENTITY_WITHER_BREAK_BLOCK, "mob.wither.break_block");
		register(BuiltinSound.ENTITY_WITHER_DEATH, "mob.wither.death");
		register(BuiltinSound.ENTITY_WITHER_HURT, "mob.wither.hurt");
		register(BuiltinSound.ENTITY_WITHER_SHOOT, "mob.wither.shoot");
		register(BuiltinSound.ENTITY_WITHER_SPAWN, "mob.wither.spawn");
		
		register(BuiltinSound.ENTITY_WITHER_SKELETON_AMBIENT, BuiltinSound.ENTITY_SKELETON_AMBIENT);
		register(BuiltinSound.ENTITY_WITHER_SKELETON_DEATH, BuiltinSound.ENTITY_SKELETON_DEATH);
		register(BuiltinSound.ENTITY_WITHER_SKELETON_HURT, BuiltinSound.ENTITY_SKELETON_HURT);
		register(BuiltinSound.ENTITY_WITHER_SKELETON_STEP, BuiltinSound.ENTITY_SKELETON_STEP);
		
		register(BuiltinSound.ENTITY_WOLF_AMBIENT, "mob.wolf.bark");
		register(BuiltinSound.ENTITY_WOLF_DEATH, "mob.wolf.death");
		register(BuiltinSound.ENTITY_WOLF_GROWL, "mob.wolf.growl");
		register(BuiltinSound.ENTITY_WOLF_HOWL, null);
		register(BuiltinSound.ENTITY_WOLF_HURT, "mob.wolf.hurt");
		register(BuiltinSound.ENTITY_WOLF_PANT, "mob.wolf.panting");
		register(BuiltinSound.ENTITY_WOLF_SHAKE, "mob.wolf.shake");
		register(BuiltinSound.ENTITY_WOLF_STEP, "mob.wolf.step");
		register(BuiltinSound.ENTITY_WOLF_WHINE, "mob.wolf.whine");
		
		register(BuiltinSound.ENTITY_ZOMBIE_AMBIENT, "mob.zombie.say");
		register(BuiltinSound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, "mob.zombie.wood");
		register(BuiltinSound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, BuiltinSound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD);
		register(BuiltinSound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, "mob.zombie.woodbreak");
		register(BuiltinSound.ENTITY_ZOMBIE_DEATH, "mob.zombie.death");
		register(BuiltinSound.ENTITY_ZOMBIE_HURT, "mob.zombie.hurt");
		register(BuiltinSound.ENTITY_ZOMBIE_INFECT, BuiltinSound.ENTITY_ZOMBIE_VILLAGER_CONVERTED);
		register(BuiltinSound.ENTITY_ZOMBIE_STEP, "mob.zombie.step");
		register(BuiltinSound.ENTITY_ZOMBIE_HORSE_AMBIENT, "mob.horse.zombie.idle");
		register(BuiltinSound.ENTITY_ZOMBIE_HORSE_DEATH, "mob.horse.zombie.death");
		register(BuiltinSound.ENTITY_ZOMBIE_HORSE_HURT, "mob.horse.zombie.hit");
		
		register(BuiltinSound.ENTITY_ZOMBIE_PIG_AMBIENT, "mob.zombiepig.zpig");
		register(BuiltinSound.ENTITY_ZOMBIE_PIG_ANGRY, "mob.zombiepig.zpigangry");
		register(BuiltinSound.ENTITY_ZOMBIE_PIG_DEATH, "mob.zombiepig.zpigdeath");
		register(BuiltinSound.ENTITY_ZOMBIE_PIG_HURT, "mob.zombiepig.zpighurt");
		
		register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, "mob.zombie_villager.say");
		register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, "mob.zombie.unfect");
		register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_CURE, "mob.zombie.remedy");
		register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_DEATH, "mob.zombie_villager.death");
		register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_HURT, "mob.zombie_villager.hurt");
		register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_STEP, BuiltinSound.ENTITY_ZOMBIE_STEP);
		
		register(BuiltinSound.ITEM_ARMOR_EQUIP_CHAIN, null);
		register(BuiltinSound.ITEM_ARMOR_EQUIP_DIAMOND, null);
		register(BuiltinSound.ITEM_ARMOR_EQUIP_ELYTRA, null);
		register(BuiltinSound.ITEM_ARMOR_EQUIP_GENERIC, null);
		register(BuiltinSound.ITEM_ARMOR_EQUIP_GOLD, null);
		register(BuiltinSound.ITEM_ARMOR_EQUIP_IRON, null);
		register(BuiltinSound.ITEM_ARMOR_EQUIP_LEATHER, null);
		
		register(BuiltinSound.ITEM_BOTTLE_EMPTY, BuiltinSound.ITEM_BUCKET_EMPTY);
		register(BuiltinSound.ITEM_BOTTLE_FILL, BuiltinSound.ITEM_BUCKET_FILL);
		register(BuiltinSound.ITEM_BOTTLE_FILL_DRAGONBREATH, null);
		
		register(BuiltinSound.ITEM_BUCKET_EMPTY, "bucket.empty_water");
		register(BuiltinSound.ITEM_BUCKET_EMPTY_LAVA, "bucket.empty_lava");
		register(BuiltinSound.ITEM_BUCKET_FILL, "bucket.fill_water");
		register(BuiltinSound.ITEM_BUCKET_FILL_LAVA, "bucket.fill_lava");
		
		register(BuiltinSound.ITEM_CHORUS_FRUIT_TELEPORT, BuiltinSound.ENTITY_ENDERMEN_TELEPORT);
		
		register(BuiltinSound.ITEM_ELYTRA_FLYING, "elytra.loop");
		
		register(BuiltinSound.ITEM_FIRECHARGE_USE, BuiltinSound.ENTITY_GHAST_SHOOT);
		
		register(BuiltinSound.ITEM_FLINTANDSTEEL_USE, "fire.ignit");
		
		register(BuiltinSound.ITEM_HOE_TILL, null);
		
		register(BuiltinSound.ITEM_SHIELD_BLOCK, null);
		
		register(BuiltinSound.ITEM_SHIELD_BREAK, BuiltinSound.ENTITY_ITEM_BREAK);
		
		register(BuiltinSound.ITEM_SHOVEL_FLATTEN, null);
		
		register(BuiltinSound.ITEM_TOTEM_USE, "random.totem");
		
		register(BuiltinSound.MUSIC_CREATIVE, "music.game.creative");
		register(BuiltinSound.MUSIC_CREDITS, "music.game.credits");
		register(BuiltinSound.MUSIC_DRAGON, "music.game.endboss");
		register(BuiltinSound.MUSIC_END, "music.game.end");
		register(BuiltinSound.MUSIC_GAME, "music.game");
		register(BuiltinSound.MUSIC_MENU, "music.menu");
		register(BuiltinSound.MUSIC_NETHER, "music.game.nether");
		
		register(BuiltinSound.RECORD_11, "record.11");
		register(BuiltinSound.RECORD_13, "record.13");
		register(BuiltinSound.RECORD_BLOCKS, "record.blocks");
		register(BuiltinSound.RECORD_CAT, "record.cat");
		register(BuiltinSound.RECORD_CHIRP, "record.chirp");
		register(BuiltinSound.RECORD_FAR, "record.far");
		register(BuiltinSound.RECORD_MALL, "record.mall");
		register(BuiltinSound.RECORD_MELLOHI, "record.mellohi");
		register(BuiltinSound.RECORD_STAL, "record.stal");
		register(BuiltinSound.RECORD_STRAD, "record.strad");
		register(BuiltinSound.RECORD_WAIT, "record.wait");
		register(BuiltinSound.RECORD_WARD, "record.ward");
		
		register(BuiltinSound.UI_BUTTON_CLICK, "random.click");
		register(BuiltinSound.UI_TOAST_IN, "random.toast");
		register(BuiltinSound.UI_TOAST_OUT, BuiltinSound.UI_TOAST_IN);
		register(BuiltinSound.UI_TOAST_CHALLENGE_COMPLETE, BuiltinSound.UI_TOAST_IN);
		
		register(BuiltinSound.WEATHER_RAIN, "ambient.weather.rain");
		register(BuiltinSound.WEATHER_RAIN_ABOVE, BuiltinSound.WEATHER_RAIN);

		for (Entry<BuiltinSound, BuiltinSound> en : pceq.entrySet()) {
			registerFromEq(en.getKey(), en.getValue());
		}
		
		DragonProxy.getInstance().getLogger().info("Loaded sound translates: "+pctope.size());
	}
	
	private void registerFromEq(BuiltinSound shortcut, BuiltinSound parent) {
		DragonProxy.getInstance().getLogger().info("Checking registry of shortcut: "+shortcut.toString());
		if(parent == shortcut) {
			return; // WHAT???
		}
		if(pctope.containsKey(shortcut)) {
			return; // Already registered
		}
		if(!pctope.containsKey(parent) && pceq.containsKey(parent)) {
			registerFromEq(parent, pceq.get(parent)); // Register parent shortcut into sound list
		}
		if (pctope.containsKey(parent)) {
			pctope.put(shortcut, pctope.get(parent)); // Register into sound list
			DragonProxy.getInstance().getLogger().info("Register shortcut \""+shortcut.toString()+"\" to translates (Parent: \""+parent.toString()+"\") ");
		}
	}

	private boolean register(BuiltinSound bs, Object pe) {
		if (bs == null) {
			return false;
		}
		if (pctope.containsKey(bs) || ignore.contains(bs) || pceq.containsKey(bs)) {
			return false;
		}
		if (pe instanceof String) {
			pctope.put(bs, (String) pe);
			DragonProxy.getInstance().getLogger().info("Register translate of \""+bs.toString()+"\" to \""+pe+"\" ");
			return true;
		} else if (pe instanceof BuiltinSound) {
			pceq.put(bs, (BuiltinSound) pe);
			DragonProxy.getInstance().getLogger().info("Register translate of \""+bs.toString()+"\" to shortcut of \""+pe.toString()+"\" ");
			return true;
		} else if (pe instanceof Boolean) {
			if ((boolean) pe == false) {
				ignore.add(bs);
				DragonProxy.getInstance().getLogger().info("Register translate of \""+bs.toString()+"\" to ignore packet ");
			}
			return true;
		}
		return false;
	}
}
