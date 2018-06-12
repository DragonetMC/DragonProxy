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
package org.dragonet.proxy.network;

public final class CacheKey {

    // PEPackets
    public static final String PACKET_JOIN_GAME_PACKET = "achedJoinGamePacket";
    public static final String PACKET_LOGIN_PACKET = "achedLoginPacket";

    // ints
    public static final String PLAYER_EID = "player_entity_id";
    public static final String PLAYER_SELECTED_SLOT = "player_selected_slot";
    public static final String AUTHENTICATION_STATE = "auth_state";
    public static final String CURRENT_WINDOW_ID = "window_opened_id";
    public static final String CURRENT_WINDOW_SIZE = "window_opened_size";

    // Slot
    public static final String CURRENT_TRANSACTION_CREATIVE = "window_cursor_creative";

    // Positions
    public static final String BLOCK_BREAKING_POSITION = "block_breaking_position";
    public static final String CURRENT_WINDOW_POSITION = "window_block_position";

    // Settings
    public static final String PLAYER_LANGUAGE = "player_language";
    public static final String PLAYER_REQUESTED_CHUNK_RADIUS = "player_requested_chunk_radius";
}
