package org.dragonet.proxy.network.translator.pc;

import java.util.HashMap;

import org.dragonet.common.maths.BlockPosition;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.PlaySoundPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;

import com.github.steveice10.mc.protocol.data.game.world.effect.ParticleEffect;
import com.github.steveice10.mc.protocol.data.game.world.effect.SoundEffect;
import com.github.steveice10.mc.protocol.data.game.world.effect.WorldEffect;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;

public class PCPlayEffectPacketTranslator implements IPCPacketTranslator<ServerPlayEffectPacket> {

	@Override
	public PEPacket[] translate(UpstreamSession session, ServerPlayEffectPacket packet) {
		DragonProxy.getInstance().getLogger().info("WorldEffect: "+packet.getEffect().toString());
		WorldEffect effect = packet.getEffect();
		PlaySoundPacket psp = null;
		if(sounds.containsKey(effect)) {
			if(!DragonProxy.getInstance().getSoundTranslator().isIgnored(sounds.get(effect)) && DragonProxy.getInstance().getSoundTranslator().isTranslatable(sounds.get(effect))) {
				psp = new PlaySoundPacket();
				psp.name = DragonProxy.getInstance().getSoundTranslator().translate(sounds.get(effect));
				psp.blockPosition = new BlockPosition(packet.getPosition());
				psp.pitch = 1;
				psp.volume = 10;
				DragonProxy.getInstance().getLogger().info("Sound: "+ DragonProxy.getInstance().getSoundTranslator().translate(sounds.get(effect)));
			}
		}
		//TODO Other effects
		if (psp != null) {
			DragonProxy.getInstance().getLogger().info("Sending packet");
			return new PEPacket[] {psp};
		} else {
			return null;
		}
	}

	private static HashMap<WorldEffect, BuiltinSound> sounds = new HashMap<WorldEffect, BuiltinSound>(); 
	
	static {
		sounds.put(SoundEffect.BLOCK_DISPENSER_DISPENSE, BuiltinSound.BLOCK_DISPENSER_DISPENSE);
		sounds.put(SoundEffect.BLOCK_DISPENSER_FAIL, BuiltinSound.BLOCK_DISPENSER_FAIL);
		sounds.put(SoundEffect.BLOCK_DISPENSER_LAUNCH, BuiltinSound.BLOCK_DISPENSER_LAUNCH);
		sounds.put(SoundEffect.ENTITY_ENDEREYE_LAUNCH, BuiltinSound.ENTITY_ENDEREYE_LAUNCH);
		sounds.put(SoundEffect.ENTITY_FIREWORK_SHOOT, BuiltinSound.ENTITY_FIREWORK_SHOOT);
		sounds.put(SoundEffect.BLOCK_IRON_DOOR_OPEN, BuiltinSound.BLOCK_IRON_DOOR_OPEN);
		sounds.put(SoundEffect.BLOCK_WOODEN_DOOR_OPEN, BuiltinSound.BLOCK_WOODEN_DOOR_OPEN);
		sounds.put(SoundEffect.BLOCK_WOODEN_TRAPDOOR_OPEN, BuiltinSound.BLOCK_WOODEN_TRAPDOOR_OPEN);
		sounds.put(SoundEffect.BLOCK_FENCE_GATE_OPEN, BuiltinSound.BLOCK_FENCE_GATE_OPEN);
		sounds.put(SoundEffect.BLOCK_FIRE_EXTINGUISH, BuiltinSound.BLOCK_FIRE_EXTINGUISH);
		sounds.put(SoundEffect.BLOCK_IRON_DOOR_CLOSE, BuiltinSound.BLOCK_IRON_DOOR_CLOSE);
		sounds.put(SoundEffect.BLOCK_WOODEN_DOOR_CLOSE, BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE);
		sounds.put(SoundEffect.BLOCK_WOODEN_TRAPDOOR_CLOSE, BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE);
		sounds.put(SoundEffect.BLOCK_FENCE_GATE_CLOSE, BuiltinSound.BLOCK_FENCE_GATE_CLOSE);
		sounds.put(SoundEffect.ENTITY_GHAST_WARN, BuiltinSound.ENTITY_GHAST_WARN);
		sounds.put(SoundEffect.ENTITY_GHAST_SHOOT, BuiltinSound.ENTITY_GHAST_SHOOT);
		sounds.put(SoundEffect.ENTITY_ENDERDRAGON_SHOOT, BuiltinSound.ENTITY_ENDERDRAGON_SHOOT);
		sounds.put(SoundEffect.ENTITY_BLAZE_SHOOT, BuiltinSound.ENTITY_BLAZE_SHOOT);
		sounds.put(SoundEffect.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, BuiltinSound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD);
		sounds.put(SoundEffect.ENTITY_ZOMBIE_ATTACK_DOOR_IRON, BuiltinSound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR);
		sounds.put(SoundEffect.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, BuiltinSound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD);
		sounds.put(SoundEffect.ENTITY_WITHER_BREAK_BLOCK, BuiltinSound.ENTITY_WITHER_BREAK_BLOCK);
		sounds.put(SoundEffect.ENTITY_WITHER_SPAWN, BuiltinSound.ENTITY_WITHER_SPAWN);
		sounds.put(SoundEffect.ENTITY_WITHER_SHOOT, BuiltinSound.ENTITY_WITHER_SHOOT);
		sounds.put(SoundEffect.ENTITY_BAT_TAKEOFF, BuiltinSound.ENTITY_BAT_TAKEOFF);
		sounds.put(SoundEffect.ENTITY_ZOMBIE_INFECT, BuiltinSound.ENTITY_ZOMBIE_INFECT);
		sounds.put(SoundEffect.ENTITY_ZOMBIE_VILLAGER_CONVERTED, BuiltinSound.ENTITY_ZOMBIE_VILLAGER_CONVERTED);
		sounds.put(SoundEffect.ENTITY_ENDERDRAGON_DEATH, BuiltinSound.ENTITY_ENDERDRAGON_DEATH);
		sounds.put(SoundEffect.BLOCK_ANVIL_DESTROY, BuiltinSound.BLOCK_ANVIL_DESTROY);
		sounds.put(SoundEffect.BLOCK_ANVIL_USE, BuiltinSound.BLOCK_ANVIL_USE);
		sounds.put(SoundEffect.BLOCK_ANVIL_LAND, BuiltinSound.BLOCK_ANVIL_LAND);
		sounds.put(SoundEffect.BLOCK_PORTAL_TRAVEL, BuiltinSound.BLOCK_PORTAL_TRAVEL);
		sounds.put(SoundEffect.BLOCK_CHORUS_FLOWER_GROW, BuiltinSound.BLOCK_CHORUS_FLOWER_GROW);
		sounds.put(SoundEffect.BLOCK_CHORUS_FLOWER_DEATH, BuiltinSound.BLOCK_CHORUS_FLOWER_DEATH);
		sounds.put(SoundEffect.BLOCK_BREWING_STAND_BREW, BuiltinSound.BLOCK_BREWING_STAND_BREW);
		sounds.put(SoundEffect.BLOCK_IRON_TRAPDOOR_OPEN, BuiltinSound.BLOCK_IRON_TRAPDOOR_OPEN);
		sounds.put(SoundEffect.BLOCK_IRON_TRAPDOOR_CLOSE, BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE);
		sounds.put(ParticleEffect.BREAK_EYE_OF_ENDER, BuiltinSound.ENTITY_ENDEREYE_DEATH);
		sounds.put(SoundEffect.ENTITY_ENDERDRAGON_GROWL, BuiltinSound.ENTITY_ENDERDRAGON_GROWL);
	}

}
