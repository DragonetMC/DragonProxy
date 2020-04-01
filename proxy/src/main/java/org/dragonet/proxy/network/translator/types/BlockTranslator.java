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
package org.dragonet.proxy.network.translator.types;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.nbt.tag.ListTag;
import com.nukkitx.protocol.bedrock.data.ItemData;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.types.item.ItemEntry;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class BlockTranslator {
    public static final ListTag<CompoundTag> BLOCK_PALETTE;

    private static final Map<Integer, Integer> java2BedrockMap = new HashMap<>();
    private static final Map<Integer, BlockState> bedrock2JavaMap = new HashMap<>();

    private static final AtomicInteger bedrockIdAllocator = new AtomicInteger();
    private static final AtomicInteger javaIdAllocator = new AtomicInteger();

    private static final int BLOCK_STATE_VERSION = 17760256;
    public static HashMap<Integer, String> BEDROCK_TEMP = new HashMap<>();

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

        stream = DragonProxy.class.getClassLoader().getResourceAsStream("block_mappings.json");
        if(stream == null) {
            throw new AssertionError("Block mapping table not found");
        }

        JsonNode blocks;
        try {
            blocks = DragonProxy.JSON_MAPPER.readTree(stream);
        } catch (Exception e) {
            throw new AssertionError("Unable to load Java block mappings", e);
        }
        Object2IntMap<CompoundTag> addedStatesMap = new Object2IntOpenHashMap<>();
        addedStatesMap.defaultReturnValue(-1);
        List<CompoundTag> paletteList = new ArrayList<>();

        // I broke my old code, so here's some code borrowed from Geyser until i get the time
        // to change it, sorry guys
        // https://github.com/GeyserMC/Geyser
        Iterator<Map.Entry<String, JsonNode>> blocksIterator = blocks.fields();
        while (blocksIterator.hasNext()) {
            int javaProtocolId = javaIdAllocator.getAndIncrement();
            int bedrockRuntimeId = bedrockIdAllocator.get();

            Map.Entry<String, JsonNode> entry = blocksIterator.next();
            CompoundTag blockTag = buildBedrockState(entry.getValue());

            bedrock2JavaMap.putIfAbsent(bedrockRuntimeId, new BlockState(javaProtocolId));
            BEDROCK_TEMP.putIfAbsent(bedrockRuntimeId, entry.getValue().get("bedrock_identifier").textValue());

            CompoundTag runtimeTag = blockStateMap.remove(blockTag);
            if (runtimeTag != null) {
                addedStatesMap.put(blockTag, bedrockRuntimeId);
                paletteList.add(runtimeTag);
            } else {
                int duplicateRuntimeId = addedStatesMap.get(blockTag);
                if (duplicateRuntimeId == -1) {
                    log.warn("Mapping " + entry.getKey() + " was not found for bedrock edition!");
                } else {
                    java2BedrockMap.put(javaProtocolId, duplicateRuntimeId);
                }
                continue;
            }
            java2BedrockMap.put(javaProtocolId, bedrockRuntimeId);
            bedrockIdAllocator.incrementAndGet();
        }

        paletteList.addAll(blockStateMap.values()); // Add any missing mappings that could crash the client

        //log.warn(paletteList);

        BLOCK_PALETTE = new ListTag<>("", CompoundTag.class, paletteList);
    }

    public BlockTranslator() {
    }

    private static CompoundTag buildBedrockState(JsonNode node) {
        CompoundTagBuilder tagBuilder = CompoundTag.builder();
        tagBuilder.stringTag("name", node.get("bedrock_identifier").textValue())
            .intTag("version", BlockTranslator.BLOCK_STATE_VERSION);

        CompoundTagBuilder statesBuilder = CompoundTag.builder();

        if (node.has("bedrock_states")) {
            Iterator<Map.Entry<String, JsonNode>> statesIterator = node.get("bedrock_states").fields();

            while (statesIterator.hasNext()) {
                Map.Entry<String, JsonNode> stateEntry = statesIterator.next();
                JsonNode stateValue = stateEntry.getValue();
                switch (stateValue.getNodeType()) {
                    case BOOLEAN:
                        statesBuilder.booleanTag(stateEntry.getKey(), stateValue.booleanValue());
                        continue;
                    case STRING:
                        statesBuilder.stringTag(stateEntry.getKey(), stateValue.textValue());
                        continue;
                    case NUMBER:
                        statesBuilder.intTag(stateEntry.getKey(), stateValue.intValue());
                }
            }
        }
        return tagBuilder.tag(statesBuilder.build("states")).build("block");
    }

    public static int translateToBedrock(BlockState state) {
        //return ItemData.of(bedrockItem.getRuntimeId(), (short) getBedrockData(javaItem.getIdentifier()), item.getAmount());
        return java2BedrockMap.get(state.getId());
    }

    public static BlockState translateToJava(int bedrockId) {
        return bedrock2JavaMap.get(bedrockId);
    }


