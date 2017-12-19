package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created on 2017/10/22.
 */
public class BlockEntityDataPacket extends PEPacket {
	//vars
	public BlockPosition blockPosition;
	public CompoundTag tag;
	
	//constructor
	public BlockEntityDataPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.BLOCK_ENTITY_DATA_PACKET;
	}
	
	public void encodePayload() {
		putBlockPosition(blockPosition);
		if (tag != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				NBTIO.writeTag(bos, tag);
				bos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			put(bos.toByteArray());
		}
	}
	public void decodePayload() {
		blockPosition = getBlockPosition();
		try {
			tag = (CompoundTag) NBTIO.readTag(new ByteArrayInputStream(get(getBuffer().length - offset)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//private
	
}
