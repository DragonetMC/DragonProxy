package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.Slot;

/**
 * Created on 2017/10/22.
 */
public class MobEquipmentPacket extends PEPacket {
	//vars
	public long rtid;
	public Slot item;
	public int inventorySlot;
	public int hotbarSlot;
	public int windowId;
	
	//constructor
	
	//public
	public int pid() {
		return ProtocolInfo.MOB_EQUIPMENT_PACKET;
	}
	
	public void encodePayload() {
		putUnsignedVarLong(rtid);
		putSlot(item);
		putByte((byte) (inventorySlot & 0xFF));
		putByte((byte) (hotbarSlot & 0xFF));
		putByte((byte) (windowId & 0xFF));
	}
	public void decodePayload() {

	}
	
	//private
	
}
