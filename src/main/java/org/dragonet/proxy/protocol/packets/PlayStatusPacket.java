package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Versioning;

/**
 * Created on 2017/10/21.
 */
public class PlayStatusPacket extends PEPacket {
	//vars
	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_FAILED_CLIENT = 1;
	public static final int LOGIN_FAILED_SERVER = 2;
	public static final int PLAYER_SPAWN = 3;
	public static final int LOGIN_FAILED_INVALID_TENANT = 4;
	public static final int LOGIN_FAILED_VANILLA_EDU = 5;
	public static final int LOGIN_FAILED_EDU_VANILLA = 6;
	
	public int status;
	public int protocol = Versioning.MINECRAFT_PE_PROTOCOL;
	
	//constructor
	public PlayStatusPacket() {
		
	}
	public PlayStatusPacket(int status) {
		this.status = status;
	}
	public PlayStatusPacket(int status, int protocol) {
		this.status = status;
		this.protocol = protocol;
	}
	
	//public
	public int pid() {
		return ProtocolInfo.PLAY_STATUS_PACKET;
	}
	
	public void encodeHeader() {
		if (protocol < 130) { // MCPE <= 1.1
			putByte((byte) (pid() & 0xFF));
		} else {
			super.encodeHeader();
		}
	}
	public void encodePayload() {
		putInt(status);
	}
	public void decodePayload() {
		status = getInt();
	}
	
	//private
	
}
