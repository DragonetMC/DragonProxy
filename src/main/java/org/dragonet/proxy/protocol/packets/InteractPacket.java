package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/22.
 */
public class InteractPacket extends PEPacket {
	//vars
	public static final byte ACTION_RIGHT_CLICK = 1;
	public static final byte ACTION_LEFT_CLICK = 2;
	public static final byte ACTION_LEAVE_VEHICLE = 3;
	public static final byte ACTION_MOUSEOVER = 4;
	public static final byte ACTION_OPEN_INVENTORY = 6;
	
	public byte type;
	public long targetRtid;
	public Vector3F position;
	
	//constructor
	public InteractPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.INTERACT_PACKET;
	}
	
	public void encodePayload() {
		putByte(type);
		putUnsignedVarLong(targetRtid);
		if (type == ACTION_MOUSEOVER) {
			putVector3F(position);
		}
	}
	public void decodePayload() {
		type = (byte) (getByte() & 0xFF);
		targetRtid = getUnsignedVarLong();
		if (type == ACTION_MOUSEOVER) {
			position = getVector3F();
		}
	}
	
	//private
	
}
