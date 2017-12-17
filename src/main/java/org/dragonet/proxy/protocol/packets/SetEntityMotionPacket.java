package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/21.
 */
public class SetEntityMotionPacket extends PEPacket {
	//vars
	public long rtid;
	public Vector3F motion;
	
	//constructor
	public SetEntityMotionPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.SET_ENTITY_MOTION_PACKET;
	}
	
	public void encodePayload() {
		putUnsignedVarLong(rtid);
		putVector3F(motion);
	}
	public void decodePayload() {
		rtid = getUnsignedVarLong();
		motion = getVector3F();
	}
	
	//private
	
}
