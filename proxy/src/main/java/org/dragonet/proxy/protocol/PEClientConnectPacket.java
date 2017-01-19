package org.dragonet.proxy.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.raknet.protocol.packet.CLIENT_CONNECT_DataPacket;

public class PEClientConnectPacket extends DataPacket {

	private CLIENT_CONNECT_DataPacket pkt;

	public PEClientConnectPacket() {
		this.pkt = new CLIENT_CONNECT_DataPacket();
	}
	
	@Override
	public byte pid() {
		return this.pkt.getID();
	}

	@Override
	public void decode() {
		this.pkt.decode();
	}

	@Override
	public void encode() {
		this.pkt.encode();
	}

}
