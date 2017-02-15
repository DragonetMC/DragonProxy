package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.data.game.ClientRequest;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.packetlib.packet.Packet;

import sul.protocol.pocket101.play.Respawn;

public class PERespawnPacketTranslator implements PEPacketTranslator<Respawn> {

	@Override
	public Packet[] translate(ClientConnection session, Respawn packet) {
		
		System.err.println("Respawning Client");
		ClientRequestPacket pack = new ClientRequestPacket(ClientRequest.RESPAWN);
		
		return new Packet[]{pack};
	}

}
