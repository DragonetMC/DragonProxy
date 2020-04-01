package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewDistancePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewPositionPacket;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.packet.ChunkRadiusUpdatedPacket;
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@PCPacketTranslator(packetClass = ServerUpdateViewPositionPacket.class)
public class PCUpdateViewPositionTranslator extends PacketTranslator<ServerUpdateViewPositionPacket> {

    @Override
    public void translate(ProxySession session, ServerUpdateViewPositionPacket packet) {
//        NetworkChunkPublisherUpdatePacket chunkPublisherUpdatePacket = new NetworkChunkPublisherUpdatePacket();
//        chunkPublisherUpdatePacket.setPosition(Vector3i.from(packet.getChunkX() << 4, 0, packet.getChunkZ() << 4));
//        chunkPublisherUpdatePacket.setRadius(session.getRenderDistance() << 4);
//        session.sendPacket(chunkPublisherUpdatePacket);
    }
}
