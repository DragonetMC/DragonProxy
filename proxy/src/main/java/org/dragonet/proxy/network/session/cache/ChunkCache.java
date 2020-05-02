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
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import jdk.nashorn.internal.ir.Block;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.chunk.ChunkData;
import org.dragonet.proxy.data.chunk.ChunkSection;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.BlockEntityTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.BlockTranslator;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ChunkCache implements Cache {
    @Getter
    private Object2ObjectMap<Vector2i, Column> javaChunks = new Object2ObjectOpenHashMap<>();

    /**
     * Translates a chunk from Java Edition to Bedrock Edition.
     */
    public ChunkData translateChunk(ProxySession session, int columnX, int columnZ) {
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
                int x = Integer.parseInt(column.getTileEntities()[i].get("x").getValue().toString());
                int y = Integer.parseInt(column.getTileEntities()[i].get("y").getValue().toString());
                int z = Integer.parseInt(column.getTileEntities()[i].get("z").getValue().toString());
                BlockState block = getJavaBlockAt(Vector3i.from(x, y, z));
                CompoundTag tag = BlockEntityTranslatorRegistry.translateToBedrock(column.getTileEntities()[i], block);
                if(tag != null) {
                    bedrockBlockEntities.add(tag);
                }
            }
            chunkData.blockEntities = bedrockBlockEntities;
            return chunkData;
        }
        return null;
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

    public BlockState getJavaBlockAt(Vector3i position) {
        Vector2i chunkPosition = Vector2i.from(position.getX() >> 4, position.getZ() >> 4);
        if(!javaChunks.containsKey(chunkPosition)) {
            return new BlockState(0); // Air
        }
        Column column = javaChunks.get(chunkPosition);
        Chunk chunk = column.getChunks()[position.getY() >> 4];
        Vector3i blockPosition = Vector3i.from(position.getX() & 15, position.getY() & 15, position.getZ() & 15);

        if(chunk != null) {
            return chunk.get(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        }
        return new BlockState(0);
    }

    public void updateBlock(ProxySession session, Vector3i position, BlockState state) {
        Vector2i chunkPosition = Vector2i.from(position.getX() >> 4, position.getZ() >> 4);
        Vector3i blockPosition = Vector3i.from(position.getX() & 15, position.getY() & 15, position.getZ() & 15);
        Column column = javaChunks.get(chunkPosition);
        column.getChunks()[position.getY() >> 4].set(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), state);
        javaChunks.put(chunkPosition, column);

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
    }
}
