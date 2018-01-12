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
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import org.dragonet.common.mcbedrock.data.entity.meta.type.*;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.common.mcbedrock.data.entity.EntityType;
import org.dragonet.common.mcbedrock.data.entity.meta.EntityMetaData;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.common.mcbedrock.utilities.BlockPosition;

public final class EntityMetaTranslator {

    public static EntityMetaData translateToPE(UpstreamSession session, EntityMetadata[] pcMeta, EntityType type) {
        /*
     * Following format was fetched from http://wiki.vg/Entities#Entity_meta_Format
         */
        EntityMetaData peMeta = EntityMetaData.createDefault();
        if (pcMeta == null || type == EntityType.NONE) {
            return peMeta;
        }
//        System.out.println("Entity + " + type);
        for (EntityMetadata m : pcMeta) {
//            System.out.println(m);
            boolean handle = true;
            try {
                if (m == null)
                    continue;

                switch (m.getId()) {
                    case 0:// Flags
                        byte pcFlags = ((byte) m.getValue());
                        //System.out.println("PC flag: " + pcFlags);
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_ONFIRE, (pcFlags & 0x01) > 0);
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_SNEAKING, (pcFlags & 0x02) > 0);
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_RIDING, (pcFlags & 0x04) > 0);
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_SPRINTING, (pcFlags & 0x08) > 0);
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_ACTION, (pcFlags & 0x10) > 0);
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_INVISIBLE, (pcFlags & 0x20) > 0);
                        //peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_GLOWING, (pcFlags & 0x40) > 0); //Not implemented (possible with potions)
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_GLIDING, (pcFlags & 0x80) > 0);
                        break;
                    case 1:// Air
                        peMeta.set(EntityMetaData.Constants.DATA_AIR, new ShortMeta(((Integer) m.getValue()).shortValue()));
                        break;
                    case 2:// Name tag
                        if (!m.getValue().equals("")) {
                            peMeta.set(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta((String) m.getValue()));
                        }
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
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_SILENT, ((boolean) m.getValue()));
                        break;
                    case 5://Boolean : No gravity
                        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_AFFECTED_BY_GRAVITY, (!((boolean) m.getValue())));
                        break;
                    case 6:
                        switch (type) {
                            case MINECART: //VarInt : Shaking power
                                peMeta.set(EntityMetaData.Constants.DATA_HURT_TIME, new IntegerMeta((int) m.getValue()));
                                break;
                            case PRIMED_TNT: //VarInt : Fuse time
                                peMeta.set(EntityMetaData.Constants.DATA_FUSE_LENGTH, new IntegerMeta((int) m.getValue()));
                                break;
                            case POTION: //Slot : Potion which is thrown
                                peMeta.set(EntityMetaData.Constants.DATA_TYPE_SLOT, new SlotMeta(ItemBlockTranslator.translateSlotToPE((ItemStack) m.getValue())));
                                break;
                            case FALLING_BLOCK: //Position : spawn position
                                peMeta.set(EntityMetaData.Constants.DATA_BLOCK_TARGET, new BlockPositionMeta(new BlockPosition((Position) m.getValue())));
                                break;
                            case AREA_EFFECT_CLOUD: //Float : Radius
                                peMeta.set(EntityMetaData.Constants.DATA_AREA_EFFECT_CLOUD_RADIUS, new FloatMeta((float) m.getValue()));
                                break;
                            case FISHING_HOOK: //VarInt : Hooked entity id + 1, or 0 if there is no hooked entity
                                break;
                            case ARROW: //Byte : is critical
                                peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_CRITICAL, ((byte) m.getValue() & 0x01) > 0);
                                break;
                            //case TIPPED_ARROW: //VarInt : Color (-1 for no particles)
                            case BOAT: //VarInt : Time since last hit
                                peMeta.set(EntityMetaData.Constants.DATA_HURT_TIME, new IntegerMeta((int) m.getValue()));
                                break;
                            case ENDER_CRYSTAL: //OptPosition : Beam target
                                if (m.getValue() != null) {
                                    peMeta.set(EntityMetaData.Constants.DATA_BLOCK_TARGET, new BlockPositionMeta(new BlockPosition((Position) m.getValue())));
                                }
                                break;
                            case WITHER_SKULL: //Boolean Invulnerable
                                peMeta.set(EntityMetaData.Constants.DATA_WITHER_INVULNERABLE_TICKS, new IntegerMeta((int) m.getValue()));
                                break;
                            case FIREWORKS_ROCKET: //Slot : Firework info
                                peMeta.set(EntityMetaData.Constants.DATA_TYPE_SLOT, new SlotMeta(ItemBlockTranslator.translateSlotToPE((ItemStack) m.getValue())));
                                break;
                            //case ITEM_FRAME: //Slot : Item
                            case ITEM: //Slot : Item
                                peMeta.set(EntityMetaData.Constants.DATA_TYPE_SLOT, new SlotMeta(ItemBlockTranslator.translateSlotToPE((ItemStack) m.getValue())));
                                break;
                            default: // (all LIVING) Byte : Hand states, used to trigger blocking/eating/drinking animation.
                                if (m.getValue() instanceof Byte)
                                {
                                    peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_ACTION, ((byte) m.getValue() & 0x01) > 0);
                                }
                                else
                                    handle = false;
