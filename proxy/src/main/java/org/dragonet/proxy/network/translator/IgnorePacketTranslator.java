package org.dragonet.proxy.network.translator;

import org.dragonet.proxy.network.UpstreamSession;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.DataPacket;

public class IgnorePacketTranslator implements PEPacketTranslator, PCPacketTranslator {

	@Override
	public Packet[] translate(UpstreamSession session, DataPacket packet) {
		System.err.println("[PE to PC] Ignoring packet: " + packet.getClass().getCanonicalName());
		return new Packet[0];
	}

	@Override
	public DataPacket[] translate(UpstreamSession session, Packet packet) {
		System.err.println("[PC to PE] Ignoring packet: " + packet.getClass().getCanonicalName());
		return new DataPacket[0];
	}

}
