/*
 * DragonProxy API
 * Copyright © 2016 Dragonet Foundation (https://github.com/DragonetMC/DragonProxy)
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.api.caches;

import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import java.util.Map;
import java.util.Set;
import org.dragonet.common.data.chunk.ChunkData;
import org.dragonet.common.data.itemsblocks.ItemEntry;
import org.dragonet.common.maths.ChunkPos;

/**
 *
 * @author Epic
 */
public interface IChunkCache {

    public void onTick();

    public Map<ChunkPos, Column> getChunks();

    public Set<ChunkPos> getLoadedChunks();

    public void purge();

    public void update(Column column);

    public void remove(int x, int z);

    public void sendChunk(int x, int z, boolean force);

    public void sendEmptyChunk(int x, int z);

    public void sendEmptyChunks(int radius);

    public void update(Position position, BlockState block);

    public ItemEntry translateBlock(Position position);

    public ItemStack getBlock(Position position);

    public void sendOrderedChunks();

    public ChunkData translateChunk(int columnX, int columnZ);
}
