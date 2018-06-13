/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
