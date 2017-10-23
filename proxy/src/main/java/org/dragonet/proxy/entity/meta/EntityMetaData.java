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
package org.dragonet.proxy.entity.meta;

import java.util.HashMap;
import java.util.Map;
import org.dragonet.proxy.entity.meta.type.ByteArrayMeta;
import org.dragonet.proxy.entity.meta.type.ByteMeta;
import org.dragonet.proxy.entity.meta.type.LongMeta;
import org.dragonet.proxy.entity.meta.type.ShortMeta;
import org.dragonet.proxy.utilities.BinaryStream;

public class EntityMetaData extends BinaryStream {

    public static class Constants {

        public final static int DATA_TYPE_BYTE = 0;
        public final static int DATA_TYPE_SHORT = 1;
        public final static int DATA_TYPE_INT = 2;
        public final static int DATA_TYPE_FLOAT = 3;
        public final static int DATA_TYPE_STRING = 4;
        public final static int DATA_TYPE_SLOT = 5;
        public final static int DATA_TYPE_POS = 6;
        public final static int DATA_TYPE_LONG = 7;
        public final static int DATA_TYPE_VECTOR3F = 8;


        public final static int DATA_FLAGS = 0;
        public final static int DATA_HEALTH = 1; //int (minecart/boat)
        public final static int DATA_VARIANT = 2; //int
        public final static int DATA_COLOR = 3, DATA_COLOUR = 3; //byte
        public final static int DATA_NAMETAG = 4; //string
        public final static int DATA_OWNER_EID = 5; //long
        public final static int DATA_TARGET_EID = 6; //long
        public final static int DATA_AIR = 7; //short
        public final static int DATA_POTION_COLOR = 8; //int (ARGB!)
        public final static int DATA_POTION_AMBIENT = 9; //byte
        /* 10 (byte) */
        public final static int DATA_HURT_TIME = 11; //int (minecart/boat)
        public final static int DATA_HURT_DIRECTION = 12; //int (minecart/boat)
        public final static int DATA_PADDLE_TIME_LEFT = 13; //float
        public final static int DATA_PADDLE_TIME_RIGHT = 14; //float
        public final static int DATA_EXPERIENCE_VALUE = 15; //int (xp orb)
        public final static int DATA_MINECART_DISPLAY_BLOCK = 16; //int (id | (data << 16))
        public final static int DATA_MINECART_DISPLAY_OFFSET = 17; //int
        public final static int DATA_MINECART_HAS_DISPLAY = 18; //byte (must be 1 for minecart to show block inside)

        //TODO: add more properties

        public final static int DATA_ENDERMAN_HELD_ITEM_ID = 23; //short
        public final static int DATA_ENDERMAN_HELD_ITEM_DAMAGE = 24; //short
        public final static int DATA_ENTITY_AGE = 25; //short

