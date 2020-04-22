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
package org.dragonet.proxy.network.session.cache;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTOutputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.data.chunk.ChunkData;
import org.dragonet.proxy.data.chunk.ChunkSection;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.misc.BlockEntityTranslator;
import org.dragonet.proxy.network.translator.misc.BlockTranslator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.dragonet.proxy.network.translator.java.PCJoinGameTranslator.EMPTY_LEVEL_CHUNK_DATA;

@Log4j2
public class ChunkCache implements Cache {

    @Getter
    private Object2ObjectMap<Vector2i, Column> javaChunks = new Object2ObjectOpenHashMap<>();

    private final ObjectSet<Vector2i> loadedChunks = new ObjectOpenHashSet<>();
    private final Queue<LevelChunkPacket> updateQueue = new ConcurrentLinkedQueue<>();

    private int chunkPerTick;

    public ChunkCache() {
        chunkPerTick = DragonProxy.INSTANCE.getConfiguration().getChunksPerTick();
    }

    /**
     * Translates a chunk from Java Edition to Bedrock Edition.
     */
    public ChunkData translateChunk(int columnX, int columnZ) {
        Vector2i columnPos = Vector2i.from(columnX, columnZ);

        if (javaChunks.containsKey(columnPos)) {
            Column column = javaChunks.get(columnPos);
            ChunkData chunkData = new ChunkData(columnX, columnZ);

            int chunkSectionCount = column.getChunks().length;
            chunkData.sections = new ChunkSection[chunkSectionCount];

            for (int chunkY = 0; chunkY < chunkSectionCount; chunkY++) {
                chunkData.sections[chunkY] = new ChunkSection();
                Chunk javaChunk = column.getChunks()[chunkY];

                if (javaChunk == null || javaChunk.isEmpty()) continue;
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            BlockState block = javaChunk.get(x, y, z);
                            int bedrockId = BlockTranslator.translateToBedrock(block);

                            ChunkSection section = chunkData.sections[chunkY];
                            section.setFullBlock(x, y, z, 0, bedrockId);

                            if(BlockTranslator.isWaterlogged(block)) {
                                section.setFullBlock(x, y, z, 1, BlockTranslator.BEDROCK_WATER_ID);
                            }
                        }
                    }
                }
            }

            List<CompoundTag> bedrockBlockEntities = new ArrayList<>();
            for(int i = 0; i < column.getTileEntities().length; i++) {
                CompoundTag tag = BlockEntityTranslator.translateToBedrock(column.getTileEntities()[i]);
                if(tag != null) {
                    bedrockBlockEntities.add(tag);
                }
            }
            chunkData.blockEntities = bedrockBlockEntities;
            return chunkData;
        }
        return null;
    }

    public void onTick(ProxySession session) {
        int counter = 0;
        while (!updateQueue.isEmpty() && counter <= chunkPerTick) {
            session.sendPacket(updateQueue.poll());
            counter++;
        }
    }

    public void unloadChunk(Vector2i position) {
        javaChunks.remove(position);
        loadedChunks.remove(position);
    }

    public void sendChunk(ProxySession session, int x, int z, boolean force) {
        Vector2i columnPos = Vector2i.from(x, z);

        if (!loadedChunks.contains(columnPos) || force) {
            NetworkChunkPublisherUpdatePacket chunkPublisherUpdatePacket = new NetworkChunkPublisherUpdatePacket();
            chunkPublisherUpdatePacket.setPosition(session.getCachedEntity().getPosition().toInt());
            chunkPublisherUpdatePacket.setRadius(8 << 4);

            session.sendPacket(chunkPublisherUpdatePacket);

            ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer();
            try {
                ChunkData chunkData = translateChunk(x, z);
                if(chunkData != null) {

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

                    buffer.writeBytes(stream.buffer());

                    byte[] payload = new byte[buffer.readableBytes()];
                    buffer.readBytes(payload);

                    LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
                    levelChunkPacket.setChunkX(x);
                    levelChunkPacket.setChunkZ(z);
                    levelChunkPacket.setCachingEnabled(false);
                    levelChunkPacket.setSubChunksLength(sectionCount);
                    levelChunkPacket.setData(payload);

                    updateQueue.add(levelChunkPacket);
                    loadedChunks.add(columnPos);
                } else {
                    //log.warn("chunk data is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                buffer.release();
            }
        }
    }

    public void sendEmptyChunk(int x, int z) {
        LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
        levelChunkPacket.setChunkX(x);
        levelChunkPacket.setChunkZ(z);
        levelChunkPacket.setSubChunksLength(0);
        levelChunkPacket.setData(EMPTY_LEVEL_CHUNK_DATA);
        levelChunkPacket.setCachingEnabled(false);

        updateQueue.add(levelChunkPacket);
    }

    public void sendEmptyChunks(ProxySession session, int radius) {
        CachedPlayer player = session.getCachedEntity();
        int centerX = player.getPosition().getFloorX() >> 4;
        int centerZ = player.getPosition().getFloorZ() >> 4;

        for (int x = -radius; x < radius; x++)
            for (int z = -radius; z < radius; z++)
                sendEmptyChunk(centerX + x, centerZ + z);
    }

    public void sendOrderedChunks(ProxySession session) {
        CachedPlayer player = session.getCachedEntity();

        int centerX = player.getPosition().getFloorX() >> 4;
        int centerZ = player.getPosition().getFloorZ() >> 4;

        int radius = (int) Math.ceil(Math.sqrt(56));

        Set<Vector2i> toLoad = new HashSet<>();

        for (int x = centerX - radius; x <= centerX + radius; x++)
            for (int z = centerZ - radius; z <= centerZ + radius; z++)
                toLoad.add(Vector2i.from(x, z));

        for (Vector2i chunk : toLoad)
            sendChunk(session, chunk.getX(), chunk.getY(), false);

        Set<Vector2i> toUnLoad = new HashSet<>(loadedChunks);
        toUnLoad.removeAll(toLoad);

        for (Vector2i chunk : toUnLoad)
            sendEmptyChunk(chunk.getX(), chunk.getY());

        loadedChunks.removeAll(toUnLoad);
    }

    public int getBlockAt(Vector3i position) {
        Vector2i chunkPosition = Vector2i.from(position.getX() >> 4, position.getZ() >> 4);
        if(!javaChunks.containsKey(chunkPosition)) {
            return 0; // Air
        }
        Column column = javaChunks.get(chunkPosition);
        Chunk chunk = column.getChunks()[position.getY() >> 4];
        Vector3i blockPosition = Vector3i.from(position.getX() & 15, position.getY() & 15, position.getZ() & 15);

        if(chunk != null) {
            return BlockTranslator.translateToBedrock(chunk.get(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()));
        }
        return 0;
    }

    public void updateBlock(ProxySession session, Vector3i position, BlockState state) {
        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
        updateBlockPacket.setBlockPosition(position);
        updateBlockPacket.setDataLayer(0);
        updateBlockPacket.setRuntimeId(BlockTranslator.translateToBedrock(state));
        updateBlockPacket.getFlags().add(UpdateBlockPacket.Flag.NEIGHBORS);

        session.sendPacket(updateBlockPacket);
    }

    public void sendFakeBlock(ProxySession session, String identifier, Vector3i position) {
        sendFakeBlock(session, BlockTranslator.bedrockIdToRuntime(identifier), position);
    }

    public void sendFakeBlock(ProxySession session, int runtimeId, Vector3i position) {
        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
        updateBlockPacket.setBlockPosition(position);
        updateBlockPacket.setDataLayer(0);
        updateBlockPacket.setRuntimeId(runtimeId);
        updateBlockPacket.getFlags().add(UpdateBlockPacket.Flag.PRIORITY);

        session.sendPacket(updateBlockPacket);
    }

    @Override
    public void purge() {
        javaChunks.clear();
        loadedChunks.clear();
        updateQueue.clear();
    }
}
