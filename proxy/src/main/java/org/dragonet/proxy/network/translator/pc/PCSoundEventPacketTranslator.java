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
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.LevelSoundEventPacket;


public class PCSoundEventPacketTranslator implements IPCPacketTranslator<ServerPlayBuiltinSoundPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerPlayBuiltinSoundPacket packet) {
        LevelSoundEventPacket pk = new LevelSoundEventPacket();

        //System.out.println("BuiltIn Sound packet: " + packet.getSound().name());

        pk.position = new Vector3F((float) packet.getX(), (float) packet.getY(), (float) packet.getZ());

        switch (packet.getSound()) {

            case BLOCK_CHEST_OPEN:
                pk.sound = LevelSoundEventPacket.Sound.CHEST_OPEN;
                break;
            case BLOCK_CHEST_CLOSE:
                pk.sound = LevelSoundEventPacket.Sound.CHEST_CLOSED;
                break;
            case BLOCK_SHULKER_BOX_OPEN:
                pk.sound = LevelSoundEventPacket.Sound.SHULKER_BOX_OPEN;
                break;
            case BLOCK_SHULKER_BOX_CLOSE:
                pk.sound = LevelSoundEventPacket.Sound.SHULKER_BOX_CLOSE;
                break;
            case ENCHANT_THORNS_HIT:
                pk.sound = LevelSoundEventPacket.Sound.THORNS;
                break;
            case ENTITY_ARROW_SHOOT:
                pk.sound = LevelSoundEventPacket.Sound.SHOOT;
                break;
            case ENTITY_ARROW_HIT_PLAYER:
                pk.sound = LevelSoundEventPacket.Sound.HIT;
                break;
            case ENTITY_ARROW_HIT:
                pk.sound = LevelSoundEventPacket.Sound.BOW_HIT;
                break;
            case ENTITY_GENERIC_EXTINGUISH_FIRE:
            	pk.sound = LevelSoundEventPacket.Sound.FIZZ;
                break;
            case BLOCK_FIRE_EXTINGUISH:
                pk.sound = LevelSoundEventPacket.Sound.EXTINGUISH_FIRE;
                break;
            case ENTITY_CAT_PURR:
                pk.sound = LevelSoundEventPacket.Sound.PURR;
                break;
            case ENTITY_CAT_PURREOW:
                pk.sound = LevelSoundEventPacket.Sound.PURREOW;
                break;
            case ENTITY_HORSE_GALLOP:
                pk.sound = LevelSoundEventPacket.Sound.GALLOP;
                break;
            case ENTITY_LIGHTNING_THUNDER:
                pk.sound = LevelSoundEventPacket.Sound.THUNDER;
                break;
            case ENTITY_PLAYER_LEVELUP:
                pk.sound = LevelSoundEventPacket.Sound.LEVEL_UP;
                break;
            case ENTITY_LEASHKNOT_PLACE:
                pk.sound = LevelSoundEventPacket.Sound.LEASHKNOT_PLACE;
                break;
            case ENTITY_LEASHKNOT_BREAK:
                pk.sound = LevelSoundEventPacket.Sound.LEASHKNOT_BREAK;
                break;
            case ENTITY_SHULKER_OPEN:
                pk.sound = LevelSoundEventPacket.Sound.SHULKER_OPEN;
                break;
            case ENTITY_SHULKER_CLOSE:
                pk.sound = LevelSoundEventPacket.Sound.SHULKER_CLOSE;
                break;
            case ITEM_BOTTLE_FILL_DRAGONBREATH:
                pk.sound = LevelSoundEventPacket.Sound.BOTTLE_DRAGON_BREATH;
                break;
            case ITEM_BUCKET_FILL_LAVA:
                pk.sound = LevelSoundEventPacket.Sound.BUCKET_FILL_LAVA;
                break;
            case ITEM_BUCKET_EMPTY_LAVA:
                pk.sound = LevelSoundEventPacket.Sound.BUCKET_EMPTY_LAVA;
                break;
            case ITEM_BUCKET_FILL:
                pk.sound = LevelSoundEventPacket.Sound.BUCKET_FILL_WATER;
                break;
            case ITEM_BUCKET_EMPTY:
                pk.sound = LevelSoundEventPacket.Sound.BUCKET_EMPTY_WATER;
                break;
            case RECORD_11:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_11;
                break;
            case RECORD_13:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_13;
                break;
            case RECORD_BLOCKS:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_BLOCKS;
                break;
            case RECORD_CAT:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_CAT;
                break;
            case RECORD_CHIRP:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_CHIRP;
                break;
            case RECORD_FAR:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_FAR;
                break;
            case RECORD_MALL:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_MALL;
                break;
            case RECORD_MELLOHI:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_MELLOHI;
                break;
            case RECORD_STAL:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_STAL;
                break;
            case RECORD_STRAD:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_STRAD;
                break;
            case RECORD_WAIT:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_WAIT;
                break;
            case RECORD_WARD:
                pk.sound = LevelSoundEventPacket.Sound.RECORD_WARD;
                break;
            case ENTITY_ZOMBIE_VILLAGER_CONVERTED:
                pk.sound = LevelSoundEventPacket.Sound.UNFECT;
                break;
            case ENTITY_ZOMBIE_VILLAGER_CURE:
                pk.sound = LevelSoundEventPacket.Sound.REMEDY;
                break;
            case ENTITY_SHEEP_SHEAR:
            	pk.sound = LevelSoundEventPacket.Sound.SHEAR;
                break;
            case ENTITY_MOOSHROOM_SHEAR:
                pk.sound = LevelSoundEventPacket.Sound.SHEAR;
                break;
            case BLOCK_GRASS_BREAK:
            case BLOCK_ANVIL_BREAK:
            case BLOCK_GLASS_BREAK:
            case BLOCK_CLOTH_BREAK:
            case BLOCK_GRAVEL_BREAK:
            case BLOCK_LADDER_BREAK:
            case BLOCK_METAL_BREAK:
            case BLOCK_SAND_BREAK:
            case BLOCK_SLIME_BREAK:
            case BLOCK_SNOW_BREAK:
            case BLOCK_STONE_BREAK:
            case BLOCK_WOOD_BREAK:
                pk.sound = LevelSoundEventPacket.Sound.BREAK_BLOCK;
                break;
            case BLOCK_GRASS_PLACE:
            case BLOCK_ANVIL_PLACE:
            case BLOCK_CLOTH_PLACE:
            case BLOCK_GLASS_PLACE:
            case BLOCK_GRAVEL_PLACE:
            case BLOCK_LADDER_PLACE:
            case BLOCK_METAL_PLACE:
            case BLOCK_SAND_PLACE:
            case BLOCK_SLIME_PLACE:
            case BLOCK_SNOW_PLACE:
            case BLOCK_STONE_PLACE:
            case BLOCK_WATERLILY_PLACE:
            case BLOCK_WOOD_PLACE:
                pk.sound = LevelSoundEventPacket.Sound.PLACE;
                break;
            case BLOCK_LAVA_POP:
                pk.sound = LevelSoundEventPacket.Sound.POP;
                break;
            case BLOCK_PORTAL_TRAVEL:
                pk.sound = LevelSoundEventPacket.Sound.PORTAL;
                break;
            case BLOCK_LEVER_CLICK:
                pk.sound = LevelSoundEventPacket.Sound.POWER_ON;
                break;
            case BLOCK_COMPARATOR_CLICK:
                pk.sound = LevelSoundEventPacket.Sound.POWER_ON;
                break;
            case BLOCK_STONE_BUTTON_CLICK_ON:
                pk.sound = LevelSoundEventPacket.Sound.POWER_ON;
                break;
            case BLOCK_METAL_PRESSUREPLATE_CLICK_ON:
                pk.sound = LevelSoundEventPacket.Sound.POWER_ON;
                break;
            case BLOCK_STONE_PRESSUREPLATE_CLICK_ON:
                pk.sound = LevelSoundEventPacket.Sound.POWER_ON;
                break;
            case BLOCK_TRIPWIRE_CLICK_ON:
                pk.sound = LevelSoundEventPacket.Sound.POWER_ON;
                break;
            case BLOCK_WOOD_BUTTON_CLICK_ON:
                pk.sound = LevelSoundEventPacket.Sound.POWER_ON;
                break;
            case BLOCK_WOOD_PRESSUREPLATE_CLICK_ON:
                pk.sound = LevelSoundEventPacket.Sound.POWER_ON;
                break;
            case BLOCK_METAL_PRESSUREPLATE_CLICK_OFF:
                pk.sound = LevelSoundEventPacket.Sound.POWER_OFF;
                break;
            case BLOCK_STONE_BUTTON_CLICK_OFF:
                pk.sound = LevelSoundEventPacket.Sound.POWER_OFF;
                break;
            case BLOCK_STONE_PRESSUREPLATE_CLICK_OFF:
                pk.sound = LevelSoundEventPacket.Sound.POWER_OFF;
                break;
            case BLOCK_TRIPWIRE_CLICK_OFF:
                pk.sound = LevelSoundEventPacket.Sound.POWER_OFF;
                break;
            case BLOCK_WOOD_BUTTON_CLICK_OFF:
                pk.sound = LevelSoundEventPacket.Sound.POWER_OFF;
                break;
            case BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF:
                pk.sound = LevelSoundEventPacket.Sound.POWER_OFF;
                break;
            case BLOCK_NOTE_BASEDRUM:
            case BLOCK_NOTE_BASS:
            case BLOCK_NOTE_BELL:
            case BLOCK_NOTE_CHIME:
            case BLOCK_NOTE_FLUTE:
            case BLOCK_NOTE_GUITAR:
            case BLOCK_NOTE_HARP:
            case BLOCK_NOTE_HAT:
            case BLOCK_NOTE_PLING:
            case BLOCK_NOTE_SNARE:
            case BLOCK_NOTE_XYLOPHONE:
                pk.sound = LevelSoundEventPacket.Sound.NOTE;
                break;
            case BLOCK_PISTON_EXTEND:
                pk.sound = LevelSoundEventPacket.Sound.PISTON_OUT;
                break;
            case BLOCK_PISTON_CONTRACT:
                pk.sound = LevelSoundEventPacket.Sound.PISTON_IN;
                break;
            case ENTITY_BOBBER_THROW:
            	pk.sound = LevelSoundEventPacket.Sound.SPLASH;
            	break;
            case ENTITY_EGG_THROW:
            	pk.sound = LevelSoundEventPacket.Sound.THROW;
            	break;
            case ENTITY_ENDERPEARL_THROW:
            case ENTITY_EXPERIENCE_BOTTLE_THROW:
            case ENTITY_LINGERINGPOTION_THROW:
            case ENTITY_SNOWBALL_THROW:
            case ENTITY_SPLASH_POTION_THROW:
            case ENTITY_WITCH_THROW:
                pk.sound = LevelSoundEventPacket.Sound.THROW;
                break;
            case ITEM_FLINTANDSTEEL_USE:
                pk.sound = LevelSoundEventPacket.Sound.IGNITE;
                break;
            case ENTITY_TNT_PRIMED:
                pk.sound = LevelSoundEventPacket.Sound.EVENT_SOUND_TNT;//Fix
                break;
            case ENTITY_GENERIC_EXPLODE:
                pk.sound = LevelSoundEventPacket.Sound.EXPLODE;
                break;
            default:
                return null;
        }

        //System.out.println("Converted sound packet " + pk.sound.name() + " (" + pk.sound.soundID + ") - " + pk.position + " - " + pk.extraData + " - " + pk.pitch);

        return new PEPacket[]{pk};
    }

}
