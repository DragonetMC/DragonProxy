package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class RequestChunkRadiusPacket extends PEPacket {
	//vars
	public int radius;
	
	//constructor
	public RequestChunkRadiusPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET;
	}
	
	public void encodePayload() {
		putVarInt(radius);
	}
	public void decodePayload() {
		radius = getVarInt();
	}
	
	//private
	
}
