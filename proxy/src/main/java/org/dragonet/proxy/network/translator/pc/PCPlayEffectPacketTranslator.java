package org.dragonet.proxy.network.translator.pc;

import java.util.ArrayList;
import java.util.HashMap;

import org.dragonet.common.maths.BlockPosition;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.packets.LevelEventPacket;
import org.dragonet.protocol.packets.PlaySoundPacket;
import org.dragonet.protocol.packets.StopSoundPacket;
import org.dragonet.protocol.packets.TextPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.common.data.ParticleEffects;

import com.github.steveice10.mc.protocol.data.game.world.effect.BreakBlockEffectData;
import com.github.steveice10.mc.protocol.data.game.world.effect.ParticleEffect;
import com.github.steveice10.mc.protocol.data.game.world.effect.RecordEffectData;
import com.github.steveice10.mc.protocol.data.game.world.effect.SoundEffect;
import com.github.steveice10.mc.protocol.data.game.world.effect.WorldEffect;
import com.github.steveice10.mc.protocol.data.game.world.effect.WorldEffectData;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import org.dragonet.api.sessions.IUpstreamSession;

public class PCPlayEffectPacketTranslator implements IPCPacketTranslator<ServerPlayEffectPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerPlayEffectPacket packet) {
        WorldEffect effect = packet.getEffect();
        WorldEffectData data = packet.getData();
        ArrayList<PEPacket> packets = new ArrayList<PEPacket>();
        if (effect == SoundEffect.RECORD && data instanceof RecordEffectData)
            if (records.containsKey(((RecordEffectData) data).getRecordId())) {
                PlaySoundPacket psp = new PlaySoundPacket();
                psp.name = DragonProxy.getInstance().getSoundTranslator().translate(records.get(((RecordEffectData) data).getRecordId()));
                psp.blockPosition = new BlockPosition(packet.getPosition());
                psp.pitch = 1;
                psp.volume = 10;
                packets.add(psp);
                session.getJukeboxCache().registerJukebox(new BlockPosition(packet.getPosition()), records.get(((RecordEffectData) data).getRecordId()));
                if (record_names.containsKey(((RecordEffectData) data).getRecordId())) {
                    TextPacket text = new TextPacket();
                    text.type = TextPacket.TYPE_JUKEBOX_POPUP;
                    text.message = "Now playing: C418 - " + record_names.get(((RecordEffectData) data).getRecordId());
                    packets.add(text);
                }
            } else if (((RecordEffectData) data).getRecordId() == 0) {
                BuiltinSound sound = session.getJukeboxCache().unregisterJukebox(new BlockPosition(packet.getPosition()));
                if (sound != null) {
                    StopSoundPacket ssp = new StopSoundPacket();
                    ssp.name = DragonProxy.getInstance().getSoundTranslator().translate(sound);
                    packets.add(ssp);
                }
            } else {
                // Something is wrong
            }
        else if (effect == ParticleEffect.BREAK_BLOCK && data instanceof BreakBlockEffectData) {
            // I don't know where is this packet used
            PlaySoundPacket psp = new PlaySoundPacket();
            if (breakBlock.containsKey(((BreakBlockEffectData) data).getBlockState().getId()))
                psp.name = DragonProxy.getInstance().getSoundTranslator().translate(breakBlock.get(((BreakBlockEffectData) data).getBlockState().getId()));
            else
                psp.name = DragonProxy.getInstance().getSoundTranslator().translate(breakBlock.get(1));
            psp.blockPosition = new BlockPosition(packet.getPosition());
            psp.pitch = 1;
            psp.volume = 10;
            packets.add(psp);
        } else if (sounds.containsKey(effect))
            // This packet is used only when effect is from block not entity
            // ex. pressure plate -> door, but when player open door I don't hear anything
            if (!DragonProxy.getInstance().getSoundTranslator().isIgnored(sounds.get(effect)) && DragonProxy.getInstance().getSoundTranslator().isTranslatable(sounds.get(effect))) {
                PlaySoundPacket psp = new PlaySoundPacket();
                psp.name = DragonProxy.getInstance().getSoundTranslator().translate(sounds.get(effect));
                psp.blockPosition = new BlockPosition(packet.getPosition());
                psp.pitch = 1;
                psp.volume = 1;
                packets.add(psp);
            }
        if (effect == ParticleEffect.SMOKE) {
            LevelEventPacket pk = new LevelEventPacket();
            pk.eventId = LevelEventPacket.EVENT_ADD_PARTICLE_MASK;
            pk.position = new Vector3F(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
            pk.data = ParticleEffects.TYPE_SMOKE.id;
            packets.add(pk);
        }
        if (effect == ParticleEffect.BONEMEAL_GROW) {
            LevelEventPacket pk = new LevelEventPacket();
            pk.eventId = LevelEventPacket.EVENT_PARTICLE_BONEMEAL;
            pk.position = new Vector3F(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
            pk.data = 0;
            packets.add(pk);
        }
        if (effect == ParticleEffect.BREAK_BLOCK) {
            // I don't know hot to test it // TODO
        }
        if (effect == ParticleEffect.BREAK_EYE_OF_ENDER) {
            LevelEventPacket pk = new LevelEventPacket();
            pk.eventId = LevelEventPacket.EVENT_PARTICLE_EYE_DESPAWN;
            pk.position = new Vector3F(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
            pk.data = 0;
            packets.add(pk);
        }
        if (effect == ParticleEffect.BREAK_SPLASH_POTION) {
            LevelEventPacket pk = new LevelEventPacket();
            pk.eventId = LevelEventPacket.EVENT_PARTICLE_SPLASH;
            pk.position = new Vector3F(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
            pk.data = 0; // TODO
            packets.add(pk);
        }
        if (effect == ParticleEffect.END_GATEWAY_SPAWN) {
            // TODO
        }
        if (effect == ParticleEffect.ENDERDRAGON_FIREBALL_EXPLODE) {
            // TODO
        }
        if (effect == ParticleEffect.MOB_SPAWN) {
            LevelEventPacket pk = new LevelEventPacket();
            pk.eventId = LevelEventPacket.EVENT_PARTICLE_SPAWN;
            pk.position = new Vector3F(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
            pk.data = 2;
            packets.add(pk);
        }
        if (!packets.isEmpty())
            return packets.toArray(new PEPacket[packets.size()]);
        else
            return null;
    }

    private static HashMap<WorldEffect, BuiltinSound> sounds = new HashMap<WorldEffect, BuiltinSound>();
    private static HashMap<Integer, BuiltinSound> breakBlock = new HashMap<Integer, BuiltinSound>();
    private static HashMap<Integer, BuiltinSound> records = new HashMap<Integer, BuiltinSound>();
    private static HashMap<Integer, String> record_names = new HashMap<Integer, String>();

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
        sounds.put(ParticleEffect.BREAK_SPLASH_POTION, BuiltinSound.BLOCK_GLASS_BREAK);

        records.put(2256, BuiltinSound.RECORD_13); // record_13
        records.put(2257, BuiltinSound.RECORD_CAT); // record_cat
        records.put(2258, BuiltinSound.RECORD_BLOCKS); // record_blocks
        records.put(2259, BuiltinSound.RECORD_CHIRP); // record_chirp
        records.put(2260, BuiltinSound.RECORD_FAR); // record_far
        records.put(2261, BuiltinSound.RECORD_MALL); // record_mall
        records.put(2262, BuiltinSound.RECORD_MELLOHI); // record_mellohi
        records.put(2263, BuiltinSound.RECORD_STAL); // record_stal
        records.put(2264, BuiltinSound.RECORD_STRAD); // record_strad
        records.put(2265, BuiltinSound.RECORD_WARD); // record_ward
        records.put(2266, BuiltinSound.RECORD_11); // record_11
        records.put(2267, BuiltinSound.RECORD_WAIT); // record_wait

        record_names.put(2256, "13"); // record_13
        record_names.put(2257, "Cat"); // record_cat
        record_names.put(2258, "Blocks"); // record_blocks
        record_names.put(2259, "Chirp"); // record_chirp
        record_names.put(2260, "Far"); // record_far
        record_names.put(2261, "Mall"); // record_mall
        record_names.put(2262, "Mellohi"); // record_mellohi
        record_names.put(2263, "Stal"); // record_stal
        record_names.put(2264, "Strad"); // record_strad
        record_names.put(2265, "Ward"); // record_ward
        record_names.put(2266, "11"); // record_11
        record_names.put(2267, "Wait"); // record_wait

        breakBlock.put(1, BuiltinSound.BLOCK_STONE_BREAK);
        breakBlock.put(2, BuiltinSound.BLOCK_GRASS_BREAK);
        breakBlock.put(3, BuiltinSound.BLOCK_GRAVEL_BREAK);
        breakBlock.put(4, BuiltinSound.BLOCK_STONE_BREAK);
        breakBlock.put(5, BuiltinSound.BLOCK_WOOD_BREAK);
        breakBlock.put(6, BuiltinSound.BLOCK_GRASS_BREAK);
        breakBlock.put(12, BuiltinSound.BLOCK_SAND_BREAK);
        breakBlock.put(13, BuiltinSound.BLOCK_GRAVEL_BREAK);
    }

}
