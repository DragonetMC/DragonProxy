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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session.cache;

import com.flowpowered.math.vector.Vector2f;
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
import org.dragonet.proxy.network.translator.types.ItemTranslator;

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
                        ItemData entry = ItemTranslator.translateToBedrock(new ItemStack(block.getId()));

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