//                                System.out.println("case 6 " + type.name() + " " + m.getValue());
                                break;
                        }
                        break;
                    case 7:
                        switch (type) {
                            case BOAT: //VarInt : Forward direction
                            case MINECART: //VarInt : Shaking direction
                                peMeta.set(EntityMetaData.Constants.DATA_HURT_DIRECTION, new IntegerMeta((int) m.getValue()));
                                break;
                            case AREA_EFFECT_CLOUD: //VarInt : Color (only for mob spell particle)
                                peMeta.set(EntityMetaData.Constants.DATA_AREA_EFFECT_CLOUD_WAITING, new IntegerMeta((int) m.getValue()));
                                break;
                            case ENDER_CRYSTAL: //Boolean : Show bottom
                                break;
                            case FIREWORKS_ROCKET: //VarInt : Entity ID of entity which used firework (for elytra boosting)
                                peMeta.set(EntityMetaData.Constants.DATA_OWNER_EID, new IntegerMeta((int) m.getValue()));
                                break;
                            case ARROW:
                                break;
//                        case ITEM_FRAME: //VarInt : Rotation
//                            peMeta.set(EntityMetaData.Constants.DATA_, new IntegerMeta((int) m.getValue()));
//                            break;
                            default: // (all LIVING) Float : Health
                                if (m.getValue() instanceof Float)
                                {
                                    peMeta.set(EntityMetaData.Constants.DATA_HEALTH, new IntegerMeta((int) ((float) m.getValue())));
                                }
                                else
                                    handle = false;
//                                System.out.println("case 7 " + type.name() + " " + m.getValue());
                                break;
                        }
                        break;
                    case 8:
                        switch (type) {
                            case AREA_EFFECT_CLOUD: //Boolean : Ignore radius and show effect as single point, not area
                                break;
                            case MINECART: //Float : Shaking multiplier
                            case BOAT: //Float : Damage taken
                                break;
                            default: // (all LIVING) VarInt : Potion effect color (or 0 if there is no effect)
                                peMeta.set(EntityMetaData.Constants.DATA_POTION_COLOR, new ByteMeta((byte) ((int) m.getValue() & 0xFF)));
//                                System.out.println("case 8 " + type.name() + " " + m.getValue());
                                break;
                        }
                        break;
                    case 9:
                        switch (type) {
                            case MINECART: //VarInt : Custom block ID and damage
                                peMeta.set(EntityMetaData.Constants.DATA_MINECART_DISPLAY_BLOCK, new IntegerMeta((int) m.getValue()));
                                break;
                            case AREA_EFFECT_CLOUD: //VarInt : Particle ID
                                peMeta.set(EntityMetaData.Constants.DATA_AREA_EFFECT_CLOUD_PARTICLE_ID, new IntegerMeta((int) m.getValue()));
                                break;
                            case BOAT: //VarInt : Type (0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=dark oak)
                                peMeta.set(20 /* woodID */, new ByteMeta((byte) ((Integer) m.getValue()).byteValue()));
                                break;
                            default: //(all LIVING) Boolean : Is potion effect ambient: reduces the number of particles generated by potions to 1/5 the normal amount
                                peMeta.set(EntityMetaData.Constants.DATA_POTION_AMBIENT, new ByteMeta((byte) (((boolean) m.getValue()) ? 0x01 : 0x00)));
//                                System.out.println("case 9 " + type.name() + " " + m.getValue());
                                break;
                        }
                        break;
                    case 10:
                        switch (type) {
                            case MINECART: //VarInt : Custom block Y position (in 16ths of a block)
                                peMeta.set(EntityMetaData.Constants.DATA_MINECART_DISPLAY_OFFSET, new IntegerMeta((int) m.getValue()));
                                break;
                            case AREA_EFFECT_CLOUD: //VarInt : Particle parameter 1
                                break;
                            case BOAT: //Boolean : Right paddle turning
                                peMeta.set(EntityMetaData.Constants.DATA_PADDLE_TIME_RIGHT, new IntegerMeta(20));
                                break;
                            default: //(all LIVING) VarInt : Number of arrows in entity
//                                System.out.println("case 10 " + type.name() + " " + m.getValue());
                                // Not supported yet
                                break;
                        }
                        break;
                    case 11:
                        switch (type) {
                            case MINECART: //Boolean : Show custom block
                                peMeta.set(EntityMetaData.Constants.DATA_MINECART_HAS_DISPLAY, new ByteMeta((byte) (((boolean) m.getValue()) ? 0x01 : 0x00)));
                                break;
                            case AREA_EFFECT_CLOUD: //VarInt : Particle parameter 2
                                break;
                            case BOAT: //Boolean : Left paddle turning
                                peMeta.set(EntityMetaData.Constants.DATA_PADDLE_TIME_LEFT, new IntegerMeta(20));
                                break;
                            case ARMOR_STAND: //Byte : see http://wiki.vg/Entity_metadata#ArmorStand
                                break;
                            case PLAYER: //Float : Additional Hearts
                                // Not supported yet
                                break;
                            default: //case INSENTIENT: //Byte : (0x01 = NoAI, 0x02 = Left handed)
//                                System.out.println("case 11 " + type.name() + " " + m.getValue());
                                break;
                        }
                        break;
                    case 12:
                        switch (type) {
                            case MINECART:
                                if (m.getValue() instanceof Boolean) { //Boolean : Is powered
                                    peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_POWERED, ((boolean) m.getValue()));
                                }
                                if (m.getValue() instanceof String) { //String : Command
                                    //
                                }
                                break;
                            case ARMOR_STAND: //Rotation : Head rotation
                                break;
                            case BAT: //Byte : Is hanging
