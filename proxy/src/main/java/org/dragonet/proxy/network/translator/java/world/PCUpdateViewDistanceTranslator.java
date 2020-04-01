package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewDistancePacket;
import com.nukkitx.math.GenericMath;
import com.nukkitx.math.TrigMath;
import com.nukkitx.protocol.bedrock.packet.ChunkRadiusUpdatedPacket;
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@PCPacketTranslator(packetClass = ServerUpdateViewDistancePacket.class)
public class PCUpdateViewDistanceTranslator extends PacketTranslator<ServerUpdateViewDistancePacket> {

    @Override
    public void translate(ProxySession session, ServerUpdateViewDistancePacket packet) {
        session.setRenderDistance(GenericMath.ceil(Math.min(packet.getViewDistance(), 30) * TrigMath.SQRT_OF_TWO));

        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(session.getRenderDistance());
        session.sendPacket(chunkRadiusUpdatedPacket);
    }
}
