package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.PlayStatus;
import sul.protocol.pocket100.play.ResourcePacksInfo;
import sul.protocol.pocket100.types.Pack;

public class PCLoginSucessPacketTranslator implements PCPacketTranslator<LoginSuccessPacket> {

	@Override
	public RakNetPacket[] translate(ClientConnection session, LoginSuccessPacket packet) {
		DragonProxy.getLogger().info("Recieved LoginSuccessPacket from remote server for player: " + packet.getProfile());
		PlayStatus pkPlayStatus = new PlayStatus();
		pkPlayStatus.status = PlayStatus.OK;
		
		ResourcePacksInfo rpi = new ResourcePacksInfo();
		rpi.mustAccept = false;
		rpi.behaviourPacks = new Pack[0];
		rpi.resourcePacks = new Pack[0];
		
		return fromSulPackets(pkPlayStatus, rpi);
	}

}
