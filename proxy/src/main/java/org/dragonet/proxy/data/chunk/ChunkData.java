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

@NoArgsConstructor
@AllArgsConstructor
public class ChunkData {
    public ChunkSection[] sections;
    private byte[] height = new byte[512];
    private byte[] biomeId = new byte[256];

   public LevelChunkPacket createChunkPacket() {
       LevelChunkPacket packet = new LevelChunkPacket();

       // Chunk sections
       int bufferSize = 1 + 4096 + 768 + 2; // TODO
       ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(bufferSize);
       try {
           for(ChunkSection section : sections) {
               section.writeToNetwork(byteBuf);
           }

           byteBuf.writeBytes(height);
           byteBuf.writeBytes(biomeId);

           // Extra data
           VarInts.writeInt(byteBuf, 0);

           byte[] chunkData = new byte[byteBuf.readableBytes()];
           byteBuf.readBytes(chunkData);

           packet.setData(chunkData);
           packet.setSubChunksLength(sections.length);
       } finally {
           byteBuf.release();
       }
       return packet;
   }
}
