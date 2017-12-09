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
package org.dragonet.proxy.network.translator;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import org.dragonet.proxy.entity.meta.type.ByteArrayMeta;
import org.dragonet.proxy.entity.meta.type.ByteMeta;
import org.dragonet.proxy.entity.meta.type.LongMeta;
import org.dragonet.proxy.entity.meta.type.ShortMeta;
import org.dragonet.proxy.entity.EntityType;
import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.entity.meta.type.IntegerMeta;

public final class EntityMetaTranslator {
	// vars

	// constructor
	public EntityMetaTranslator() {

	}

	// public
	public static EntityMetaData translateToPE(EntityMetadata[] pcMeta, EntityType type) {
		/*
		 * Following format was fetched from http://wiki.vg/Entities#Entity_meta_Format
		 */
		EntityMetaData peMeta = EntityMetaData.createDefault();
		for (EntityMetadata m : pcMeta) {
			if (m == null) {
				continue;
			}
//                    System.out.println("EntityMetadata : " + m.getId() + ", value : " + m.getValue().toString() + " ( " + m.getValue().getClass() + ")");
			switch (m.getId()) {
			case 0:// Flags
				byte pcFlags = ((byte) m.getValue());
				long peFlags = peMeta.map.containsKey(EntityMetaData.Constants.DATA_FLAGS)
						? ((LongMeta) peMeta.map.get(EntityMetaData.Constants.DATA_FLAGS)).data
						: 0;
				peFlags |= (byte) ((pcFlags & 0x01) > 0 ? EntityMetaData.Constants.DATA_FLAG_ONFIRE : 0);
				peFlags |= (pcFlags & 0x02) > 0 ? EntityMetaData.Constants.DATA_FLAG_SNEAKING : 0;
				peFlags |= (pcFlags & 0x08) > 0 ? EntityMetaData.Constants.DATA_FLAG_SPRINTING : 0;
				peFlags |= (pcFlags & 0x10) > 0 ? EntityMetaData.Constants.DATA_FLAG_ACTION : 0;
				peFlags |= (pcFlags & 0x20) > 0 ? EntityMetaData.Constants.DATA_FLAG_INVISIBLE : 0;
				peMeta.map.put(EntityMetaData.Constants.DATA_FLAGS, new LongMeta(peFlags));
				break;
			case 1:// Air
				peMeta.map.put(EntityMetaData.Constants.DATA_AIR, new ShortMeta(((Integer) m.getValue()).shortValue()));
				break;
			case 2:// Name tag
				peMeta.map.put(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta((String) m.getValue()));
				break;
			case 3:// Always show name tag
				byte data;
				if (m.getType() == MetadataType.BYTE) {
					data = (byte) m.getValue();
				} else if (m.getType() == MetadataType.INT) {
					data = (byte) (((int) m.getValue()) & 0xFF);
				} else {
					data = 1;
				}
				peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_CAN_SHOW_NAMETAG, data > 0);
				break;
			case 4://Boolean : Is silent
                            peMeta.map.put(EntityMetaData.Constants.DATA_FLAG_SILENT, new ByteMeta((byte)(((boolean)m.getValue()) ? 0x00 : 0x01)));
				break;
			case 5://Boolean : No gravity
                            peMeta.map.put(EntityMetaData.Constants.DATA_FLAG_AFFECTED_BY_GRAVITY, new ByteMeta((byte)(((boolean)m.getValue()) ? 0x01 : 0x00)));// need !
				break;
			case 6:
                            switch(type)
                            {
                                //case MINECART: //VarInt : Shaking power
                                    //peMeta.map.put(EntityMetaData.Constants.DATA_HEALTH, new IntegerMeta((int) m.getValue()));
                                //case PRIMED_TNT: //VarInt : Fuse time
                                    //peMeta.map.put(EntityMetaData.Constants.DATA_FLAG_IGNITED, new IntegerMeta((int) m.getValue()));
                                //case POTION: //Slot : Potion which is thrown
                                //case FALLING_BLOCK: //Position : spawn position
                                //case AREA_EFFECT_CLOUD: //Float : Radius
                                    //peMeta.map.put(EntityMetaData.Constants.DATA_AREA_EFFECT_CLOUD_RADIUS, new FloatMeta((float) m.getValue()));
                                //case FISHING_HOOK: //VarInt : Hooked entity id + 1, or 0 if there is no hooked entity
                                case ARROW: //Byte : is critical
                                    peMeta.map.put(EntityMetaData.Constants.DATA_FLAG_CRITICAL, new ByteMeta((byte) m.getValue()));
                                //case TIPPED_ARROW: //VarInt : Color (-1 for no particles)
                                //case BOAT: //VarInt : Time since last hit
                                //case ENDER_CRISTAL: //OptPosition : Beam target
                                //case WITHER_SKULL: //Boolean Invulnerable
                                //case FIREWORKS: //Slot : Firework info
                                //case ITEM_FRAME: //Slot : Item
                                //case ITEM: //Slot : Item
                                default: // (all LIVING) Byte : Hand states, used to trigger blocking/eating/drinking animation.
                                    break;
                            }
				break;
			case 7:
                            switch(type)
                            {
                                //case MINECART: //VarInt : Shaking direction
                                //case AREA_EFFECT_CLOUD: //VarInt : Color (only for mob spell particle)
                                    //peMeta.map.put(EntityMetaData.Constants.DATA_AREA_EFFECT_CLOUD_WAITING, new IntegerMeta((int) m.getValue()));
                                //case BOAT: //VarInt : Forward direction
                                //case ENDER_CRISTAL: //Boolean : Show bottom
                                //case FIREWORKS: //VarInt : Entity ID of entity which used firework (for elytra boosting)
                                //case ITEM_FRAME: //VarInt : Rotation
                                default: // (all LIVING) Float : Health
//                                    peMeta.map.put(EntityMetaData.Constants.DATA_HEALTH, new IntegerMeta((int) m.getValue()));
                                    break;
                            }
				break;
			case 8:
                            switch(type)
                            {
                                //case MINECART: //Float : Shaking multiplier
                                //case AREA_EFFECT_CLOUD: //Boolean : Ignore radius and show effect as single point, not area
                                //case BOAT: //Float : Damage taken
                                default: // (all LIVING) VarInt : Potion effect color (or 0 if there is no effect)
                                    peMeta.map.put(EntityMetaData.Constants.DATA_POTION_COLOR, new ByteMeta((byte) ((int) m.getValue() & 0xFF)));
                                    break;
                            }
				break;
			case 9:
                            switch(type)
                            {
                                //case MINECART: //VarInt : Custom block ID and damage
                                //case AREA_EFFECT_CLOUD: //VarInt : Particle ID
                                    //peMeta.map.put(EntityMetaData.Constants.DATA_AREA_EFFECT_CLOUD_PARTICLE_ID, new IntegerMeta((int) m.getValue()));
                                //case BOAT: //VarInt : Type (0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=dark oak)
                                default: //(all LIVING) Boolean : Is potion effect ambient: reduces the number of particles generated by potions to 1/5 the normal amount
                                    peMeta.map.put(EntityMetaData.Constants.DATA_POTION_AMBIENT, new ByteMeta((byte)(((boolean)m.getValue()) ? 0x00 : 0x01)));
                                    break;
                            }
				break;
			case 10:
                            switch(type)
                            {
                                //case MINECART: //VarInt : Custom block Y position (in 16ths of a block)
                                //case AREA_EFFECT_CLOUD: //VarInt : Particle parameter 1
                                //case BOAT: //Boolean : Right paddle turning
                                default: //(all LIVING) VarInt : Number of arrows in entity
                                    // Not supported yet
                                    break;
                            }
				break;
			case 11:
                            switch(type)
                            {
                                //case MINECART: //Boolean : Show custom block
                                //case AREA_EFFECT_CLOUD: //VarInt : Particle parameter 2
                                //case BOAT: //Boolean : Left paddle turning
                                //case ARMOR_STAND: //Byte : see http://wiki.vg/Entity_metadata#ArmorStand
                                //case INSENTIENT: //Byte : (0x01 = NoAI, 0x02 = Left handed)
                                case PLAYER: //Float : Additional Hearts
                                    // Not supported yet
                                    break;
                            }
				break;
			case 12:
                            switch(type)
                            {
                                //case MINECART_FURNACE: //Boolean : Is powered
                                //case MINECART_COMMAND_BLOCK: //String : Command
                                //case ARMOR_STAND: //Rotation : Head rotation
                                //case BAT: //Byte : Is hanging
                                //case AGEABLE: //Boolean : Is baby
                                //case IRONGOLEM: //Byte : Is player-created
                                //case SNOWMAN: //Byte : has no pumpkin hat
                                //case SHULKER: //Direction : Facing direction
                                //case BLAZE: //Byte : 0x01 = Is on fire
                                //case CREEPER: //VarInt	State (-1 = idle, 1 = fuse)
                                //case EVOCATOR: //Byte	Spell (0: none, 1: summon vex, 2: attack, 3: wololo)
                                //case VEX: //Byte : 0x01 = Is in attack mode
                                //case VINDICATOR: //Byte : 0x01 = Has target (aggressive state)
                                //case SKELETON: //Boolean : Is swinging arms
                                //case SPIDER: //Byte : 0x01 = Is climbing
                                //case WITCH: //Boolean : Is drinking potion
                                //case WITHER: //VarInt : Center head's target (entity ID, or 0 if no target)
                                //case ZOMBIE: //Boolean : Is baby
                                //case ENDERMAN: //Opt BlockID : Carried block
                                //case ENDER_DRAGON: //VarInt : Dragon phase
                                //case GHAST: //Boolean : Is attacking
                                //case SLIME : //VarInt : Size
                                case PLAYER: //VarInt : Score
                                    // Not supported yet
                                    break;
                            }
				break;
			case 13:
                            switch(type)
                            {
                                //case MINECART_COMMAND_BLOCK: //Chat : Last output
                                //case ARMOR_STAND: //Rotation : Body rotation
                                //case HORSE: //Byte : see http://wiki.vg/Entity_metadata#AbstractHorse
                                //case PIG: //Boolean : Has saddle
                                //case RABBIT: //VarInt : Type
                                //case POLAR_BEAR: //Boolean : Standing up
                                //case SHEEP: //Byte : see http://wiki.vg/Entity_metadata#Sheep
                                //case TAMEABLE_ANIMAL: //Byte : see http://wiki.vg/Entity_metadata#TameableAnimal
                                //case VILLAGER: //VarInt : Profession (Farmer = 0, Librarian = 1, Priest = 2, Blacksmith = 3, Butcher = 4, Nitwit = 5)
                                //case SHULKER: //OptPosition : Attachment position
                                //case CREEPER: //Boolean : Is charged
                                //case WITHER: //VarInt : Left(?) head's target (entity ID, or 0 if no target)
                                //case ZOMBIE: //VarInt : Unused (previously type)
                                //case ENDERMAN: //Boolean : Is screaming
                                case PLAYER: //Byte : The Displayed Skin Parts bit mask that is sent in Client Settings
                                    // Not supported yet
                                    break;
                            }
				break;
			case 14:
                            switch(type)
                            {
                                //case ARMOR_STAND: //Rotation : Left arm rotation
                                //case HORSE: //OptUUID : Owner
                                //case PIG: //VarInt : Total time to "boost" with a carrot on a stick for
                                //case TAMEABLE_ANIMAL: //OptUUID : Owner
                                //case SHULKER: //Byte : Shield height
                                //case CREEPER: //Boolean : Is ignited
                                //case WITHER: //VarInt : Right(?) head's target (entity ID, or 0 if no target)
                                //case ZOMBIE: //Boolean : Are hands held up
                                case PLAYER: //Byte : Main hand (0 : Left, 1 : Right)
                                    // Not supported yet
                                    break;
                            }
				break;
			case 15:
                            switch(type)
                            {
                                //case ARMOR_STAND: //Rotation : Right arm rotation
                                //case HORSE: //VarInt : Variant (Color & Style)
                                //case CHESTED_HORSE: //Boolean : Has Chest
                                //case OCELOT: //VarInt	Type (0 = untamed, 1 = tuxedo, 2 = tabby, 3 = siamese). Used to render regardless as to whether it is tamed or not.
                                //case WOLF: //Float : Damage taken (used for tail rotation)
                                //case PARROT: //VarInt	Variant (0: red/blue, 1: blue, 2: green, 3: yellow/blue, 4: silver)
                                //case SHULKER: //Byte : Color (dye color)
                                //case WITHER: //VarInt : Invulnerable time
                                //case ZOMBIE_VILLAGER: //Boolean : Is converting
                                case PLAYER: //NBT Tag : Left shoulder entity data (for occupying parrot)
                                    // Not supported yet
                                    break;
                            }
				break;
			case 16:
                            switch(type)
                            {
                                //case ARMOR_STAND: //Rotation : Left leg rotation
                                //case HORSE: //VarInt : Armor (0: none, 1: iron, 2: gold, 3: diamond)
                                //case WOLF: //Boolean : Is begging
                                //case ZOMBIE_VILLAGER: //VarInt : Profession
                                case PLAYER: //NBT Tag : Right shoulder entity data (for occupying parrot)
                                    // Not supported yet
                                    break;
                            }
			case 17:
                            switch(type)
                            {
                                //case ARMOR_STAND: //Rotation : Right leg rotation
                                //case WOLF: //VarInt : Collar color (values are those used with dyes)
                                    //break;
                            }
				break;
			}
		}
		return peMeta;
	}

	// private

}
