package org.dragonet.proxy.protocol;

/**
 * Class from PMMP project translated to Java
 *
 * @author PMMP Created on 2017/10/21.
 */
public class ProtocolInfo {

    /**
     * Actual Minecraft: PE protocol version
     */
    public static final int CURRENT_PROTOCOL = 201;
    /**
     * Current Minecraft PE version reported by the server. This is usually the
     * earliest currently supported version.
     */
    public static final String MINECRAFT_VERSION = "v1.2.10";
    /**
     * Version number sent to clients in ping responses.
     */
    public static final String MINECRAFT_VERSION_NETWORK = "1.2.10";

    public static final byte LOGIN_PACKET = (byte) 0x01;
    public static final byte PLAY_STATUS_PACKET = (byte) 0x02;
    public static final byte SERVER_TO_CLIENT_HANDSHAKE_PACKET = (byte) 0x03;
    public static final byte CLIENT_TO_SERVER_HANDSHAKE_PACKET = (byte) 0x04;
    public static final byte DISCONNECT_PACKET = (byte) 0x05;
    public static final byte RESOURCE_PACKS_INFO_PACKET = (byte) 0x06;
    public static final byte RESOURCE_PACK_STACK_PACKET = (byte) 0x07;
    public static final byte RESOURCE_PACK_CLIENT_RESPONSE_PACKET = (byte) 0x08;
    public static final byte TEXT_PACKET = (byte) 0x09;
    public static final byte SET_TIME_PACKET = (byte) 0x0a;
    public static final byte START_GAME_PACKET = (byte) 0x0b;
    public static final byte ADD_PLAYER_PACKET = (byte) 0x0c;
    public static final byte ADD_ENTITY_PACKET = (byte) 0x0d;
    public static final byte REMOVE_ENTITY_PACKET = (byte) 0x0e;
    public static final byte ADD_ITEM_ENTITY_PACKET = (byte) 0x0f;
    public static final byte ADD_HANGING_ENTITY_PACKET = (byte) 0x10;
    public static final byte TAKE_ITEM_ENTITY_PACKET = (byte) 0x11;
    public static final byte MOVE_ENTITY_PACKET = (byte) 0x12;
    public static final byte MOVE_PLAYER_PACKET = (byte) 0x13;
    public static final byte RIDER_JUMP_PACKET = (byte) 0x14;
    public static final byte UPDATE_BLOCK_PACKET = (byte) 0x15;
    public static final byte ADD_PAINTING_PACKET = (byte) 0x16;
    public static final byte EXPLODE_PACKET = (byte) 0x17;
    public static final byte LEVEL_SOUND_EVENT_PACKET = (byte) 0x18;
    public static final byte LEVEL_EVENT_PACKET = (byte) 0x19;
    public static final byte BLOCK_EVENT_PACKET = (byte) 0x1a;
    public static final byte ENTITY_EVENT_PACKET = (byte) 0x1b;
    public static final byte MOB_EFFECT_PACKET = (byte) 0x1c;
    public static final byte UPDATE_ATTRIBUTES_PACKET = (byte) 0x1d;
    public static final byte INVENTORY_TRANSACTION_PACKET = (byte) 0x1e;
    public static final byte MOB_EQUIPMENT_PACKET = (byte) 0x1f;
    public static final byte MOB_ARMOR_EQUIPMENT_PACKET = (byte) 0x20;
    public static final byte INTERACT_PACKET = (byte) 0x21;
    public static final byte BLOCK_PICK_REQUEST_PACKET = (byte) 0x22;
    public static final byte ENTITY_PICK_REQUEST_PACKET = (byte) 0x23;
    public static final byte PLAYER_ACTION_PACKET = (byte) 0x24;
    public static final byte ENTITY_FALL_PACKET = (byte) 0x25;
    public static final byte HURT_ARMOR_PACKET = (byte) 0x26;
    public static final byte SET_ENTITY_DATA_PACKET = (byte) 0x27;
    public static final byte SET_ENTITY_MOTION_PACKET = (byte) 0x28;
    public static final byte SET_ENTITY_LINK_PACKET = (byte) 0x29;
    public static final byte SET_HEALTH_PACKET = (byte) 0x2a;
    public static final byte SET_SPAWN_POSITION_PACKET = (byte) 0x2b;
    public static final byte ANIMATE_PACKET = (byte) 0x2c;
    public static final byte RESPAWN_PACKET = (byte) 0x2d;
    public static final byte CONTAINER_OPEN_PACKET = (byte) 0x2e;
    public static final byte CONTAINER_CLOSE_PACKET = (byte) 0x2f;
    public static final byte PLAYER_HOTBAR_PACKET = (byte) 0x30;
    public static final byte INVENTORY_CONTENT_PACKET = (byte) 0x31;
    public static final byte INVENTORY_SLOT_PACKET = (byte) 0x32;
    public static final byte CONTAINER_SET_DATA_PACKET = (byte) 0x33;
    public static final byte CRAFTING_DATA_PACKET = (byte) 0x34;
    public static final byte CRAFTING_EVENT_PACKET = (byte) 0x35;
    public static final byte GUI_DATA_PICK_ITEM_PACKET = (byte) 0x36;
    public static final byte ADVENTURE_SETTINGS_PACKET = (byte) 0x37;
    public static final byte BLOCK_ENTITY_DATA_PACKET = (byte) 0x38;
    public static final byte PLAYER_INPUT_PACKET = (byte) 0x39;
    public static final byte FULL_CHUNK_DATA_PACKET = (byte) 0x3a;
    public static final byte SET_COMMANDS_ENABLED_PACKET = (byte) 0x3b;
    public static final byte SET_DIFFICULTY_PACKET = (byte) 0x3c;
    public static final byte CHANGE_DIMENSION_PACKET = (byte) 0x3d;
    public static final byte SET_PLAYER_GAME_TYPE_PACKET = (byte) 0x3e;
    public static final byte PLAYER_LIST_PACKET = (byte) 0x3f;
    public static final byte SIMPLE_EVENT_PACKET = (byte) 0x40;
    public static final byte EVENT_PACKET = (byte) 0x41;
    public static final byte SPAWN_EXPERIENCE_ORB_PACKET = (byte) 0x42;
    public static final byte CLIENTBOUND_MAP_ITEM_DATA_PACKET = (byte) 0x43;
    public static final byte MAP_INFO_REQUEST_PACKET = (byte) 0x44;
    public static final byte REQUEST_CHUNK_RADIUS_PACKET = (byte) 0x45;
    public static final byte CHUNK_RADIUS_UPDATED_PACKET = (byte) 0x46;
    public static final byte ITEM_FRAME_DROP_ITEM_PACKET = (byte) 0x47;
    public static final byte GAME_RULES_CHANGED_PACKET = (byte) 0x48;
    public static final byte CAMERA_PACKET = (byte) 0x49;
    public static final byte BOSS_EVENT_PACKET = (byte) 0x4a;
    public static final byte SHOW_CREDITS_PACKET = (byte) 0x4b;
    public static final byte AVAILABLE_COMMANDS_PACKET = (byte) 0x4c;
    public static final byte COMMAND_REQUEST_PACKET = (byte) 0x4d;
    public static final byte COMMAND_BLOCK_UPDATE_PACKET = (byte) 0x4e;
    public static final byte COMMAND_OUTPUT_PACKET = (byte) 0x4f;
    public static final byte UPDATE_TRADE_PACKET = (byte) 0x50;
    public static final byte UPDATE_EQUIP_PACKET = (byte) 0x51;
    public static final byte RESOURCE_PACK_DATA_INFO_PACKET = (byte) 0x52;
    public static final byte RESOURCE_PACK_CHUNK_DATA_PACKET = (byte) 0x53;
    public static final byte RESOURCE_PACK_CHUNK_REQUEST_PACKET = (byte) 0x54;
    public static final byte TRANSFER_PACKET = (byte) 0x55;
    public static final byte PLAY_SOUND_PACKET = (byte) 0x56;
    public static final byte STOP_SOUND_PACKET = (byte) 0x57;
    public static final byte SET_TITLE_PACKET = (byte) 0x58;
    public static final byte ADD_BEHAVIOR_TREE_PACKET = (byte) 0x59;
    public static final byte STRUCTURE_BLOCK_UPDATE_PACKET = (byte) 0x5a;
    public static final byte SHOW_STORE_OFFER_PACKET = (byte) 0x5b;
    public static final byte PURCHASE_RECEIPT_PACKET = (byte) 0x5c;
    public static final byte PLAYER_SKIN_PACKET = (byte) 0x5d;
    public static final byte SUB_CLIENT_LOGIN_PACKET = (byte) 0x5e;
    public static final byte W_S_CONNECT_PACKET = (byte) 0x5f;
    public static final byte SET_LAST_HURT_BY_PACKET = (byte) 0x60;
    public static final byte BOOK_EDIT_PACKET = (byte) 0x61;
    public static final byte NPC_REQUEST_PACKET = (byte) 0x62;
    public static final byte PHOTO_TRANSFER_PACKET = (byte) 0x63;
    public static final byte MODAL_FORM_REQUEST_PACKET = (byte) 0x64;
    public static final byte MODAL_FORM_RESPONSE_PACKET = (byte) 0x65;
    public static final byte SERVER_SETTINGS_REQUEST_PACKET = (byte) 0x66;
    public static final byte SERVER_SETTINGS_RESPONSE_PACKET = (byte) 0x67;
    public static final byte SHOW_PROFILE_PACKET = (byte) 0x68;
    public static final byte SET_DEFAULT_GAME_TYPE_PACKET = (byte) 0x69;
}
