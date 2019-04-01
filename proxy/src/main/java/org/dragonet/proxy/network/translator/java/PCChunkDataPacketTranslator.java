package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.nukkitx.protocol.bedrock.packet.FullChunkDataPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCChunkDataPacketTranslator implements PacketTranslator<ServerChunkDataPacket> {

    @Override
    public void translate(ProxySession session, ServerChunkDataPacket packet) {
        Column column = packet.getColumn();
        FullChunkDataPacket fullChunkData = new FullChunkDataPacket();
        fullChunkData.setChunkX(column.getX());
        fullChunkData.setChunkZ(column.getZ());

        // TODO: 01/04/2019 Finish off chunk data

        session.getUpstream().sendPacketImmediately(fullChunkData);
    }
}
