package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.dragonet.proxy.utilities.DefaultSkin;
import org.spacehq.mc.protocol.data.game.entity.metadata.EntityMetadata;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import net.marfgamer.jraknet.RakNetPacket;
import sul.metadata.Pocket100;
import sul.protocol.pocket100.play.AddPlayer;
import sul.protocol.pocket100.types.Slot;
import sul.utils.Tuples;

public class PCSpawnPlayerPacketTranslator implements PCPacketTranslator<ServerSpawnPlayerPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerSpawnPlayerPacket packet) {
    	try {
    		CachedEntity entity = session.getEntityCache().newPlayer(packet);

            // TODO: Do we need to register the player here ?
            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
            pkAddPlayer.entityRuntimeId = entity.eid;
            pkAddPlayer.entityUniqueId = entity.eid;

            for (EntityMetadata meta : packet.getMetadata()) {
                if (meta.getId() == 2) {
                    pkAddPlayer.username = meta.getValue().toString();
                    break;
                }
            }
            
            if (pkAddPlayer.username == null) {
                if(session.getPlayerInfoCache().containsKey(packet.getUUID())){
                    pkAddPlayer.username = session.getPlayerInfoCache().get(packet.getUUID()).getProfile().getName();
                } else {
                    return new RakNetPacket[0];
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
            pkAddPlayer.item = new Item(0, 1);
            
            pkAddPlayer.encode();
            
            PlayerListPacket lst = new PlayerListPacket(); //Using nukkit packets for this because the sul packet makes no sense
            lst.entries = new PlayerListPacket.Entry[] { new PlayerListPacket.Entry(packet.getUUID(), packet.getEntityId(), pkAddPlayer.username, new Skin(DefaultSkin.getDefaultSkinBase64Encoded()))  };
            //TODO: get the default skin to work.
            //TODO: send the player's skin
            lst.encode();
            
            return new RakNetPacket[]{new RakNetPacket(lst.getByteArray()), new RakNetPacket(pkAddPlayer.getByteArray())};
        } catch (Exception e) {
            e.printStackTrace();
            return new RakNetPacket[0];
        }
    }
}