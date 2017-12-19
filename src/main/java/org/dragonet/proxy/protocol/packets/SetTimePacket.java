package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class SetTimePacket extends PEPacket {
	//vars
	public int time;
	
	//constructor
	public SetTimePacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.SET_TIME_PACKET;
	}
	
	public void encodePayload() {
		putVarInt(time);
	}
	public void decodePayload() {
		time = getVarInt();
	}
	
	//private
	
}
