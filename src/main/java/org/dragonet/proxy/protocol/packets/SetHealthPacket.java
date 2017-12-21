package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class SetHealthPacket extends PEPacket {
	//vars
	public int health;
	
	//constructor
	public SetHealthPacket() {
		
	}
	public SetHealthPacket(int health) {
		this.health = health;
	}
	
	//public
	public int pid() {
		return ProtocolInfo.SET_HEALTH_PACKET;
	}
	
	public void encodePayload() {
		putVarInt(health);
	}
	public void decodePayload() {
		health = getVarInt();
	}
	
	//private
	
}
