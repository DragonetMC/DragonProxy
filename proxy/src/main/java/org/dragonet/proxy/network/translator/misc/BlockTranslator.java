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
package org.dragonet.proxy.network.translator.misc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.nbt.tag.ListTag;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class BlockTranslator {
    // The block pallete for the StartGamePacket
    public static final ListTag<CompoundTag> BLOCK_PALETTE;

    // Constants
    private static final int BLOCK_STATE_VERSION = 17760256;
    // TODO: i don't like hardcoding these ids, but its fine for now
    public static final int BEDROCK_WATER_ID = 32;
    public static final int BEDROCK_COMMAND_BLOCK_ID = 1049;
    public static final int BEDROCK_PISTON_ID = 234;
    public static final int BEDROCK_STICKY_PISTON_ID = 221;

    // Java to bedrock map
    private static final Int2IntMap java2BedrockMap = new Int2IntOpenHashMap();
    // Bedrock to java map
    private static final Int2ObjectMap<BlockState> bedrock2JavaMap = new Int2ObjectOpenHashMap<>();

    // Waterlogged blocks
    private static final Set<Integer> waterlogged = new ObjectOpenHashSet<>();

    // Unique ID allocators
    private static final AtomicInteger bedrockIdAllocator = new AtomicInteger();
    private static final AtomicInteger javaIdAllocator = new AtomicInteger();

    private static Object2IntMap<String> bedrockId2RuntimeMap = new Object2IntOpenHashMap<>();
    private static Int2ObjectMap<String> bedrockRuntime2IdMap = new Int2ObjectOpenHashMap<>();
    @Getter
    private static Int2IntMap beds = new Int2IntOpenHashMap();

    @Getter
    private static int waterRuntimeId;

    static {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("data/runtime_block_states.dat");
        if(stream == null) {
            throw new AssertionError("Static runtime block state table not found");
        }

        ListTag<CompoundTag> blocksTag;
        try (NBTInputStream nbtInputStream = NbtUtils.createNetworkReader(stream)) {
            // noinspection unchecked
            blocksTag = (ListTag<CompoundTag>) nbtInputStream.readTag();
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        Map<CompoundTag, CompoundTag> blockStateMap = new HashMap<>();

        for (CompoundTag tag : blocksTag.getValue()) {
            if (blockStateMap.putIfAbsent(tag.getCompound("block"), tag) != null) {
                throw new AssertionError("Duplicate block states in Bedrock palette");
            }
        }

        stream = DragonProxy.class.getClassLoader().getResourceAsStream("mappings/1.15/block_mappings.json");
        if(stream == null) {
            throw new AssertionError("Block mapping table not found");
        }

        Map<String, BlockMappingEntry> blockEntries;
        try {
             blockEntries = DragonProxy.JSON_MAPPER.readValue(stream, new TypeReference<Map<String, BlockMappingEntry>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Object2IntMap<CompoundTag> addedStatesMap = new Object2IntOpenHashMap<>();
        addedStatesMap.defaultReturnValue(-1);
        List<CompoundTag> paletteList = new ArrayList<>();

        // I broke my old code, so here's some code borrowed from Geyser until i get the time
        // to change it, sorry guys
        // https://github.com/GeyserMC/Geyser
        blockEntries.forEach((javaIdentifier, blockMappingEntry) -> {
            int javaProtocolId = javaIdAllocator.getAndIncrement();
            int bedrockRuntimeId = bedrockIdAllocator.get();

            String bedrockIdentifier = blockMappingEntry.getBedrockIdentifier();
            CompoundTag blockTag = buildBedrockState(bedrockIdentifier, blockMappingEntry.getBedrockStates());

            if(blockMappingEntry.isWaterlogged()) {
                waterlogged.add(javaProtocolId);
            }

            if(blockMappingEntry.getBedColor() != null && javaIdentifier.contains("_bed")) {
                beds.put(bedrockRuntimeId, blockMappingEntry.getBedColor().byteValue());
            }

            bedrock2JavaMap.putIfAbsent(bedrockRuntimeId, new BlockState(javaProtocolId));

            CompoundTag runtimeTag = blockStateMap.remove(blockTag);
            if(runtimeTag != null) {
                addedStatesMap.put(blockTag, bedrockRuntimeId);
                paletteList.add(runtimeTag);
            } else {
                int duplicateRuntimeId = addedStatesMap.get(blockTag);
                if (duplicateRuntimeId == -1) {
                    log.warn("Mapping " + javaIdentifier + " was not found for bedrock edition!");
                } else {
                    java2BedrockMap.put(javaProtocolId, duplicateRuntimeId);
                }
                return; // continue
            }

            bedrockId2RuntimeMap.put(bedrockIdentifier, bedrockRuntimeId);
            bedrockRuntime2IdMap.put(bedrockRuntimeId, bedrockIdentifier);
            java2BedrockMap.put(javaProtocolId, bedrockRuntimeId);

            bedrockIdAllocator.incrementAndGet();
        });

        paletteList.addAll(blockStateMap.values()); // Add any missing mappings that could crash the client

        BLOCK_PALETTE = new ListTag<>("", CompoundTag.class, paletteList);
    }

    public static CompoundTag buildBedrockState(String identifier, List<BlockStateEntry> states) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();
        root.stringTag("name", identifier);
        root.intTag("version", BLOCK_STATE_VERSION);

        CompoundTagBuilder statesTag = CompoundTagBuilder.builder();

        if(!states.isEmpty()) {
            for(BlockStateEntry state : states) {
                if(state.getValue() instanceof Boolean) {
                    statesTag.booleanTag(state.getName(), (boolean) state.getValue());
                }
                else if(state.getValue() instanceof Integer) {
                    statesTag.intTag(state.getName(), (int) state.getValue());
                }
                else if(state.getValue() instanceof String) {
                    statesTag.stringTag(state.getName(), state.getValue().toString());
                }
            }
        }
        return root.tag(statesTag.build("states")).build("block");
    }

    public static int translateToBedrock(BlockState state) {
        //return ItemData.of(bedrockItem.getRuntimeId(), (short) getBedrockData(javaItem.getIdentifier()), item.getAmount());
        return java2BedrockMap.get(state.getId());
    }

    public static BlockState translateToJava(int bedrockId) {
        return bedrock2JavaMap.get(bedrockId);
    }

    public static Integer bedrockIdToRuntime(String id) {
        return bedrockId2RuntimeMap.get(id);
    }

    public static String bedrockRuntimeToId(int runtimeId) {
        return bedrockRuntime2IdMap.get(runtimeId);
    }

    public static boolean isWaterlogged(BlockState state) {
        return waterlogged.contains(state.getId());
    }

    @Getter
    private static class BlockMappingEntry {
        @JsonProperty("bedrock_identifier")
        private String bedrockIdentifier;

        @JsonProperty("block_hardness")
        private double hardness;
        private boolean waterlogged;
        private List<BlockStateEntry> bedrockStates = new ArrayList<>();

        @JsonProperty("bed_color")
        private Integer bedColor;

        @JsonProperty("bedrock_states")
        private void loadBedrockStates(Map<String, Object> map) {
            map.forEach((key, value) -> bedrockStates.add(new BlockStateEntry(key, value)));
        }
    }

    @Getter
    @AllArgsConstructor
    private static class BlockStateEntry {
        private String name;
        private Object value;
    }
}
