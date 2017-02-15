package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.protocol.packet.AddPlayerPacket;
import org.dragonet.proxy.protocol.packet.PEPacket;
import org.dragonet.proxy.protocol.packet.PlayerListPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.dragonet.proxy.utilities.DefaultSkin;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;

public class PCSpawnPlayerPacketTranslator implements PCPacketTranslator<ServerSpawnPlayerPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnPlayerPacket packet) {
        try {
            CachedEntity entity = session.getEntityCache().newPlayer(packet);

            // TODO: Do we need to register the player here ?
            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
            pkAddPlayer.eid = entity.eid;

            for (EntityMetadata meta : packet.getMetadata()) {
                if (meta.getId() == 2) {
                    pkAddPlayer.username = meta.getValue().toString();
                    break;
                }
            }
            
            if (pkAddPlayer.username == null) {
                if(session.getPlayerInfoCache().containsKey(packet.getUUID())){
                    pkAddPlayer.username = session.getPlayerInfoCache().get(packet.getUUID()).getProfile().getName();
                }else{
                    return null;
                }
            }

            pkAddPlayer.uuid = packet.getUUID();

            pkAddPlayer.x = (float) packet.getX() / 32;
            pkAddPlayer.y = (float) packet.getY() / 32 + 1.62f;
            pkAddPlayer.z = (float) packet.getZ() / 32;
            pkAddPlayer.speedX = 0.0f;
            pkAddPlayer.speedY = 0.0f;
            pkAddPlayer.speedZ = 0.0f;
            pkAddPlayer.yaw = (packet.getYaw() / 256) * 360;
            pkAddPlayer.pitch = (packet.getPitch() / 256) * 360;

            pkAddPlayer.metadata = EntityMetaTranslator.translateToPE(packet.getMetadata(), null);
            
            PlayerListPacket lst = new PlayerListPacket(new PlayerListPacket.PlayerInfo(packet.getUUID(), packet.getEntityId(), pkAddPlayer.username, DefaultSkin.getDefaultSkinName(), DefaultSkin.getDefaultSkin().getData()));
            //TODO: get the default skin to work.
            return new PEPacket[]{lst, pkAddPlayer};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
