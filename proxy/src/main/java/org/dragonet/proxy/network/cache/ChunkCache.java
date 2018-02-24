/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details.
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.cache;

import com.github.steveice10.mc.protocol.data.game.chunk.BlockStorage;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import org.dragonet.common.data.itemsblocks.ItemEntry;
import org.dragonet.common.data.nbt.NBTIO;
import org.dragonet.common.data.nbt.tag.CompoundTag;
import org.dragonet.common.maths.ChunkPos;
import org.dragonet.protocol.type.chunk.ChunkData;
import org.dragonet.protocol.type.chunk.Section;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.FullChunkDataPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.CacheKey;

/**
 *
 * @author Epic
 */
public class ChunkCache {

    private final UpstreamSession session;
    private final Map<ChunkPos, Column> chunkCache = new HashMap();
    private final Set<ChunkPos> loadedChunks = new HashSet();

    private int chunkPerTick = 10;
    private final Queue<PEPacket> updateQueue = new ConcurrentLinkedQueue();

    public ChunkCache(UpstreamSession session) {
        this.session = session;
    }

    public void onTick() {
        //dequeue update blocks
        int counter = 0;
        while(!updateQueue.isEmpty() && counter <= chunkPerTick) {
            session.sendPacket(updateQueue.poll());
            counter++;
        }
    }

    public Map<ChunkPos, Column> getChunks() {
        return chunkCache;
    }

    public Set<ChunkPos> getLoadedChunks() {
        return loadedChunks;
    }

    public void clear() {
        chunkCache.clear();
    }

    public void update(Column column) {
        ChunkPos columnPos = new ChunkPos(column.getX(), column.getZ());
        chunkCache.put(columnPos, column);
//        System.out.println("ChunkCache add or update chunk " + column.getX() + ", " + column.getZ());
    }

    public void remove(int x, int z) {
        ChunkPos columnPos = new ChunkPos(x, z);
        if (chunkCache.containsKey(columnPos))
            chunkCache.remove(columnPos);
    }

