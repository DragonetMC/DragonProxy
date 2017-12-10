package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.dragonet.proxy.entity.EntityType;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AddPlayerPacket;
import org.dragonet.proxy.protocol.packets.PlayerSkinPacket;
import org.dragonet.proxy.utilities.Constants;
import org.dragonet.proxy.utilities.Vector3F;

public class PCSpawnPlayerPacketTranslator implements IPCPacketTranslator<ServerSpawnPlayerPacket> {
	// vars

	// constructor
	public PCSpawnPlayerPacketTranslator() {

	}

	// public
	public PEPacket[] translate(UpstreamSession session, ServerSpawnPlayerPacket packet) {
		try {
			CachedEntity entity = session.getEntityCache().newPlayer(packet);

                        if (session.isSpawned())
                        {
                            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
                            pkAddPlayer.eid = entity.proxyEid;
                            pkAddPlayer.rtid = entity.proxyEid;

                            for (EntityMetadata meta : packet.getMetadata()) {
                                    if (meta.getId() == 2) {
                                            pkAddPlayer.username = meta.getValue().toString();
                                            break;
                                    }
                            }

                            if (pkAddPlayer.username == null) {
                                    if (session.getPlayerInfoCache().containsKey(packet.getUUID())) {
                                            pkAddPlayer.username = session.getPlayerInfoCache().get(packet.getUUID()).getProfile().getName();
                                    } else {
                                            return null;
                                    }
                            }

                            pkAddPlayer.uuid = packet.getUUID();

                            pkAddPlayer.position = new Vector3F((float) packet.getX(), (float) packet.getY() + Constants.PLAYER_HEAD_OFFSET, (float) packet.getZ());
                            pkAddPlayer.motion = Vector3F.ZERO;
                            pkAddPlayer.yaw = packet.getYaw();
                            pkAddPlayer.pitch = packet.getPitch();

                            pkAddPlayer.meta = EntityMetaTranslator.translateToPE(entity.pcMeta, EntityType.PLAYER);

                            PlayerSkinPacket skin = new PlayerSkinPacket(packet.getUUID());

                            return new PEPacket[] { pkAddPlayer, skin };
                        }
                return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
                        }
                    
	// private

}
