package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class ChunkRadiusUpdatedPacket extends PEPacket {
	//vars
	public int radius;
	
	//constructor
	public ChunkRadiusUpdatedPacket() {
		
	}
	public ChunkRadiusUpdatedPacket(int radius) {
		this.radius = radius;
	}
	
	//public
	public int pid() {
		return ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET;
	}
	
	public void encodePayload() {
		putVarInt(radius);
	}
	public void decodePayload() {
		radius = getVarInt();
	}
	
	//private
	
}
