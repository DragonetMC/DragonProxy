package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

/**
 * Created on 2017/10/22.
 */
public class PlayerActionPacket extends PEPacket {
	//vars
	public static final int ACTION_START_BREAK = 0;
	public static final int ACTION_ABORT_BREAK = 1;
	public static final int ACTION_STOP_BREAK = 2;
	public static final int ACTION_GET_UPDATED_BLOCK = 3;
	public static final int ACTION_DROP_ITEM = 4;
	public static final int ACTION_START_SLEEPING = 5;
	public static final int ACTION_STOP_SLEEPING = 6;
	public static final int ACTION_RESPAWN = 7;
	public static final int ACTION_JUMP = 8;
	public static final int ACTION_START_SPRINT = 9;
	public static final int ACTION_STOP_SPRINT = 10;
	public static final int ACTION_START_SNEAK = 11;
	public static final int ACTION_STOP_SNEAK = 12;
	public static final int ACTION_DIMENSION_CHANGE_REQUEST = 13; // sent when dying in different dimension
	public static final int ACTION_DIMENSION_CHANGE_ACK = 14; // sent when spawning in a different dimension to tell the
																// server we spawned
	public static final int ACTION_START_GLIDE = 15;
	public static final int ACTION_STOP_GLIDE = 16;
	public static final int ACTION_BUILD_DENIED = 17;
	public static final int ACTION_CONTINUE_BREAK = 18;
	
	public static final int ACTION_SET_ENCHANTMENT_SEED = 20;
	
	public long rtid;
	public int action;
	public BlockPosition position;
	public int face;
	
	//constructor
	public PlayerActionPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.PLAYER_ACTION_PACKET;
	}
	
	public void encodePayload() {
		putUnsignedVarLong(rtid);
		putVarInt(action);
		putBlockPosition(position);
		putVarInt(face);
	}
	public void decodePayload() {
		rtid = getUnsignedVarLong();
		action = getVarInt();
		position = getBlockPosition();
		face = getVarInt();
	}
	
	//private
	
}
