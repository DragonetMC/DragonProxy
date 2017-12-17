package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

/**
 * Created on 2017/10/21.
 */
public class ContainerOpenPacket extends PEPacket {
	//vars
	public int windowId;
	public int type;
	public BlockPosition position;
	public long eid;
	
	//constructor
	
	//public
	public int pid() {
		return ProtocolInfo.CONTAINER_OPEN_PACKET;
	}
	
	public void encodePayload() {
		putByte((byte) (windowId & 0xFF));
		putByte((byte) (type & 0xFF));
		putBlockPosition(position);
		putVarLong(eid);
	}
	public void decodePayload() {
		windowId = getByte();
		type = getByte();
		position = getBlockPosition();
		eid = getVarLong();
	}
	
	//private
	
}
