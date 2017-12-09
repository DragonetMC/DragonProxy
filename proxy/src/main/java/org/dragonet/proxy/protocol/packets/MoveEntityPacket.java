package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/21.
 */
public class MoveEntityPacket extends PEPacket {
	//vars
	public long rtid;
	public Vector3F position;
	public float yaw;
	public float headYaw;
	public float pitch;
	public boolean onGround;
	public boolean teleported;
	
	//constructor
	public MoveEntityPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.MOVE_ENTITY_PACKET;
	}
	
	public void encodePayload() {
		putUnsignedVarLong(rtid);
		putVector3F(position);
		putByteRotation(pitch);
		putByteRotation(headYaw);
		putByteRotation(yaw);
		putBoolean(onGround);
		putBoolean(teleported);
	}
	public void decodePayload() {
		rtid = getUnsignedVarLong();
		position = getVector3F();
		pitch = getByteRotation();
		headYaw = getByteRotation();
		yaw = getByteRotation();
		onGround = getBoolean();
		teleported = getBoolean();
	}
	
	//private
	
}
