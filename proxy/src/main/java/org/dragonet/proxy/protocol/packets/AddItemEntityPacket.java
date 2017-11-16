package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.Slot;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/21.
 */
public class AddItemEntityPacket extends PEPacket {
	//vars
	public long rtid;
	public long eid;
	public Slot item;
	public Vector3F position;
	public Vector3F motion;
	public EntityMetaData metadata;
	
	//constructor
	public AddItemEntityPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.ADD_ITEM_ENTITY_PACKET;
	}
	
	public void encodePayload() {
		putVarLong(rtid);
		putUnsignedVarLong(eid);
		putVector3F(position);
		putVector3F(position);
		if (metadata != null) {
			metadata.encode();
			put(metadata.getBuffer());
		} else {
			putUnsignedVarInt(0);
		}
	}
	public void decodePayload() {
		rtid = getVarLong();
		eid = getUnsignedVarLong();
		item = getSlot();
		position = getVector3F();
		motion = getVector3F();
		metadata = EntityMetaData.from(this);
	}
	
	//private
	
}