//    {
//        stream = DragonProxy.class.getClassLoader().getResourceAsStream("block_mappings.json");
//        if (stream == null) {
//            throw new AssertionError("Block mapping table not found");
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, ItemEntry.ItemMap.class);
//
//        ArrayList<ItemEntry.ItemMap> entries = new ArrayList<>();
//        try {
//            entries = mapper.readValue(stream,  type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        for(ItemEntry.ItemMap entry : entries) {
//            JAVA_BLOCKS.put(entry.getJavaIdentifier(), new ItemEntry.JavaItem(entry.getJavaIdentifier(), entry.getJavaProtocolId()));
//            BEDROCK_BLOCKS.put(entry.getBedrockIdentifier(), new ItemEntry.BedrockItem(entry.getBedrockIdentifier(), entry.getBedrockRuntimeId(), entry.getBedrockData()));
//
//            JAVA_TO_BEDROCK_MAP.computeIfAbsent(entry.getJavaIdentifier(), (x) -> new HashMap<>());
//            BEDROCK_TO_JAVA_MAP.computeIfAbsent(entry.getBedrockIdentifier(), (x) -> new HashMap<>());
//
//            // Better solution for these maps
//            Map<String, Object> map = JAVA_TO_BEDROCK_MAP.get(entry.getJavaIdentifier());
//
//            map.put("name", entry.getBedrockIdentifier());
//            map.put("id", entry.getBedrockRuntimeId());
//            map.put("data", entry.getBedrockData());
//
//            BEDROCK_TO_JAVA_MAP.get(entry.getBedrockIdentifier()).put(0, entry.getJavaIdentifier());
//        }
//    }
//
//    public static ItemData translateToBedrock(ItemStack item) {
//        for(Map.Entry<String, ItemEntry.JavaItem> javaItems : JAVA_BLOCKS.entrySet()) {
//            if(javaItems.getValue().getRuntimeId() != item.getId()){
//                continue;
//            }
//            ItemEntry.JavaItem javaItem = javaItems.getValue();
//            String identifier = getBedrockIdentifier(javaItem.getIdentifier());
//
//            if(!BEDROCK_BLOCKS.containsKey(identifier)) {
//                continue;
//            }
//            ItemEntry.BedrockItem bedrockItem = BEDROCK_BLOCKS.get(identifier);
//
//            return ItemData.of(bedrockItem.getRuntimeId(), (short) getBedrockData(javaItem.getIdentifier()), item.getAmount());
//        }
//
//        return ItemData.of(BEDROCK_BLOCKS.get("minecraft:bedrock").getRuntimeId(), (short) 0, item.getAmount());
//    }
//
//    private static String getBedrockIdentifier(String javaIdentifier) {
//        if (!JAVA_TO_BEDROCK_MAP.containsKey(javaIdentifier)) {
//            return javaIdentifier;
//        }
//        return (String) JAVA_TO_BEDROCK_MAP.get(javaIdentifier).get("name");
//    }
//
//    private static int getBedrockData(String javaIdentifier) {
//        return (int) JAVA_TO_BEDROCK_MAP.get(javaIdentifier).get("data");
//    }
}
