package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.PlayerListEntry;
import org.dragonet.proxy.protocol.type.Skin;

/**
 * Created on 2017/10/22.
 */
public class PlayerListPacket extends PEPacket {
	//vars
	public static final byte TYPE_ADD = 0;
	public static final byte TYPE_REMOVE = 1;
	public byte type;
	public PlayerListEntry[] entries;
	
	//constructor
	public PlayerListPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.PLAYER_LIST_PACKET;
	}
	
	public void encodePayload() {
		putByte(type);
		if (entries != null && entries.length > 0) {
			putUnsignedVarInt(entries.length);
			for (PlayerListEntry e : entries) {
				putUUID(e.uuid);
				if (type == TYPE_ADD) {
					putUnsignedVarLong(e.eid);
					putString(e.username);
					e.skin.write(this);
					putString(e.xboxUserId);
				}
			}
		} else {
			putUnsignedVarInt(0);
		}
	}
	public void decodePayload() {
		type = (byte) (getByte() & 0xFF);
		int len = (int) getUnsignedVarInt();
		entries = new PlayerListEntry[len];
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				entries[i] = new PlayerListEntry();
				entries[i].uuid = getUUID();
				if (type == TYPE_ADD) {
					entries[i].eid = getVarLong();
					entries[i].username = getString();
					entries[i].skin = Skin.read(this);
					entries[i].xboxUserId = getString();
				}
			}
		}
	}
	
	//private
	
}
