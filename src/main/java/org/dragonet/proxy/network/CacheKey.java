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
    public static final String AUTHENTICATION_STATE = "auth_state";
    public static final String WINDOW_OPENED_ID = "window_opened_id";
    public static final String WINDOW_OPENED_SIZE = "window_opened_size";

    // Positions
    public static final String BLOCK_BREAKING_POSITION = "block_breaking_position";
    public static final String WINDOW_BLOCK_POSITION = "window_block_position";
}