//                                peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_SITTING, (((byte) m.getValue()) ? 0x01 : 0x00)); //wrong data
                                break;
                            case IRON_GOLEM: //Byte : Is player-created
                            case SNOWMAN: //Byte : has no pumpkin hat
                            case SHULKER: //Direction : Facing direction
                                break;
                            case BLAZE: //Byte : 0x01 = Is on fire
                                peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_ONFIRE, ((byte) m.getValue()) == 0x01);
                                break;
                            case CREEPER: //VarInt	State (-1 = idle, 1 = fuse)
                            case EVOCATION_ILLAGER: //Byte	Spell (0: none, 1: summon vex, 2: attack, 3: wololo)
                            case VEX: //Byte : 0x01 = Is in attack mode
                            case VINDICATOR: //Byte : 0x01 = Has target (aggressive state)
                            case SKELETON: //Boolean : Is swinging arms
                                break;
                            case SPIDER: //Byte : 0x01 = Is climbing
                                peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_WALLCLIMBING, ((byte) m.getValue()) == 0x01);
                                break;
                            case WITCH: //Boolean : Is drinking potion
                            case WITHER: //VarInt : Center head's target (entity ID, or 0 if no target)
                            case ENDERMAN: //Opt BlockID : Carried block
                            case ENDER_DRAGON: //VarInt : Dragon phase
                            case GHAST: //Boolean : Is attacking
                                break;
                            case SLIME: //VarInt : Size
                            case MAGMA_CUBE: //VarInt : Size
                                peMeta.set(EntityMetaData.Constants.DATA_SCALE, new FloatMeta((int) m.getValue()));
                                break;
                            case PLAYER: //VarInt : Score
                                // Not supported yet
                                break;
                            default: //ZOMBIE, AGEABLE //Boolean Is baby
                            {
                                if (m.getValue() instanceof Boolean)
                                {
                                    peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_BABY, ((boolean) m.getValue()));
                                    if ((boolean) m.getValue()) {
                                        peMeta.set(EntityMetaData.Constants.DATA_SCALE, new FloatMeta(0.5f));
                                    }
                                }
                                else
                                    handle = false;
                                break;
                            }
                        }
                        break;
                    case 13:
                        switch (type) {
                            case MINECART: //Chat : Last output
                                if (m.getValue() instanceof String) {
                                    //
                                }
                            case ARMOR_STAND: //Rotation : Body rotation
                                break;
                            case HORSE: //Byte : see http://wiki.vg/Entity_metadata#AbstractHorse
                                break;
                            case SHEEP: //Byte 0x0F Color (matches dye damage values) //0x10 Is sheared
                                if (((byte) (m.getValue()) & 0x10) == 0) {
                                    peMeta.set(EntityMetaData.Constants.DATA_COLOR, new ByteMeta((byte) (m.getValue())));
                                } else {
                                    peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_SHEARED, true);
                                }
                                break;
                            case RABBIT: //VarInt : Type
                                peMeta.set(EntityMetaData.Constants.DATA_VARIANT, new IntegerMeta((int) m.getValue()));
                                break;
                            case PIG: //Boolean : Has saddle
                                peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_SADDLED, ((boolean) m.getValue()));
                                break;
                            case POLAR_BEAR: //Boolean : Standing up
                            case VILLAGER: //VarInt : Profession (Farmer = 0, Librarian = 1, Priest = 2, Blacksmith = 3, Butcher = 4, Nitwit = 5)
                            case SHULKER: //OptPosition : Attachment position
                                break;
                            case CREEPER: //Boolean : Is charged
                                peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_CHARGED, ((boolean) m.getValue()));
                                break;
                            case WITHER: //VarInt : Left(?) head's target (entity ID, or 0 if no target)
                            case ZOMBIE: //VarInt : Unused (previously type)
                            case ENDERMAN: //Boolean : Is screaming
                                break;
                            case PLAYER: //Byte : The Displayed Skin Parts bit mask that is sent in Client Settings
                                // Not supported yet
                                break;
                            default: //case TAMEABLE_ANIMAL: //Byte : see http://wiki.vg/Entity_metadata#TameableAnimal
