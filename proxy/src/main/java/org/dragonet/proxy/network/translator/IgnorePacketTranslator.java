package org.dragonet.proxy.network.translator;

import org.dragonet.proxy.network.ClientConnection;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.DataPacket;

public class IgnorePacketTranslator implements PEPacketTranslator, PCPacketTranslator {

	@Override
	public Packet[] translate(ClientConnection session, DataPacket packet) {
		System.err.println("[PE to PC] Ignoring packet: " + packet.getClass().getCanonicalName());
		return new Packet[0];
	}

	@Override
	public DataPacket[] translate(ClientConnection session, Packet packet) {
		System.err.println("[PC to PE] Ignoring packet: " + packet.getClass().getCanonicalName());
		return new DataPacket[0];
	}

}
