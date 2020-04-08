package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import com.nukkitx.math.vector.Vector2i;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@PCPacketTranslator(packetClass = ServerUnloadChunkPacket.class)
public class PCUnloadChunkTranslator extends PacketTranslator<ServerUnloadChunkPacket> {

    @Override
    public void translate(ProxySession session, ServerUnloadChunkPacket packet) {
        session.getChunkCache().getJavaChunks().remove(Vector2i.from(packet.getX(), packet.getZ()));
    }
}
