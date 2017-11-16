package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.Slot;

/**
 * Created on 2017/10/21.
 */
public class InventorySlotPacket extends PEPacket {
	//vars
	public int windowId;
	public int slotId;
	public Slot item;
	
	//constructor
	public InventorySlotPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.INVENTORY_SLOT_PACKET;
	}
	
	public void encodePayload() {
		putUnsignedVarInt(windowId);
		putUnsignedVarInt(slotId);
		putSlot(item);
	}
	public void decodePayload() {
		windowId = (int) getUnsignedVarInt();
		slotId = (int) getUnsignedVarInt();
		item = getSlot();
	}
	
	//private
	
}
