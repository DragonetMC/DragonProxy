package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;

import net.marfgamer.jraknet.RakNetPacket;

public class PCLoginSucessPacketTranslator implements PCPacketTranslator<LoginSuccessPacket> {

	@Override
	public RakNetPacket[] translate(ClientConnection session, LoginSuccessPacket packet) {
		DragonProxy.getLogger().info("Recieved LoginSuccessPacket from remote server for player: " + packet.getProfile());
		//PlayStatus pkPlayStatus = new PlayStatus();
		//pkPlayStatus.status = PlayStatus.OK;
		
		//return fromSulPackets(pkPlayStatus);
		
		return new RakNetPacket[0];
	}

}
