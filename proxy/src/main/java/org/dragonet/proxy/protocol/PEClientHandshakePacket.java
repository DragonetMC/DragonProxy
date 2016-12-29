package org.dragonet.proxy.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.raknet.protocol.packet.CLIENT_HANDSHAKE_DataPacket;

public class PEClientHandshakePacket extends DataPacket {

	private CLIENT_HANDSHAKE_DataPacket pkt;

	public PEClientHandshakePacket() {
		this.pkt = new CLIENT_HANDSHAKE_DataPacket();
	}
	
	@Override
	public byte pid() {
		return pkt.getID();
	}

	@Override
	public void decode() {
		pkt.decode();
	}

	@Override
	public void encode() {
		pkt.encode();
	}

	
}
