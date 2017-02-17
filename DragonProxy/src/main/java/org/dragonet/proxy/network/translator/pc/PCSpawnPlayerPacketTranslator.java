package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.metadata.EntityMetadata;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import sul.protocol.pocket101.play.AddPlayer;
import sul.protocol.pocket101.play.PlayerList;
import sul.protocol.pocket101.play.PlayerList.Add;

import sul.protocol.pocket101.types.Slot;
import sul.utils.Tuples;

public class PCSpawnPlayerPacketTranslator implements PCPacketTranslator<ServerSpawnPlayerPacket> {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, ServerSpawnPlayerPacket packet) {
        try {
            CachedEntity entity = session.getEntityCache().newPlayer(packet);

            // TODO: Do we need to register the player here ?
            AddPlayer pkAddPlayer = new AddPlayer();
            pkAddPlayer.entityId = entity.eid;
            pkAddPlayer.runtimeId = entity.eid;

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
                    return new sul.utils.Packet[0];
                }
            }

            pkAddPlayer.uuid = packet.getUUID();

            pkAddPlayer.position = new Tuples.FloatXYZ((float) packet.getX() / 32, (float) packet.getY() / 32 + 1.62f, (float) packet.getZ() / 32);
            pkAddPlayer.motion = new Tuples.FloatXYZ();
            pkAddPlayer.yaw = (packet.getYaw() / 256) * 360;
            pkAddPlayer.pitch = (packet.getPitch() / 256) * 360;
            pkAddPlayer.heldItem = new Slot(1, 0, new byte[0]);

            pkAddPlayer.encode();

            /*PlayerList obj;
            obj = new Add();
            
            Add lst = new Add();
            //lst.entries = new PlayerListPacket.Entry[] { new PlayerListPacket.Entry(packet.getUUID(), packet.getEntityId(), pkAddPlayer.username, new Skin(DefaultSkin.getDefaultSkinBase64Encoded()))  };
            //TODO: get the default skin to work.
            //TODO: send the player's skin
            lst.encode();*/

            return new sul.utils.Packet[]{/*lst,*/ pkAddPlayer};
        } catch (Exception e) {
            e.printStackTrace();
            return new sul.utils.Packet[0];
        }
    }
}
