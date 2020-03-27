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
package org.dragonet.proxy.network.session.cache;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3i;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.chunk.ChunkData;
import org.dragonet.proxy.data.chunk.ChunkSection;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.types.BlockTranslator;

import java.util.Arrays;

@Log4j2
public class ChunkCache implements Cache {
    @Getter
    private Object2ObjectMap<Vector2f, Column> javaChunks = new Object2ObjectOpenHashMap<>();

    /**
     * Translates a chunk from Java Edition to Bedrock Edition.
     */
    public ChunkData translateChunk(int columnX, int columnZ) {
        Vector2f columnPos = Vector2f.from(columnX, columnZ);

        if (javaChunks.containsKey(columnPos)) {
            Column column = javaChunks.get(columnPos);
            ChunkData chunkData = new ChunkData(columnX, columnZ);

            for (int i = 0; i < 16; i++) {
                chunkData.getOrCreateSection(i);
            }

            // Blocks
            for (int chunkY = 0; chunkY < 256; chunkY++) {
                int cy = chunkY >> 4;

                Chunk javaChunk = null;
                try {
                    javaChunk = column.getChunks()[cy];
                } catch (Exception ex) {
                    log.warn("Chunk " + columnX + ", " + cy + ", " + columnZ + " does not exist!");
                }
                if (javaChunk == null || javaChunk.isEmpty()) continue;
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            BlockState block = javaChunk.get(x, y, z);
                            int bedrockId = BlockTranslator.translateToBedrock(block);

                            ChunkSection section = chunkData.getSection(cy);
                            section.setFullBlock(x, y, z, 0, bedrockId);
                        }
                    }
                }
            }
            return chunkData;
        }
        return null;
    }

    public BlockState getBlockAt(Vector3i position) {
        Vector2f chunkPosition = Vector2f.from(position.getX() >> 4, position.getZ() >> 4);
        if(!javaChunks.containsKey(chunkPosition)) {
            return new BlockState(0); // Air
        }
        return null; // TODO
    }

    @Override
    public void purge() {
        javaChunks.clear();
    }
}
