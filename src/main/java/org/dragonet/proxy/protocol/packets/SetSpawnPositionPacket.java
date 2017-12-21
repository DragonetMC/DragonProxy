package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

/**
 * Created on 2017/10/21.
 */
public class SetSpawnPositionPacket extends PEPacket {
	//vars
	public static final int TYPE_PLAYER_SPAWN = 0;
	public static final int TYPE_WORLD_SPAWN = 1;
	
	public int type;
	public BlockPosition position;
	public boolean forced;
	
	//constructor
	public SetSpawnPositionPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.SET_SPAWN_POSITION_PACKET;
	}
	
	public void encodePayload() {
		putVarInt(type);
		putBlockPosition(position);
		putBoolean(forced);
	}
	public void decodePayload() {
		type = getVarInt();
		position = getBlockPosition();
		forced = getBoolean();
	}
	
	//private
	
}
