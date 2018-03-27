package org.dragonet.proxy.network.translator.pc;

import org.dragonet.protocol.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;

public class PCSpawnParticlePacketTranslator  implements IPCPacketTranslator<ServerSpawnParticlePacket> {

	@Override
	public PEPacket[] translate(UpstreamSession session, ServerSpawnParticlePacket packet) {
		
		return null;
	}

}
