package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;

import cn.nukkit.network.protocol.MoveEntityPacket;
import net.marfgamer.jraknet.RakNetPacket;

public class PCEntityHeadLookPacketTranslator implements PCPacketTranslator<ServerEntityHeadLookPacket> {

	@Override
	public RakNetPacket[] translate(ClientConnection session, ServerEntityHeadLookPacket packet) {
		CachedEntity entity = session.getEntityCache().get(packet.getEntityId());
		
		if (entity == null)	{
			return new RakNetPacket[0];
		}
		
		MoveEntityPacket me = new MoveEntityPacket();
		
		me.eid = packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID) ? 0L : packet.getEntityId();
		me.x = (float) entity.x;
		me.y = (float) entity.y;
		me.z = (float) entity.z;
		me.pitch = (byte) entity.pitch;
		me.yaw = (byte) entity.yaw;
		me.headYaw = (byte) packet.getHeadYaw();
		
		return fromSulPackets(me);
	}

}
