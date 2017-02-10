package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.packetlib.packet.Packet;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.ChunkRadiusUpdated;
import sul.protocol.pocket100.play.RequestChunkRadius;

public class PERequestChunkRadiusPacketTranslator implements PEPacketTranslator<RequestChunkRadius> {

	@SuppressWarnings("unchecked")
	@Override
	public Packet[] translate(ClientConnection session, RequestChunkRadius packet) {
		ChunkRadiusUpdated pk = new ChunkRadiusUpdated();
		pk.radius = packet.radius;
		
		session.getUpstreamProtocol().sendPacket(new RakNetPacket(pk.encode()), session);
		
		return new Packet[0];
	}

}
