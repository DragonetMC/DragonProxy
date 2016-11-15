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
package org.dragonet.proxy.protocol.packet;

public final class PEPacketIDs {

    public static final byte PING = (byte) 0x00;
    public static final byte PONG = (byte) 0x00;
    public static final byte CLIENT_CONNECT = (byte) 0x09;
    
    public final static byte LOGIN_PACKET = 0x01;
    public final static byte PLAY_STATUS_PACKET = 0x02;
    public final static byte SERVER_HANDSHAKE = 0x03;
    public final static byte CLIENT_HANDSHAKE = 0x04;
    public final static byte DISCONNECT_PACKET = 0x05;
    public final static byte BATCH_PACKET = 0x06;
    public final static byte RESOURCE_PACKS_INFO_PACKET = 0x07;
    public final static byte RESOURCE_PACK_STACK_PACKET = 0x08;
    public final static byte RESOURCE_PACK_CLIENT_RESPONSE_PACKET = 0x09;
    public final static byte TEXT_PACKET = 0x0a;
    public final static byte SET_TIME_PACKET = 0x0b;
    public final static byte START_GAME_PACKET = 0x0c;
    public final static byte ADD_PLAYER_PACKET = 0x0d;
    public final static byte ADD_ENTITY_PACKET = 0x0e;
    public final static byte REMOVE_ENTITY_PACKET = 0x0f;
    public final static byte ADD_ITEM_ENTITY_PACKET = 0x10;
    public final static byte ADD_HANGING_ENTITY_PACKET = 0x11;
    public final static byte TAKE_ITEM_ENTITY_PACKET = 0x12;
    public final static byte MOVE_ENTITY_PACKET = 0x13;
    public final static byte MOVE_PLAYER_PACKET = 0x14;
    public final static byte RIDER_JUMP_PACKET = 0x15;
    public final static byte REMOVE_BLOCK_PACKET = 0x16;
    public final static byte UPDATE_BLOCK_PACKET = 0x17;
    public final static byte ADD_PAINTING_PACKET = 0x18;
    public final static byte EXPLODE_PACKET = 0x19;
    public final static byte LEVEL_SOUND_EVENT_PACKET = 0x1a;
    public final static byte LEVEL_EVENT_PACKET = 0x1b;
    public final static byte BLOCK_EVENT_PACKET = 0x1c;
    public final static byte ENTITY_EVENT_PACKET = 0x1d;
    public final static byte MOB_EFFECT_PACKET = 0x1e;
    public final static byte UPDATE_ATTRIBUTES_PACKET = 0x1f;
    public final static byte MOB_EQUIPMENT_PACKET = 0x20;
    public final static byte MOB_ARMOR_EQUIPMENT_PACKET = 0x21;
    public final static byte INTERACT_PACKET = 0x22;
    public final static byte USE_ITEM_PACKET = 0x23;
    public final static byte PLAYER_ACTION_PACKET = 0x24;
    public final static byte HURT_ARMOR_PACKET = 0x25;
    public final static byte SET_ENTITY_DATA_PACKET = 0x26;
    public final static byte SET_ENTITY_MOTION_PACKET = 0x27;
    public final static byte SET_ENTITY_LINK_PACKET = 0x28;
    public final static byte SET_HEALTH_PACKET = 0x29;
    public final static byte SET_SPAWN_POSITION_PACKET = 0x2a;
    public final static byte ANIMATE_PACKET = 0x2b;
    public final static byte RESPAWN_PACKET = 0x2c;
    public final static byte DROP_ITEM_PACKET = 0x2d;
    public final static byte INVENTORY_ACTION_PACKET = 0x2e;
    public final static byte CONTAINER_OPEN_PACKET = 0x2f;
    public final static byte CONTAINER_CLOSE_PACKET = 0x30;
    public final static byte CONTAINER_SET_SLOT_PACKET = 0x31;
    public final static byte CONTAINER_SET_DATA_PACKET = 0x32;
    public final static byte CONTAINER_SET_CONTENT_PACKET = 0x33;
    public final static byte CRAFTING_DATA_PACKET = 0x34;
    public final static byte CRAFTING_EVENT_PACKET = 0x35;
    public final static byte ADVENTURE_SETTINGS_PACKET = 0x36;
    public final static byte BLOCK_ENTITY_DATA_PACKET = 0x37;
    public final static byte PLAYER_INPUT_PACKET = 0x38;
    public final static byte FULL_CHUNK_DATA_PACKET = 0x39;
    public final static byte SET_COMMANDS_ENABLED_PACKET = 0x3a;
    public final static byte SET_DIFFICULTY_PACKET = 0x3b;
    public final static byte CHANGE_DIMENSION_PACKET = 0x3c;
    public final static byte SET_PLAYER_GAME_TYPE_PACKET = 0x3d;
    public final static byte PLAYER_LIST_PACKET = 0x3e;
    public final static byte EVENT_PACKET = 0x3f;
    public final static byte SPAWN_EXPERIENCE_ORB_PACKET = 0x40;
    public final static byte CLIENTBOUND_MAP_ITEM_DATA_PACKET = 0x41;
    public final static byte MAP_INFO_REQUEST_PACKET = 0x42;
    public final static byte REQUEST_CHUNK_RADIUS_PACKET = 0x43;
    public final static byte CHUNK_RADIUS_UPDATED_PACKET = 0x44;
    public final static byte ITEM_FRAME_DROP_ITEM_PACKET = 0x45;
    public final static byte REPLACE_SELECTED_ITEM_PACKET = 0x46;
    public final static byte GAME_RULES_CHANGED_PACKET = 0x47;
    public final static byte CAMERA_PACKET = 0x48;
    public final static byte ADD_ITEM_PACKET = 0x49;
    public final static byte BOSS_EVENT_PACKET = 0x4a;
    public final static byte AVAILABLE_COMMANDS_PACKET = 0x4b;
    public final static byte COMMAND_STEP_PACKET = 0x4c;
    public final static byte RESOURCE_PACK_DATA_INFO_PACKET = 0x4d;
    public final static byte RESOURCE_PACK_CHUNK_DATA_PACKET = 0x4e;
    public final static byte RESOURCE_PACK_CHUNK_REQUEST_PACKET = 0x4f;
}
