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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.types.item.ItemEntry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class BlockTranslator {
    public static final Map<String, ItemEntry.BedrockItem> BEDROCK_BLOCKS = new HashMap<>();
    public static final Map<String, ItemEntry.JavaItem> JAVA_BLOCKS = new HashMap<>();

    public static Map<String, Map<Integer, String>> BEDROCK_TO_JAVA_MAP = new HashMap<>();
    public static Map<String, Map<String, Object>> JAVA_TO_BEDROCK_MAP = new HashMap<>();

    public BlockTranslator() {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("block_mappings.json");
        if (stream == null) {
            throw new AssertionError("Block mapping table not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, ItemEntry.ItemMap.class);

        ArrayList<ItemEntry.ItemMap> entries = new ArrayList<>();
        try {
            entries = mapper.readValue(stream,  type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(ItemEntry.ItemMap entry : entries) {
            JAVA_BLOCKS.put(entry.getJavaIdentifier(), new ItemEntry.JavaItem(entry.getJavaIdentifier(), entry.getJavaProtocolId()));
            BEDROCK_BLOCKS.put(entry.getBedrockIdentifier(), new ItemEntry.BedrockItem(entry.getBedrockIdentifier(), entry.getBedrockRuntimeId(), entry.getBedrockData()));

            JAVA_TO_BEDROCK_MAP.computeIfAbsent(entry.getJavaIdentifier(), (x) -> new HashMap<>());
            BEDROCK_TO_JAVA_MAP.computeIfAbsent(entry.getBedrockIdentifier(), (x) -> new HashMap<>());

            // Better solution for these maps
            Map<String, Object> map = JAVA_TO_BEDROCK_MAP.get(entry.getJavaIdentifier());

            map.put("name", entry.getBedrockIdentifier());
            map.put("id", entry.getBedrockRuntimeId());
            map.put("data", entry.getBedrockData());

            BEDROCK_TO_JAVA_MAP.get(entry.getBedrockIdentifier()).put(0, entry.getJavaIdentifier());
        }
    }

    public static ItemData translateToBedrock(ItemStack item) {
        for(Map.Entry<String, ItemEntry.JavaItem> javaItems : JAVA_BLOCKS.entrySet()) {
            if(javaItems.getValue().getRuntimeId() != item.getId()){
                continue;
            }
            ItemEntry.JavaItem javaItem = javaItems.getValue();
            String identifier = getBedrockIdentifier(javaItem.getIdentifier());

            if(!BEDROCK_BLOCKS.containsKey(identifier)) {
                continue;
            }
            ItemEntry.BedrockItem bedrockItem = BEDROCK_BLOCKS.get(identifier);

            return ItemData.of(bedrockItem.getRuntimeId(), (short) getBedrockData(javaItem.getIdentifier()), item.getAmount());
        }

        return ItemData.of(BEDROCK_BLOCKS.get("minecraft:bedrock").getRuntimeId(), (short) 0, item.getAmount());
    }

    private static String getBedrockIdentifier(String javaIdentifier) {
        if (!JAVA_TO_BEDROCK_MAP.containsKey(javaIdentifier)) {
            return javaIdentifier;
        }
        return (String) JAVA_TO_BEDROCK_MAP.get(javaIdentifier).get("name");
    }

    private static int getBedrockData(String javaIdentifier) {
        return (int) JAVA_TO_BEDROCK_MAP.get(javaIdentifier).get("data");
    }
}
