package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AddPlayerPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCSpawnPlayerPacketTranslator implements PCPacketTranslator<ServerSpawnPlayerPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnPlayerPacket packet) {
        try {
            CachedEntity entity = session.getEntityCache().newPlayer(packet);

            // TODO: Do we need to register the player here ?
            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
            pkAddPlayer.eid = entity.eid;
            pkAddPlayer.rtid = entity.eid;

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

            pkAddPlayer.position = new Vector3F((float) packet.getX(), (float) packet.getY() + 1.62f, (float) packet.getZ());
            pkAddPlayer.yaw = packet.getYaw();
            pkAddPlayer.pitch = packet.getPitch();

            pkAddPlayer.meta = EntityMetaData.createDefault();

            /* PlayerListEntry entry = new PlayerListEntry();
            entry.uuid = packet.getUUID();
            entry.eid = pkAddPlayer.eid;
            entry.username = pkAddPlayer.username;
            entry.skin = Skin.defaultSkin;
            entry.xboxUserId = "";
            PlayerListPacket lst = new PlayerListPacket();
            lst.type = PlayerListPacket.TYPE_ADD;
            lst.entries = new PlayerListEntry[] {
                    entry
            }; */
            //TODO: get the default skin to work.
            return new PEPacket[]{/*add, */pkAddPlayer};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
