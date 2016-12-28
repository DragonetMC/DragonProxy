package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.DataPacket;

public class PCLoginSucessPacketTranslator implements PCPacketTranslator<LoginSuccessPacket> {

	@Override
	public DataPacket[] translate(UpstreamSession session, LoginSuccessPacket packet) {
		System.out.println("Recieved LoginSuccessPacket from remote server for player: " + packet.getProfile());
		return null;
	}

}
