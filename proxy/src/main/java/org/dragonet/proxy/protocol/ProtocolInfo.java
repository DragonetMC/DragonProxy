package org.dragonet.proxy.protocol;

/**
 * Class from PMMP project translated to Java
 * @author PMMP
 * Created on 2017/10/21.
 */
public class ProtocolInfo {

    /**
     * Actual Minecraft: PE protocol version
     */
    public final static int CURRENT_PROTOCOL = 137;
    /**
     * Current Minecraft PE version reported by the server. This is usually the earliest currently supported version.
     */
    public final static String MINECRAFT_VERSION = "v1.2.0.81";
    /**
     * Version number sent to clients in ping responses.
     */
    public final static String MINECRAFT_VERSION_NETWORK = "1.2.0.81";


    public final static byte LOGIN_PACKET = (byte) 0x01;
    public final static byte PLAY_STATUS_PACKET = (byte) 0x02;
    public final static byte SERVER_TO_CLIENT_HANDSHAKE_PACKET = (byte) 0x03;
    public final static byte CLIENT_TO_SERVER_HANDSHAKE_PACKET = (byte) 0x04;
    public final static byte DISCONNECT_PACKET = (byte) 0x05;
    public final static byte RESOURCE_PACKS_INFO_PACKET = (byte) 0x06;
    public final static byte RESOURCE_PACK_STACK_PACKET = (byte) 0x07;
    public final static byte RESOURCE_PACK_CLIENT_RESPONSE_PACKET = (byte) 0x08;
    public final static byte TEXT_PACKET = (byte) 0x09;
    public final static byte SET_TIME_PACKET = (byte) 0x0a;
    public final static byte START_GAME_PACKET = (byte) 0x0b;
    public final static byte ADD_PLAYER_PACKET = (byte) 0x0c;
    public final static byte ADD_ENTITY_PACKET = (byte) 0x0d;
    public final static byte REMOVE_ENTITY_PACKET = (byte) 0x0e;
    public final static byte ADD_ITEM_ENTITY_PACKET = (byte) 0x0f;
    public final static byte ADD_HANGING_ENTITY_PACKET = (byte) 0x10;
    public final static byte TAKE_ITEM_ENTITY_PACKET = (byte) 0x11;
    public final static byte MOVE_ENTITY_PACKET = (byte) 0x12;
    public final static byte MOVE_PLAYER_PACKET = (byte) 0x13;
    public final static byte RIDER_JUMP_PACKET = (byte) 0x14;
    public final static byte UPDATE_BLOCK_PACKET = (byte) 0x15;
    public final static byte ADD_PAINTING_PACKET = (byte) 0x16;
    public final static byte EXPLODE_PACKET = (byte) 0x17;
    public final static byte LEVEL_SOUND_EVENT_PACKET = (byte) 0x18;
    public final static byte LEVEL_EVENT_PACKET = (byte) 0x19;
    public final static byte BLOCK_EVENT_PACKET = (byte) 0x1a;
    public final static byte ENTITY_EVENT_PACKET = (byte) 0x1b;
    public final static byte MOB_EFFECT_PACKET = (byte) 0x1c;
    public final static byte UPDATE_ATTRIBUTES_PACKET = (byte) 0x1d;
    public final static byte INVENTORY_TRANSACTION_PACKET = (byte) 0x1e;
    public final static byte MOB_EQUIPMENT_PACKET = (byte) 0x1f;
    public final static byte MOB_ARMOR_EQUIPMENT_PACKET = (byte) 0x20;
    public final static byte INTERACT_PACKET = (byte) 0x21;
    public final static byte BLOCK_PICK_REQUEST_PACKET = (byte) 0x22;
    public final static byte ENTITY_PICK_REQUEST_PACKET = (byte) 0x23;
    public final static byte PLAYER_ACTION_PACKET = (byte) 0x24;
    public final static byte ENTITY_FALL_PACKET = (byte) 0x25;
    public final static byte HURT_ARMOR_PACKET = (byte) 0x26;
    public final static byte SET_ENTITY_DATA_PACKET = (byte) 0x27;
    public final static byte SET_ENTITY_MOTION_PACKET = (byte) 0x28;
    public final static byte SET_ENTITY_LINK_PACKET = (byte) 0x29;
    public final static byte SET_HEALTH_PACKET = (byte) 0x2a;
    public final static byte SET_SPAWN_POSITION_PACKET = (byte) 0x2b;
    public final static byte ANIMATE_PACKET = (byte) 0x2c;
    public final static byte RESPAWN_PACKET = (byte) 0x2d;
    public final static byte CONTAINER_OPEN_PACKET = (byte) 0x2e;
    public final static byte CONTAINER_CLOSE_PACKET = (byte) 0x2f;
    public final static byte PLAYER_HOTBAR_PACKET = (byte) 0x30;
    public final static byte INVENTORY_CONTENT_PACKET = (byte) 0x31;
    public final static byte INVENTORY_SLOT_PACKET = (byte) 0x32;
    public final static byte CONTAINER_SET_DATA_PACKET = (byte) 0x33;
    public final static byte CRAFTING_DATA_PACKET = (byte) 0x34;
    public final static byte CRAFTING_EVENT_PACKET = (byte) 0x35;
    public final static byte GUI_DATA_PICK_ITEM_PACKET = (byte) 0x36;
    public final static byte ADVENTURE_SETTINGS_PACKET = (byte) 0x37;
    public final static byte BLOCK_ENTITY_DATA_PACKET = (byte) 0x38;
    public final static byte PLAYER_INPUT_PACKET = (byte) 0x39;
    public final static byte FULL_CHUNK_DATA_PACKET = (byte) 0x3a;
    public final static byte SET_COMMANDS_ENABLED_PACKET = (byte) 0x3b;
    public final static byte SET_DIFFICULTY_PACKET = (byte) 0x3c;
    public final static byte CHANGE_DIMENSION_PACKET = (byte) 0x3d;
    public final static byte SET_PLAYER_GAME_TYPE_PACKET = (byte) 0x3e;
    public final static byte PLAYER_LIST_PACKET = (byte) 0x3f;
    public final static byte SIMPLE_EVENT_PACKET = (byte) 0x40;
    public final static byte EVENT_PACKET = (byte) 0x41;
    public final static byte SPAWN_EXPERIENCE_ORB_PACKET = (byte) 0x42;
    public final static byte CLIENTBOUND_MAP_ITEM_DATA_PACKET = (byte) 0x43;
    public final static byte MAP_INFO_REQUEST_PACKET = (byte) 0x44;
    public final static byte REQUEST_CHUNK_RADIUS_PACKET = (byte) 0x45;
    public final static byte CHUNK_RADIUS_UPDATED_PACKET = (byte) 0x46;
    public final static byte ITEM_FRAME_DROP_ITEM_PACKET = (byte) 0x47;
    public final static byte GAME_RULES_CHANGED_PACKET = (byte) 0x48;
    public final static byte CAMERA_PACKET = (byte) 0x49;
    public final static byte BOSS_EVENT_PACKET = (byte) 0x4a;
    public final static byte SHOW_CREDITS_PACKET = (byte) 0x4b;
    public final static byte AVAILABLE_COMMANDS_PACKET = (byte) 0x4c;
    public final static byte COMMAND_REQUEST_PACKET = (byte) 0x4d;
    public final static byte COMMAND_BLOCK_UPDATE_PACKET = (byte) 0x4e;
    public final static byte COMMAND_OUTPUT_PACKET = (byte) 0x4f;
    public final static byte UPDATE_TRADE_PACKET = (byte) 0x50;
    public final static byte UPDATE_EQUIP_PACKET = (byte) 0x51;
    public final static byte RESOURCE_PACK_DATA_INFO_PACKET = (byte) 0x52;
    public final static byte RESOURCE_PACK_CHUNK_DATA_PACKET = (byte) 0x53;
    public final static byte RESOURCE_PACK_CHUNK_REQUEST_PACKET = (byte) 0x54;
    public final static byte TRANSFER_PACKET = (byte) 0x55;
    public final static byte PLAY_SOUND_PACKET = (byte) 0x56;
    public final static byte STOP_SOUND_PACKET = (byte) 0x57;
    public final static byte SET_TITLE_PACKET = (byte) 0x58;
    public final static byte ADD_BEHAVIOR_TREE_PACKET = (byte) 0x59;
    public final static byte STRUCTURE_BLOCK_UPDATE_PACKET = (byte) 0x5a;
    public final static byte SHOW_STORE_OFFER_PACKET = (byte) 0x5b;
    public final static byte PURCHASE_RECEIPT_PACKET = (byte) 0x5c;
    public final static byte PLAYER_SKIN_PACKET = (byte) 0x5d;
    public final static byte SUB_CLIENT_LOGIN_PACKET = (byte) 0x5e;
    public final static byte W_S_CONNECT_PACKET = (byte) 0x5f;
    public final static byte SET_LAST_HURT_BY_PACKET = (byte) 0x60;
    public final static byte BOOK_EDIT_PACKET = (byte) 0x61;
    public final static byte NPC_REQUEST_PACKET = (byte) 0x62;
    public final static byte PHOTO_TRANSFER_PACKET = (byte) 0x63;
    public final static byte MODAL_FORM_REQUEST_PACKET = (byte) 0x64;
    public final static byte MODAL_FORM_RESPONSE_PACKET = (byte) 0x65;
    public final static byte SERVER_SETTINGS_REQUEST_PACKET = (byte) 0x66;
    public final static byte SERVER_SETTINGS_RESPONSE_PACKET = (byte) 0x67;
    public final static byte SHOW_PROFILE_PACKET = (byte) 0x68;
    public final static byte SET_DEFAULT_GAME_TYPE_PACKET = (byte) 0x69;

}
