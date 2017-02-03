package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;

public class PCLoginSucessPacketTranslator implements PCPacketTranslator<LoginSuccessPacket> {

	@Override
	public DataPacket[] translate(ClientConnection session, LoginSuccessPacket packet) {
		DragonProxy.getLogger().info("Recieved LoginSuccessPacket from remote server for player: " + packet.getProfile());
		PlayStatusPacket pkPlayStatus = new PlayStatusPacket();
		pkPlayStatus.status = PlayStatusPacket.LOGIN_SUCCESS;

		// ResourcePacksInfoPacket Required; Causes the client to switch to the "locating server" screen
		return new DataPacket[] {pkPlayStatus, new ResourcePacksInfoPacket()};
	}

}