    public void sendChunk(int x, int z, boolean force) {
        ChunkPos columnPos = new ChunkPos(x, z);
//        System.out.println("Try sending chunk " + x + ", " + z);
        if (!loadedChunks.contains(columnPos) || force)
            try {
                FullChunkDataPacket pePacket = new FullChunkDataPacket();
                pePacket.x = x;
                pePacket.z = z;
                ChunkData chunk = translateChunk(x, z);
                if (chunk != null) {
                    pePacket.payload = chunk.getBuffer();
                    updateQueue.add(pePacket);
                    loadedChunks.add(columnPos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void update(Position position, BlockState block) {
        ChunkPos columnPos = new ChunkPos(position.getX() >> 4, position.getZ() >> 4);
//        System.out.println("translateBlock Position " + position.toString());
        if (chunkCache.containsKey(columnPos)) {
            Column column = chunkCache.get(columnPos);
            BlockPosition blockPos = columnPos.getBlockInChunk(position.getX(), position.getY(), position.getZ());
            Chunk chunk = column.getChunks()[position.getY() >> 4];
            if (chunk != null)
                chunk.getBlocks().set(blockPos.x, blockPos.y, blockPos.z, block);
        }
        //enqueue block update
    }

    public final ItemEntry translateBlock(Position position) {
        ChunkPos columnPos = new ChunkPos(position.getX() >> 4, position.getZ() >> 4);
        if (chunkCache.containsKey(columnPos)) {
            Column column = chunkCache.get(columnPos);
            BlockPosition blockPos = columnPos.getBlockInChunk(position.getX(), position.getY(), position.getZ());
            Chunk chunk = column.getChunks()[position.getY() >> 4];
            if (chunk != null) {
                BlockState block = chunk.getBlocks().get(blockPos.x, blockPos.y, blockPos.z);
                return ItemBlockTranslator.translateToPE(block.getId(), block.getData());
            }
        }
        return null;
    }

    public final ItemStack getBlock(Position position) {
        ChunkPos columnPos = new ChunkPos(position.getX() >> 4, position.getZ() >> 4);
        if (chunkCache.containsKey(columnPos)) {
            Column column = chunkCache.get(columnPos);
            BlockPosition blockPos = columnPos.getBlockInChunk(position.getX(), position.getY(), position.getZ());
            Chunk chunk = column.getChunks()[position.getY() >> 4];
            if (chunk != null) {
                BlockState block = chunk.getBlocks().get(blockPos.x, blockPos.y, blockPos.z);
                return new ItemStack(block.getId(), 1, block.getData());
            }
        }
        return null;
    }

    public final void sendOrderedChunks() {
        if (!this.session.isLoggedIn())
            return;

        CachedEntity player = session.getEntityCache().getClientEntity();

        int centerX = (int) player.x >> 4;
        int centerZ = (int) player.z >> 4;

        int radius = player.spawned ? (int) session.getDataCache().getOrDefault(CacheKey.PLAYER_REQUESTED_CHUNK_RADIUS, 5) : (int) Math.ceil(Math.sqrt(56));
        int radiusSqr = radius * radius;

        for (int x = centerX - radius; x <= centerX + radius; x++) {
            int xx = x * x;
            for (int z = centerZ - radius; z <= centerZ + radius; z++) {
//                int distanceSqr = xx + z * z;
//                if (distanceSqr > radiusSqr)
//                    continue;

                sendChunk(x, z, false);
            }
        }
    }

    public final ChunkData translateChunk(int columnX, int columnZ) {
        ChunkPos columnPos = new ChunkPos(columnX, columnZ);
        if (chunkCache.containsKey(columnPos)) {
            Column column = chunkCache.get(columnPos);
            ChunkData chunk = new ChunkData();
            chunk.sections = new Section[16];
            for (int i = 0; i < 16; i++)
                chunk.sections[i] = new Section();

            // Blocks
            for (int y = 0; y < 256; y++) {
                int cy = y >> 4;

                Chunk c = null;
                try {
                    c = column.getChunks()[cy];
                } catch (Exception ex) {
                    DragonProxy.getInstance().getLogger().info("Chunk " + columnX + ", " + cy + ", " + columnZ + " not exist !");
                }
                if (c == null || c.isEmpty())
                    continue;
                BlockStorage blocks = c.getBlocks();
                for (int x = 0; x < 16; x++)
                    for (int z = 0; z < 16; z++) {
                        BlockState block = blocks.get(x, y & 0xF, z);
                        ItemEntry entry = ItemBlockTranslator.translateToPE(block.getId(), block.getData());

                        Section section = chunk.sections[cy];
                        //Block id
                        section.blockIds[index(x, y, z)] = (byte) (entry.getId() & 0xFF);

                        //Data value
                        int i = dataIndex(x, y, z);
                        byte data = section.blockMetas[i];
                        int newValue = entry.getPEDamage().byteValue();

                        if ((y & 1) == 0)
                            data = (byte) ((data & 0xf0) | (newValue & 0x0f));
                        else
                            data = (byte) (((newValue & 0x0f) << 4) | (data & 0x0f));

                        section.blockMetas[i] = data;
                    }
            }
            // Blocks entities
            try {
                List<CompoundTag> blockEntities = new ArrayList<>();
                for (int i = 0; i < column.getTileEntities().length; i++) {
                    CompoundTag peTag = ItemBlockTranslator.translateBlockEntityToPE(column.getTileEntities()[i]);
                    if (peTag != null) //filter non handled blocks entities
                        blockEntities.add(peTag);
//                else // debug
//                    DragonProxy.getInstance().getLogger().debug("NBT null for " + pc.getTileEntities()[i].toString());
                }
                chunk.blockEntities = NBTIO.write(blockEntities, ByteOrder.LITTLE_ENDIAN, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            chunk.encode();
            return chunk;
        }
//        else
//            System.out.println("Chunk " + columnX + ", " + columnZ + " not in cache !!!!!!!!!!!!!");
        return null;
    }

    private final int index(int x, int y, int z) {
        return (x << 8) | (z << 4) | (y & 0xF);
    }

    private final int dataIndex(int x, int y, int z) {
        return (x << 7) | (z << 3) | ((y & 0xF) >> 1);
    }
}
