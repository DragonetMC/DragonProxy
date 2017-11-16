package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/23.
 */
public class SetEntityDataPacket extends PEPacket {
	//vars
	public long rtid;
	public EntityMetaData meta;
	
	//constructor
	public SetEntityDataPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.SET_ENTITY_DATA_PACKET;
	}
	
	public void encodePayload() {
		putUnsignedVarLong(rtid);
		if (meta == null) {
			meta = EntityMetaData.createDefault();
		}
		meta.encode();
		put(meta.getBuffer());
	}
	public void decodePayload() {

	}
	
	//private
	
}
