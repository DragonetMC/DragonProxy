/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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
import com.nukkitx.math.GenericMath;
import com.nukkitx.math.TrigMath;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTOutputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import io.netty.buffer.*;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.chunk.ChunkData;
import org.dragonet.proxy.data.chunk.ChunkSection;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.ChunkCache;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

import java.io.IOException;


@Log4j2
@PCPacketTranslator(packetClass = ServerChunkDataPacket.class)
public class PCChunkDataTranslator extends PacketTranslator<ServerChunkDataPacket> {

    @Override
    public void translate(ProxySession session, ServerChunkDataPacket packet) {
        Column column = packet.getColumn();
        session.getChunkCache().getJavaChunks().put(Vector2i.from(column.getX(), column.getZ()), column);

        ChunkData chunkData = session.getChunkCache().translateChunk(column.getX(), column.getZ());

        NetworkChunkPublisherUpdatePacket chunkPublisherUpdatePacket = new NetworkChunkPublisherUpdatePacket();
        chunkPublisherUpdatePacket.setPosition(session.getCachedEntity().getPosition().toInt());
        chunkPublisherUpdatePacket.setRadius(8 << 4);
        session.sendPacket(chunkPublisherUpdatePacket);

        if(column.getBiomeData() != null) { // Full chunk
            ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer();
            try {
                ChunkSection[] sections = chunkData.sections;

                int sectionCount = sections.length - 1;
                while (sectionCount >= 0 && sections[sectionCount].isEmpty()) {
                    sectionCount--;
                }
                sectionCount++;

                for (int i = 0; i < sectionCount; i++) {
                    chunkData.sections[i].writeToNetwork(buffer);
                }

                buffer.writeBytes(new byte[256]); // Biomes - 256 bytes
                buffer.writeByte(0); // Border blocks - Education Edition only

                // Extra Data
                VarInts.writeUnsignedInt(buffer, 0);

                ByteBufOutputStream stream = new ByteBufOutputStream(Unpooled.buffer());
                NBTOutputStream nbtStream = NbtUtils.createNetworkWriter(stream);
                for (CompoundTag blockEntity : chunkData.blockEntities) {
                    nbtStream.write(blockEntity);
                }

//                FastByteArrayOutputStream fast = new FastByteArrayOutputStream(new byte[256]);
//                NBTOutputStream nbtStream1 = NbtUtils.createNetworkWriter(stream);
//                for (CompoundTag blockEntity : chunkData.blockEntities) {
//                    nbtStream1.write(blockEntity);
//                }
                buffer.writeBytes(stream.buffer());

                byte[] payload = new byte[buffer.readableBytes()];
                buffer.readBytes(payload);

                LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
                levelChunkPacket.setChunkX(column.getX());
                levelChunkPacket.setChunkZ(column.getZ());
                levelChunkPacket.setCachingEnabled(false);
                levelChunkPacket.setSubChunksLength(sectionCount);
                levelChunkPacket.setData(payload);

                session.sendPacket(levelChunkPacket);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                buffer.release();
            }
        } else {
            log.warn("non full chunk");
        }
    }
}
