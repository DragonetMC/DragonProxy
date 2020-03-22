package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewDistancePacket;
import com.nukkitx.protocol.bedrock.packet.ChunkRadiusUpdatedPacket;
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@PCPacketTranslator(packetClass = ServerUpdateViewDistancePacket.class)
public class PCUpdateViewDistanceTranslator extends PacketTranslator<ServerUpdateViewDistancePacket> {

    @Override
    public void translate(ProxySession session, ServerUpdateViewDistancePacket packet) {
        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(packet.getViewDistance());
        session.sendPacket(chunkRadiusUpdatedPacket);
    }
}
