package org.dragonet.proxy.network.translator.pe;

import org.dragonet.protocol.packets.EntityEventPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;

import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.packetlib.packet.Packet;

public class PEEntityEventPacketTranslator implements IPEPacketTranslator<EntityEventPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, EntityEventPacket packet) {
        CachedEntity player = session.getEntityCache().getClientEntity();
        if(packet.event == EntityEventPacket.EATING_ITEM){
            long t = System.currentTimeMillis();
            if(t-player.lastFoodPacketTime>380){
                player.lastFoodPacketTime = t;
                player.foodPacketCount = 1;
                return null;
            }
            player.foodPacketCount++;
            player.lastFoodPacketTime = t;
            if(session.getEntityCache().getClientEntity().foodPacketCount==7){
                session.getEntityCache().getClientEntity().foodPacketCount=0;
                return new Packet[]{new ClientPlayerUseItemPacket(Hand.MAIN_HAND)};
            }
        }
        return null;
    }

}
