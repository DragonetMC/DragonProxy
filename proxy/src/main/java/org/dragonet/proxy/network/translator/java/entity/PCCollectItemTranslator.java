package org.dragonet.proxy.network.translator.java.entity;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import com.nukkitx.protocol.bedrock.packet.TakeItemEntityPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@PCPacketTranslator(packetClass = ServerEntityCollectItemPacket.class)
public class PCCollectItemTranslator extends PacketTranslator<ServerEntityCollectItemPacket> {

    @Override
    public void translate(ProxySession session, ServerEntityCollectItemPacket packet) {
        CachedEntity collector = session.getEntityCache().getByRemoteId(packet.getCollectorEntityId());
        CachedEntity item = session.getEntityCache().getByRemoteId(packet.getCollectedEntityId());

        TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
        takeItemEntityPacket.setRuntimeEntityId(collector.getProxyEid());
        takeItemEntityPacket.setItemRuntimeEntityId(item.getProxyEid());

        session.sendPacket(takeItemEntityPacket);
    }
}
