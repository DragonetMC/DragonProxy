/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.chunk.ChunkData;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

import java.util.Arrays;
import java.util.Objects;


@Log4j2
@PCPacketTranslator(packetClass = ServerChunkDataPacket.class)
public class PCChunkDataTranslator extends PacketTranslator<ServerChunkDataPacket> {
    public static final PCChunkDataTranslator INSTANCE = new PCChunkDataTranslator();

    @Override
    public void translate(ProxySession session, ServerChunkDataPacket packet) {
        Column column = packet.getColumn();

        session.getChunkCache().getJavaChunks().put(Vector2f.from(column.getX(), column.getZ()), column);

        ChunkData chunkData = session.getChunkCache().translateChunk(column.getX(), column.getZ());
        if(chunkData != null) {
            LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
            levelChunkPacket.setChunkX(column.getX());
            levelChunkPacket.setChunkZ(column.getZ());
            levelChunkPacket.setCachingEnabled(false);

            int subChunkCount = chunkData.sections.length - 1;

            while (subChunkCount >= 0 && chunkData.sections[subChunkCount].isEmpty()) {
                subChunkCount--;
            }
            subChunkCount++;

            levelChunkPacket.setSubChunksLength(subChunkCount);

            ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer();
            try {
                for (int i = 0; i < subChunkCount; i++) {
                    chunkData.sections[i].writeToNetwork(buffer);
                }

                buffer.writeBytes(new byte[256]); // Biomes - 256 bytes
                buffer.writeByte(0); // Border blocks size - Education Edition only

                // Extra Data
                VarInts.writeUnsignedInt(buffer, 0);

                byte[] chunkData1 = new byte[buffer.readableBytes()];
                buffer.readBytes(chunkData1);
                levelChunkPacket.setData(chunkData1);
            } catch (Exception e) { // IOException
                throw new RuntimeException("Unable to create chunk packet", e);
            } finally {
                buffer.release();
            }

            session.sendPacket(levelChunkPacket);
        } else {
            log.warn("ChunkData is null");
        }
    }
}
