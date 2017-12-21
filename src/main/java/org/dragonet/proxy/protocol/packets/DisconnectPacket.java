package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class DisconnectPacket extends PEPacket {
	//vars
	public boolean hideDisconnectionScreen;
	public String message;
	
	//constructor
	public DisconnectPacket() {
		
	}
	public DisconnectPacket(boolean hideDisconnectionScreen, String message) {
		this.hideDisconnectionScreen = hideDisconnectionScreen;
		this.message = message;
	}
	
	//public
	public int pid() {
		return ProtocolInfo.DISCONNECT_PACKET;
	}
	
	public void encodePayload() {
		putBoolean(hideDisconnectionScreen);
		putString(message);
	}
	public void decodePayload() {
		hideDisconnectionScreen = getBoolean();
		message = getString();
	}
	
	//private
	
}
