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

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.data.game.chunk.BlockStorage;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.chunk.ChunkData;
import org.dragonet.proxy.data.chunk.ChunkSection;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.types.BlockTranslator;
import org.dragonet.proxy.network.translator.types.ItemTranslator;
import org.dragonet.proxy.network.translator.types.item.ItemEntry;
import org.dragonet.proxy.util.PaletteManager;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
public class ChunkCache implements Cache {
    @Getter
    private Map<Vector2f, Column> chunks = new HashMap<>(); // TODO

    private ProxySession session;

    @Override
    public void purge() {
        chunks.clear();
    }

    public ItemData getBlockAt(Vector3f position) {
        Vector2f chunkPosition = new Vector2f((int) position.getX() >> 4, (int) position.getZ() >> 4);
        if (!chunks.containsKey(chunkPosition))
            return ItemData.AIR;

        Column column = chunks.get(chunkPosition);
        Chunk chunk = column.getChunks()[(int) position.getY() >> 4];
        Vector3f blockPosition = getChunkBlock((int) position.getX(), (int) position.getY(), (int) position.getZ());
        if (chunk != null) {
            BlockState blockState = chunk.getBlocks().get((int) blockPosition.getX(), (int) blockPosition.getY(), (int) blockPosition.getZ());
            return BlockTranslator.translateToBedrock(new ItemStack(blockState.getId()));
        }

        return ItemData.AIR;
    }

    public Vector3f getChunkBlock(int x, int y, int z) {
        int chunkX = x % 16;
        int chunkY = y % 16;
        int chunkZ = z % 16;

        if (chunkX < 0)
            chunkX = -chunkX;
        if (chunkY < 0)
            chunkY = -chunkY;
        if (chunkZ < 0)
            chunkZ = -chunkZ;

        return new Vector3f(chunkX, chunkY, chunkZ);
    }

    public ChunkData translateChunk(int columnX, int columnZ) {
        Vector2f columnPos = new Vector2f(columnX, columnZ);

        if (chunks.containsKey(columnPos)) {
            Column column = chunks.get(columnPos);
            ChunkData chunkData = new ChunkData(columnX, columnZ);

            for (int i = 0; i < 16; i++) {
                chunkData.getOrCreateSection(i);
            }

            // Blocks
            for (int y = 0; y < 256; y++) {
                int cy = y >> 4;

                Chunk javaChunk = null;
                try {
                    javaChunk = column.getChunks()[cy];
                } catch (Exception ex) {
                    log.warn("Chunk " + columnX + ", " + cy + ", " + columnZ + " not exist !");
                }
                if (javaChunk == null || javaChunk.isEmpty()) continue;
                BlockStorage blocks = javaChunk.getBlocks();
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        BlockState block = blocks.get(x, y & 0xF, z);
                        ItemData entry = BlockTranslator.translateToBedrock(new ItemStack(block.getId()));

                        ChunkSection section = chunkData.getSection(cy);
                        section.setFullBlock(x, y & 0xF, z, 0, entry.getId() << 4 | entry.getDamage());
                    }
                }
            }
            return chunkData;
        }
        return null;
    }
}