//                                System.out.println("case 13 " + type.name() + " " + m.getValue());
                                break;
                        }
                        break;
                    case 14:
                        switch (type) {
                            case ARMOR_STAND: //Rotation : Left arm rotation
                            case HORSE: //OptUUID : Owner
                            case PIG: //VarInt : Total time to "boost" with a carrot on a stick for
                            case SHULKER: //Byte : Shield height
                                break;
                            case CREEPER: //Boolean : Is ignited
                                peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_IGNITED, ((boolean) m.getValue()));
                                break;
                            case WITHER: //VarInt : Right(?) head's target (entity ID, or 0 if no target)
                            case ZOMBIE: //Boolean : Are hands held up
                                break;
                            case PLAYER: //Byte : Main hand (0 : Left, 1 : Right)
                                // Not supported yet
                                break;
                            default: //case TAMEABLE_ANIMAL: //OptUUID : Owner
//                                System.out.println("case 14 " + type.name() + " " + m.getValue());
                                break;
                        }
                        break;
                    case 15:
                        switch (type) {
                            case ARMOR_STAND: //Rotation : Right arm rotation
                                break;
                            case HORSE:
                                if (m.getValue() instanceof Integer) { //VarInt : Variant (Color & Style)
                                    peMeta.set(EntityMetaData.Constants.DATA_VARIANT, new IntegerMeta((int) m.getValue()));
                                }
                                if (m.getValue() instanceof Boolean) { //Boolean : Has Chest
                                    //
                                }
                                break;
                            case OCELOT: //VarInt	Type (0 = untamed, 1 = tuxedo, 2 = tabby, 3 = siamese). Used to render regardless as to whether it is tamed or not.
                                peMeta.set(EntityMetaData.Constants.DATA_VARIANT, new IntegerMeta((int) m.getValue()));
                                break;
                            case WOLF: //Float : Damage taken (used for tail rotation)
                                break;
                            case PARROT: //VarInt	Variant (0: red/blue, 1: blue, 2: green, 3: yellow/blue, 4: silver)
                                peMeta.set(EntityMetaData.Constants.DATA_VARIANT, new IntegerMeta((int) m.getValue()));
                                break;
                            case SHULKER: //Byte : Color (dye color)
                                peMeta.set(EntityMetaData.Constants.DATA_VARIANT, new ByteMeta((byte) m.getValue()));
                                break;
                            case WITHER: //VarInt : Invulnerable time
//                            case ZOMBIE_VILLAGER: //Boolean : Is converting
                                break;
                            case PLAYER: //NBT Tag : Left shoulder entity data (for occupying parrot)
                                // Not supported yet
                                break;
                        }
                        break;
                    case 16:
                        switch (type) {
                            case ARMOR_STAND: //Rotation : Left leg rotation
                            case HORSE: //VarInt : Armor (0: none, 1: iron, 2: gold, 3: diamond)
                            case WOLF: //Boolean : Is begging
//                            case ZOMBIE_VILLAGER: //VarInt : Profession
                            case PLAYER: //NBT Tag : Right shoulder entity data (for occupying parrot)
                                // Not supported yet
                                break;
                        }
                    case 17:
                        switch (type) {
                            case ARMOR_STAND: //Rotation : Right leg rotation
                            case WOLF: //VarInt : Collar color (values are those used with dyes)
                                break;
                        }
                        break;
                }
                if (type == EntityType.GIANT_ZOMBIE)
                {
                    peMeta.set(EntityMetaData.Constants.DATA_SCALE, new FloatMeta(6));
                }
                if (!handle)
                    DragonProxy.getInstance().getLogger().debug("Not supported entity meta (" + type.name() + ") : " + m.toString());
            } catch (Exception p_Ex) {
                p_Ex.printStackTrace();
            }
        }
        return peMeta;
    }

    public static EntityType translateToPE(ObjectType pcType) {
        switch (pcType) {
            case SNOWBALL:
                return EntityType.SNOW_BALL;
            case GHAST_FIREBALL:
                return EntityType.LARGE_FIREBALL;
            case BLAZE_FIREBALL:
                return EntityType.LARGE_FIREBALL;
            case WITHER_HEAD_PROJECTILE:
                return EntityType.LARGE_FIREBALL;
            case ITEM_FRAME:
                return EntityType.NONE; //TODO
            case EYE_OF_ENDER:
                return EntityType.ENDER_EYE;
            case EXP_BOTTLE:
                return EntityType.EXP_ORB;
            case FIREWORK_ROCKET:
                return EntityType.FIREWORKS_ROCKET;
            case EVOCATION_FANGS:
                return EntityType.EVOCATION_FANG;
            case FISH_HOOK:
                return EntityType.FISHING_HOOK;
            case SPECTRAL_ARROW:
                return EntityType.ARROW;
            case TIPPED_ARROW:
                return EntityType.ARROW;
            case DRAGON_FIREBALL:
                return EntityType.LARGE_FIREBALL;
            default:
                return EntityType.valueOf(pcType.name());
        }
    }
}
