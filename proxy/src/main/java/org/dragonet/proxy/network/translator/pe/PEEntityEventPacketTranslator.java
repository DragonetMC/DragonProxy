package org.dragonet.proxy.network.translator.pe;

import org.dragonet.protocol.packets.EntityEventPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.api.translators.IPEPacketTranslator;

import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.api.sessions.IUpstreamSession;

public class PEEntityEventPacketTranslator implements IPEPacketTranslator<EntityEventPacket> {

    @Override
    public Packet[] translate(IUpstreamSession session, EntityEventPacket packet) {
        ICachedEntity player = session.getEntityCache().getClientEntity();
        if(packet.event == EntityEventPacket.EATING_ITEM){
            long t = System.currentTimeMillis();
            if(t-player.getLastFoodPacketTime()>380){
                player.setLastFoodPacketTime(t);
                player.setFoodPacketCount(1);
                return null;
            }
            player.setFoodPacketCount(player.getFoodPacketCount() + 1);
            player.setLastFoodPacketTime(t);
            if(session.getEntityCache().getClientEntity().getFoodPacketCount() ==7){
                session.getEntityCache().getClientEntity().setFoodPacketCount(0);
                player.setLastFoodPacketTime(t);
                return null;
            }
        }
        return null;
    }

}
