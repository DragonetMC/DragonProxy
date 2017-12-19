package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/11/15.
 */
public class CommandRequestPacket extends PEPacket {
	//vars
	public String command;
	
	//constructor
	public CommandRequestPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.COMMAND_REQUEST_PACKET;
	}
	
	public void encodePayload() {
		putString(command);
	}
	public void decodePayload() {
		command = getString();
	}
	
	//private
	
}
