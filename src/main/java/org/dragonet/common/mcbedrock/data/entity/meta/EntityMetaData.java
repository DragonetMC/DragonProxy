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
package org.dragonet.common.mcbedrock.data.entity.meta;

import java.util.HashMap;
import java.util.Map;

import org.dragonet.common.mcbedrock.data.entity.meta.type.FloatMeta;
import org.dragonet.common.mcbedrock.data.entity.meta.type.LongMeta;
import org.dragonet.common.mcbedrock.data.entity.meta.type.ShortMeta;
import org.dragonet.common.mcbedrock.utilities.BinaryStream;

public class EntityMetaData extends BinaryStream {

    public static class Constants {

        public static final int DATA_TYPE_BYTE = 0;
        public static final int DATA_TYPE_SHORT = 1;
        public static final int DATA_TYPE_INT = 2;
        public static final int DATA_TYPE_FLOAT = 3;
        public static final int DATA_TYPE_STRING = 4;
        public static final int DATA_TYPE_SLOT = 5;
        public static final int DATA_TYPE_POS = 6;
        public static final int DATA_TYPE_LONG = 7;
        public static final int DATA_TYPE_VECTOR3F = 8;

        public static final int DATA_FLAGS = 0;
        public static final int DATA_HEALTH = 1; // int (minecart/boat)
        public static final int DATA_VARIANT = 2; // int
        public static final int DATA_COLOR = 3, DATA_COLOUR = 3; // byte
        public static final int DATA_NAMETAG = 4; // string
        public static final int DATA_OWNER_EID = 5; // long
        public static final int DATA_TARGET_EID = 6; // long
        public static final int DATA_AIR = 7; // short
        public static final int DATA_POTION_COLOR = 8; // int (ARGB!)
        public static final int DATA_POTION_AMBIENT = 9; // byte
        /* 10 (byte) */
        public static final int DATA_HURT_TIME = 11; // int (minecart/boat)
        public static final int DATA_HURT_DIRECTION = 12; // int (minecart/boat)
        public static final int DATA_PADDLE_TIME_LEFT = 13; // float
        public static final int DATA_PADDLE_TIME_RIGHT = 14; // float
        public static final int DATA_EXPERIENCE_VALUE = 15; // int (xp orb)
        public static final int DATA_MINECART_DISPLAY_BLOCK = 16; // int (id | (data << 16))
        public static final int DATA_MINECART_DISPLAY_OFFSET = 17; // int
        public static final int DATA_MINECART_HAS_DISPLAY = 18; // byte (must be 1 for minecart to show block inside)
        // TODO: add more properties
        public static final int DATA_ENDERMAN_HELD_ITEM_ID = 23; // short
        public static final int DATA_ENDERMAN_HELD_ITEM_DAMAGE = 24; // short
        public static final int DATA_ENTITY_AGE = 25; // short
        /*
         * 27 (byte) player-specific flags 28 (int) player "index"? 29 (block coords)
		 * bed position
         */
        public static final int DATA_BED_POSITION = 29;
        public static final int DATA_FIREBALL_POWER_X = 30; // float
        public static final int DATA_FIREBALL_POWER_Y = 31;
        public static final int DATA_FIREBALL_POWER_Z = 32;
        /*
		 * 33 (unknown) 34 (float) fishing bobber 35 (float) fishing bobber 36 (float)
		 * fishing bobber
         */
        public static final int DATA_POTION_AUX_VALUE = 37; // short
        public static final int DATA_LEAD_HOLDER_EID = 38; // long
        public static final int DATA_SCALE = 39; // float
        public static final int DATA_INTERACTIVE_TAG = 40; // string (button text)
        public static final int DATA_NPC_SKIN_ID = 41; // string
        public static final int DATA_URL_TAG = 42; // string
        public static final int DATA_MAX_AIR = 43; // short
        public static final int DATA_MARK_VARIANT = 44; // int
        /*
		 * 45 (byte) container stuff 46 (int) container stuff 47 (int) container stuff
         */
        public static final int DATA_BLOCK_TARGET = 48; // block coords (ender crystal)
        public static final int DATA_WITHER_INVULNERABLE_TICKS = 49; // int
        public static final int DATA_WITHER_TARGET_1 = 50; // long
        public static final int DATA_WITHER_TARGET_2 = 51; // long
        public static final int DATA_WITHER_TARGET_3 = 52; // long
        /* 53 (short) */
        public static final int DATA_BOUNDING_BOX_WIDTH = 54; // float
        public static final int DATA_BOUNDING_BOX_HEIGHT = 55; // float
        public static final int DATA_FUSE_LENGTH = 56; // int
        public static final int DATA_RIDER_SEAT_POSITION = 57; // vector3f
        public static final int DATA_RIDER_ROTATION_LOCKED = 58; // byte
        public static final int DATA_RIDER_MAX_ROTATION = 59; // float
        public static final int DATA_RIDER_MIN_ROTATION = 60; // float
        public static final int DATA_AREA_EFFECT_CLOUD_RADIUS = 61; // float
        public static final int DATA_AREA_EFFECT_CLOUD_WAITING = 62; // int
        public static final int DATA_AREA_EFFECT_CLOUD_PARTICLE_ID = 63; // int
        /* 64 (int) shulker-related */
        public static final int DATA_SHULKER_ATTACH_FACE = 65; // byte
        /* 66 (short) shulker-related */
        public static final int DATA_SHULKER_ATTACH_POS = 67; // block coords
        public static final int DATA_TRADING_PLAYER_EID = 68; // long
        /* 70 (byte) command-block */
        public static final int DATA_COMMAND_BLOCK_COMMAND = 71; // string
        public static final int DATA_COMMAND_BLOCK_LAST_OUTPUT = 72; // string
        public static final int DATA_COMMAND_BLOCK_TRACK_OUTPUT = 73; // byte
        public static final int DATA_CONTROLLING_RIDER_SEAT_NUMBER = 74; // byte
        public static final int DATA_STRENGTH = 75; // int
        public static final int DATA_MAX_STRENGTH = 76; // int
        /*
		 * 77 (int) 78 (int)
         */
        //Generic flags (Boolean)
        public static final int DATA_FLAG_ONFIRE = 0;
        public static final int DATA_FLAG_SNEAKING = 1;
        public static final int DATA_FLAG_RIDING = 2;
        public static final int DATA_FLAG_SPRINTING = 3;
        public static final int DATA_FLAG_ACTION = 4;
        public static final int DATA_FLAG_INVISIBLE = 5;
        public static final int DATA_FLAG_TEMPTED = 6;
        public static final int DATA_FLAG_INLOVE = 7;
        public static final int DATA_FLAG_SADDLED = 8;
        public static final int DATA_FLAG_POWERED = 9;
        public static final int DATA_FLAG_IGNITED = 10;
        public static final int DATA_FLAG_BABY = 11;
        public static final int DATA_FLAG_CONVERTING = 12;
        public static final int DATA_FLAG_CRITICAL = 13;
        public static final int DATA_FLAG_CAN_SHOW_NAMETAG = 14;
        public static final int DATA_FLAG_ALWAYS_SHOW_NAMETAG = 15;
        public static final int DATA_FLAG_IMMOBILE = 16, DATA_FLAG_NO_AI = 16;
        public static final int DATA_FLAG_SILENT = 17;
        public static final int DATA_FLAG_WALLCLIMBING = 18;
        public static final int DATA_FLAG_CAN_CLIMB = 19;
        public static final int DATA_FLAG_SWIMMER = 20;
        public static final int DATA_FLAG_CAN_FLY = 21;
        public static final int DATA_FLAG_RESTING = 22;
        public static final int DATA_FLAG_SITTING = 23;
        public static final int DATA_FLAG_ANGRY = 24;
        public static final int DATA_FLAG_INTERESTED = 25;
        public static final int DATA_FLAG_CHARGED = 26;
        public static final int DATA_FLAG_TAMED = 27;
        public static final int DATA_FLAG_LEASHED = 28;
        public static final int DATA_FLAG_SHEARED = 29;
        public static final int DATA_FLAG_GLIDING = 30;
        public static final int DATA_FLAG_ELDER = 31;
        public static final int DATA_FLAG_MOVING = 32;
        public static final int DATA_FLAG_BREATHING = 33;
        public static final int DATA_FLAG_CHESTED = 34;
        public static final int DATA_FLAG_STACKABLE = 35;
        public static final int DATA_FLAG_SHOWBASE = 36;
        public static final int DATA_FLAG_REARING = 37;
        public static final int DATA_FLAG_VIBRATING = 38;
        public static final int DATA_FLAG_IDLING = 39;
        public static final int DATA_FLAG_EVOKER_SPELL = 40;
        public static final int DATA_FLAG_CHARGE_ATTACK = 41;
        public static final int DATA_FLAG_WASD_CONTROLLED = 42;
        public static final int DATA_FLAG_CAN_POWER_JUMP = 43;
        public static final int DATA_FLAG_LINGER = 44;
        public static final int DATA_FLAG_HAS_COLLISION = 45;
        public static final int DATA_FLAG_AFFECTED_BY_GRAVITY = 46;
        public static final int DATA_FLAG_FIRE_IMMUNE = 47;
        public static final int DATA_FLAG_DANCING = 48;
    }