        /* 27 (byte) player-specific flags
         * 28 (int) player "index"?
         * 29 (block coords) bed position */
        public final static int DATA_FIREBALL_POWER_X = 30; //float
        public final static int DATA_FIREBALL_POWER_Y = 31;
        public final static int DATA_FIREBALL_POWER_Z = 32;
        /* 33 (unknown)
         * 34 (float) fishing bobber
         * 35 (float) fishing bobber
         * 36 (float) fishing bobber */
        public final static int DATA_POTION_AUX_VALUE = 37; //short
        public final static int DATA_LEAD_HOLDER_EID = 38; //long
        public final static int DATA_SCALE = 39; //float
        public final static int DATA_INTERACTIVE_TAG = 40; //string (button text)
        public final static int DATA_NPC_SKIN_ID = 41; //string
        public final static int DATA_URL_TAG = 42; //string
        public final static int DATA_MAX_AIR = 43; //short
        public final static int DATA_MARK_VARIANT = 44; //int
        /* 45 (byte) container stuff
         * 46 (int) container stuff
         * 47 (int) container stuff */
        public final static int DATA_BLOCK_TARGET = 48; //block coords (ender crystal)
        public final static int DATA_WITHER_INVULNERABLE_TICKS = 49; //int
        public final static int DATA_WITHER_TARGET_1 = 50; //long
        public final static int DATA_WITHER_TARGET_2 = 51; //long
        public final static int DATA_WITHER_TARGET_3 = 52; //long
        /* 53 (short) */
        public final static int DATA_BOUNDING_BOX_WIDTH = 54; //float
        public final static int DATA_BOUNDING_BOX_HEIGHT = 55; //float
        public final static int DATA_FUSE_LENGTH = 56; //int
        public final static int DATA_RIDER_SEAT_POSITION = 57; //vector3f
        public final static int DATA_RIDER_ROTATION_LOCKED = 58; //byte
        public final static int DATA_RIDER_MAX_ROTATION = 59; //float
        public final static int DATA_RIDER_MIN_ROTATION = 60; //float
        public final static int DATA_AREA_EFFECT_CLOUD_RADIUS = 61; //float
        public final static int DATA_AREA_EFFECT_CLOUD_WAITING = 62; //int
        public final static int DATA_AREA_EFFECT_CLOUD_PARTICLE_ID = 63; //int
        /* 64 (int) shulker-related */
        public final static int DATA_SHULKER_ATTACH_FACE = 65; //byte
        /* 66 (short) shulker-related */
        public final static int DATA_SHULKER_ATTACH_POS = 67; //block coords
        public final static int DATA_TRADING_PLAYER_EID = 68; //long

        /* 70 (byte) command-block */
        public final static int DATA_COMMAND_BLOCK_COMMAND = 71; //string
        public final static int DATA_COMMAND_BLOCK_LAST_OUTPUT = 72; //string
        public final static int DATA_COMMAND_BLOCK_TRACK_OUTPUT = 73; //byte
        public final static int DATA_CONTROLLING_RIDER_SEAT_NUMBER = 74; //byte
        public final static int DATA_STRENGTH = 75; //int
        public final static int DATA_MAX_STRENGTH = 76; //int
	/* 77 (int)
	 * 78 (int) */


        public final static int DATA_FLAG_ONFIRE = 0;
        public final static int DATA_FLAG_SNEAKING = 1;
        public final static int DATA_FLAG_RIDING = 2;
        public final static int DATA_FLAG_SPRINTING = 3;
        public final static int DATA_FLAG_ACTION = 4;
        public final static int DATA_FLAG_INVISIBLE = 5;
        public final static int DATA_FLAG_TEMPTED = 6;
        public final static int DATA_FLAG_INLOVE = 7;
        public final static int DATA_FLAG_SADDLED = 8;
        public final static int DATA_FLAG_POWERED = 9;
        public final static int DATA_FLAG_IGNITED = 10;
        public final static int DATA_FLAG_BABY = 11;
        public final static int DATA_FLAG_CONVERTING = 12;
        public final static int DATA_FLAG_CRITICAL = 13;
        public final static int DATA_FLAG_CAN_SHOW_NAMETAG = 14;
        public final static int DATA_FLAG_ALWAYS_SHOW_NAMETAG = 15;
        public final static int DATA_FLAG_IMMOBILE = 16, DATA_FLAG_NO_AI = 16;
        public final static int DATA_FLAG_SILENT = 17;
        public final static int DATA_FLAG_WALLCLIMBING = 18;
        public final static int DATA_FLAG_CAN_CLIMB = 19;
        public final static int DATA_FLAG_SWIMMER = 20;
        public final static int DATA_FLAG_CAN_FLY = 21;
        public final static int DATA_FLAG_RESTING = 22;
        public final static int DATA_FLAG_SITTING = 23;
        public final static int DATA_FLAG_ANGRY = 24;
        public final static int DATA_FLAG_INTERESTED = 25;
        public final static int DATA_FLAG_CHARGED = 26;
        public final static int DATA_FLAG_TAMED = 27;
        public final static int DATA_FLAG_LEASHED = 28;
        public final static int DATA_FLAG_SHEARED = 29;
        public final static int DATA_FLAG_GLIDING = 30;
        public final static int DATA_FLAG_ELDER = 31;
        public final static int DATA_FLAG_MOVING = 32;
        public final static int DATA_FLAG_BREATHING = 33;
        public final static int DATA_FLAG_CHESTED = 34;
        public final static int DATA_FLAG_STACKABLE = 35;
        public final static int DATA_FLAG_SHOWBASE = 36;
        public final static int DATA_FLAG_REARING = 37;
        public final static int DATA_FLAG_VIBRATING = 38;
        public final static int DATA_FLAG_IDLING = 39;
        public final static int DATA_FLAG_EVOKER_SPELL = 40;
        public final static int DATA_FLAG_CHARGE_ATTACK = 41;
        public final static int DATA_FLAG_WASD_CONTROLLED = 42;
        public final static int DATA_FLAG_CAN_POWER_JUMP = 43;
        public final static int DATA_FLAG_LINGER = 44;
        public final static int DATA_FLAG_HAS_COLLISION = 45;
        public final static int DATA_FLAG_AFFECTED_BY_GRAVITY = 46;
        public final static int DATA_FLAG_FIRE_IMMUNE = 47;
        public final static int DATA_FLAG_DANCING = 48;
    }

