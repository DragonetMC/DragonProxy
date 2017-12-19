package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/22.
 */
public class RespawnPacket extends PEPacket {
	//vars
	public Vector3F position;
	
	//constructor
	public RespawnPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.RESPAWN_PACKET;
	}
	
	public void encodePayload() {
		putVector3F(position);
	}
	public void decodePayload() {
		position = getVector3F();
	}
	
	//private
	
}