    // vars
    public HashMap<Integer, IEntityMetaDataObject> map;

    // constructor
    public EntityMetaData() {
        this.map = new HashMap<>();
    }

    // public
    public static EntityMetaData from(BinaryStream source) {
        // TODO
        return createDefault();
    }

    public static EntityMetaData createDefault() {
        EntityMetaData data = new EntityMetaData();
        data.setGenericFlag(Constants.DATA_FLAG_BREATHING, true);
        data.setGenericFlag(Constants.DATA_FLAG_AFFECTED_BY_GRAVITY, true);
        data.setGenericFlag(Constants.DATA_FLAG_HAS_COLLISION, true);
        data.setGenericFlag(Constants.DATA_FLAG_CAN_CLIMB, true);
        data.set(Constants.DATA_AIR, new ShortMeta((short) 400));
        data.set(Constants.DATA_MAX_AIR, new ShortMeta((short) 400));
        // data.set(Constants.DATA_NAMETAG, new ByteArrayMeta(""));
        data.set(Constants.DATA_LEAD_HOLDER_EID, new LongMeta(-1L));
        data.set(Constants.DATA_SCALE, new FloatMeta(1.0f));
//		data.set(Constants.DATA_BED_POSITION, new BlockPositionMeta(new BlockPosition(0, 0, 0)));
        return data;
    }

    public void set(int key, IEntityMetaDataObject object) {
        this.map.put(key, object);
    }

    public void encode() {
        reset();
        putUnsignedVarInt(map.size());
        for (Map.Entry<Integer, IEntityMetaDataObject> entry : this.map.entrySet()) {
            putUnsignedVarInt(entry.getKey());
            putUnsignedVarInt(entry.getValue().type());
            entry.getValue().encode(this);
        }
    }

    public void setGenericFlag(int flagId, boolean value) {
        long flag = 0;
        if (!map.containsKey(Constants.DATA_FLAGS)) {
            map.put(Constants.DATA_FLAGS, new LongMeta(0L));
        } else {
            flag = ((LongMeta) map.get(Constants.DATA_FLAGS)).data;
        }
        boolean currValue = ((flag >> flagId) & 0b1) > 0;
        if (currValue != value) {
            flag ^= (1L << flagId);
        }
        ((LongMeta) map.get(Constants.DATA_FLAGS)).data = flag;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityMetaData [\n");
        for (Map.Entry<Integer, IEntityMetaDataObject> entry : this.map.entrySet()) {
            builder.append("\t- ID: " + entry.getKey() + " " + entry.getValue() + "\n");
        }
        builder.append("]");
        return builder.toString();
    }
}
