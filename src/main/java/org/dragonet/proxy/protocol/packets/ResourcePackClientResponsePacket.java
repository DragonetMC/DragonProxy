package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class ResourcePackClientResponsePacket extends PEPacket {
	//vars
	public static final byte STATUS_REFUSED = 1;
	public static final byte STATUS_SEND_PACKS = 2;
	public static final byte STATUS_HAVE_ALL_PACKS = 3;
	public static final byte STATUS_COMPLETED = 4;
	
	public byte status;
	public byte[][] packs;
	
	//constructor
	public ResourcePackClientResponsePacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET;
	}
	
	public void encodePayload() {
		putByte(status);
		if (packs != null && packs.length > 0) {
			putLShort(packs.length);
			for (byte[] p : packs) {
				putByteArray(p);
			}
		} else {
			putLShort(0);
		}
	}
	public void decodePayload() {
		status = (byte) (getByte() & 0xFF);
		int len = getLShort();
		packs = new byte[len][];
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				packs[i] = getByteArray();
			}
		}
	}
	
	//private
	
}
