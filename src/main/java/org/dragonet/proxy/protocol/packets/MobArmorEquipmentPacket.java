package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.Slot;

public class MobArmorEquipmentPacket extends PEPacket {
	
	public long rtid;
	public Slot helmet;
	public Slot chestplate;
	public Slot leggings;
	public Slot boots;

	@Override
	public int pid() {
		return ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;
	}

	@Override
	public void encodePayload() {
		putUnsignedVarLong(rtid);
		putSlot(helmet);
		putSlot(chestplate);
		putSlot(leggings);
		putSlot(boots);
		
	}

	@Override
	public void decodePayload() {
		rtid = getUnsignedVarLong();
		helmet = getSlot();
		chestplate = getSlot();
		leggings = getSlot();
		boots = getSlot();
	}

}
