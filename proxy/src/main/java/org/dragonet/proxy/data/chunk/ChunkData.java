/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * Parts of the code in this file is from the NukkitX project, and it has been
 * used with permission.
 *
 * Copyright (C) 2019 NukkitX
 * https://github.com/NukkitX/Nukkit
 */
package org.dragonet.proxy.data.chunk;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

@NoArgsConstructor
@AllArgsConstructor
public class ChunkData {
    public ChunkSection[] sections;
    private byte[] height = new byte[512];
    private byte[] biomeId = new byte[256];

    @Synchronized
    public ChunkSection getOrCreateSection(int y) {
        if (y >= sections.length) {
            throw new IllegalArgumentException("expected y to be up to " + (sections.length - 1) + ", got " + y);
        }
        ChunkSection section = sections[y];
        if (section == null) {
            sections[y] = section = new ChunkSection();
        }
        return section;
    }

   public LevelChunkPacket createChunkPacket() {
       LevelChunkPacket packet = new LevelChunkPacket();

       // We don't have to send empty chunks
       int topBlank = 0;
       for (int i = sections.length - 1; i >= 0; i--) {
           ChunkSection section = sections[i];
           if (section == null || section.isEmpty()) {
               topBlank = i + 1;
           } else {
               break;
           }
       }

       // Chunk sections
       int bufferSize = 1 + 4096 * topBlank + 768 + 2;
       ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(bufferSize);
       try {
           byteBuf.markReaderIndex();
           byteBuf.writeByte((byte) topBlank);

           for (int i = 0; i < topBlank; i++) {
               getOrCreateSection(i).writeToNetwork(byteBuf);
           }

           // Heightmap
           byteBuf.writeBytes(height);
           // Biomes
           byteBuf.writeBytes(biomeId);

           // Extra data TODO: Implement
           VarInts.writeInt(byteBuf, 0);
           VarInts.writeInt(byteBuf, 0);

           byte[] chunkData = new byte[byteBuf.readableBytes()];
           byteBuf.readBytes(chunkData);
           packet.setData(chunkData);
       } finally {
           byteBuf.release();
       }
       return packet;
   }
}
