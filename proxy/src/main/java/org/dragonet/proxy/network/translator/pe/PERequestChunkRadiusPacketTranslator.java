package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket;
import cn.nukkit.network.protocol.RequestChunkRadiusPacket;

public class PERequestChunkRadiusPacketTranslator implements PEPacketTranslator<RequestChunkRadiusPacket> {

	@Override
	public Packet[] translate(ClientConnection session, RequestChunkRadiusPacket packet) {
		ChunkRadiusUpdatedPacket pk = new ChunkRadiusUpdatedPacket();
		pk.radius = packet.radius;
		session.sendPacket(pk);
		return null;
	}

}
