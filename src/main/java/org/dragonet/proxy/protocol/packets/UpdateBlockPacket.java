package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

/**
 * Created on 2017/10/21.
 */
public class UpdateBlockPacket extends PEPacket {
	//vars
	public static final int FLAG_NONE = 0b0000;
	public static final int FLAG_NEIGHBORS = 0b0001;
	public static final int FLAG_NETWORK = 0b0010;
	public static final int FLAG_NOGRAPHIC = 0b0100;
	public static final int FLAG_PRIORITY = 0b1000;
	
	public static final int FLAG_ALL = FLAG_NEIGHBORS | FLAG_NETWORK;
	public static final int FLAG_ALL_PRIORITY = FLAG_ALL | FLAG_PRIORITY;
	
	public BlockPosition blockPosition;
	public int id;
	public int data;
	public int flags;
	
	//constructor
	public UpdateBlockPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.UPDATE_BLOCK_PACKET;
	}
	
	public void decodePayload() {
		blockPosition = getBlockPosition();
		id = (int) getUnsignedVarInt();
		long aux = getUnsignedVarInt();
		data = (int) (aux & 0xf);
		flags = (int) (aux >> 4);
	}
	public void encodePayload() {
		putBlockPosition(blockPosition);
		;
		putUnsignedVarInt(id);
		int aux = ((flags & 0xf) << 4) | (data & 0xf);
		putUnsignedVarInt(aux);
	}
	
	//private
	
}
