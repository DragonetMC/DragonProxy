package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.protocol.packets.ChunkRadiusUpdatedPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import org.dragonet.protocol.packets.RequestChunkRadiusPacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.PCDownstreamSession;

public class PERequestChunkRadiusPacketTranslator implements IPEPacketTranslator<RequestChunkRadiusPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, RequestChunkRadiusPacket packet) {
        session.getDataCache().put(CacheKey.PLAYER_REQUESTED_CHUNK_RADIUS, packet.radius);
        System.out.println("Requested chunk radius : " + packet.radius);
        session.sendPacket(new ChunkRadiusUpdatedPacket(((RequestChunkRadiusPacket) packet).radius));
        session.getChunkCache().sendOrderedChunks();
        ClientSettingsPacket clientSettingsPacket = new ClientSettingsPacket(
                (String) session.getDataCache().getOrDefault(CacheKey.PLAYER_LANGUAGE, "enUS"),
                (int) session.getDataCache().getOrDefault(CacheKey.PLAYER_REQUESTED_CHUNK_RADIUS, 5),
                ChatVisibility.FULL,
                false,
                new SkinPart[]{},
                Hand.OFF_HAND);
        ((PCDownstreamSession) session.getDownstream()).send(clientSettingsPacket);
        return null;

    }

}
