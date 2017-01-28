package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.data.game.ClientRequest;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.RespawnPacket;

public class PERespawnPacketTranslator implements PEPacketTranslator<RespawnPacket> {

	@Override
	public Packet[] translate(ClientConnection session, RespawnPacket packet) {
		System.err.println("Respawning Client");
		ClientRequestPacket pack = new ClientRequestPacket(ClientRequest.RESPAWN);
		return new Packet[]{pack};
	}

}
