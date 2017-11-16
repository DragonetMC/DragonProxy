package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class RemoveEntityPacket extends PEPacket {
	//vars
	public long eid;
	
	//constructor
	public RemoveEntityPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.REMOVE_ENTITY_PACKET;
	}
	
	public void encodePayload() {
		putVarLong(eid);
	}
	public void decodePayload() {
		eid = getVarLong();
	}
	
	//private
	
}
