package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class ContainerClosePacket extends PEPacket {
	//vars
	public int windowId;
	
	//constructor
	public ContainerClosePacket() {
		
	}
	public ContainerClosePacket(int windowId) {
		this.windowId = windowId;
	}
	
	//public
	public int pid() {
		return ProtocolInfo.CONTAINER_CLOSE_PACKET;
	}
	
	public void encodePayload() {
		putByte((byte) (windowId & 0xFF));
	}
	public void decodePayload() {
		windowId = getByte();
	}
	
	//private
	
}