    public HashMap<Integer, EntityMetaDataObject> map;

    public EntityMetaData() {
        this.map = new HashMap<>();
    }

    public void set(int key, EntityMetaDataObject object) {
        this.map.put(key, object);
    }

    public void encode() {
        reset();
        putUnsignedVarInt(map.size());
        for (Map.Entry<Integer, EntityMetaDataObject> entry : this.map.entrySet()) {
            putUnsignedVarInt(entry.getKey());
            putUnsignedVarInt(entry.getValue().type());
            entry.getValue().encode(this);
        }
    }

    public void setGenericFlag(int flagId, boolean value) {
        long flag = 0;
        if(!map.containsKey(Constants.DATA_FLAGS)) {
            map.put(Constants.DATA_FLAGS, new LongMeta(0L));
        } else {
            flag = ((LongMeta)map.get(Constants.DATA_FLAGS)).data;
        }
        boolean currValue = ((flag >> flagId) & 0b1) > 0;
        if(currValue != value) {
            flag ^= (1 << flagId);
        }
        ((LongMeta)map.get(Constants.DATA_FLAGS)).data = flag;
    }

    public static EntityMetaData from(BinaryStream source) {
        // TODO
        return createDefault();
    }

    public static EntityMetaData createDefault() {
        EntityMetaData data = new EntityMetaData();
        data.set(Constants.DATA_FLAGS, new LongMeta(1 << Constants.DATA_FLAG_AFFECTED_BY_GRAVITY));
        data.set(Constants.DATA_AIR, new ShortMeta((short) 300));
        data.set(Constants.DATA_NAMETAG, new ByteArrayMeta(""));
        return data;
    }

    /*
    public static EntityMetaData getMetaDataFromPlayer(GlowPlayer player) {
        byte flags = (byte) 0x00;
        if (player.getFireTicks() > 0) {
            flags |= EntityMetaData.Constants.DATA_FLAG_ONFIRE;
        }
        if(player.isSprinting()){
            flags |= EntityMetaData.Constants.DATA_FLAG_SPRINTING;
        }
        if(player.isSneaking()){
            flags |= EntityMetaData.Constants.DATA_FLAG_SNEAKING;
        }
        EntityMetaData data = createDefault();
        data.set(EntityMetaData.Constants.DATA_FLAGS, new ByteMeta(flags));
        data.set(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta(player.getDisplayName()));
        return data;
    }
     */
}
