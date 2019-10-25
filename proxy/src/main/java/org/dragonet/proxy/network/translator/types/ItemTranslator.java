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
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.opennbt.tag.builtin.*;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.types.item.ItemEntry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class ItemTranslator {
    public static final Map<String, ItemEntry.BedrockItem> BEDROCK_ITEMS = new HashMap<>();
    public static final Map<String, ItemEntry.JavaItem> JAVA_ITEMS = new HashMap<>();

    public static Map<String, Map<Integer, String>> BEDROCK_TO_JAVA_MAP = new HashMap<>();
    public static Map<String, Map<String, Object>> JAVA_TO_BEDROCK_MAP = new HashMap<>();

    public ItemTranslator() {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("item_mappings.json");
        if (stream == null) {
            throw new AssertionError("Item mapping table not found");
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
            JAVA_ITEMS.put(entry.getJavaIdentifier(), new ItemEntry.JavaItem(entry.getJavaIdentifier(), entry.getJavaProtocolId()));
            BEDROCK_ITEMS.put(entry.getBedrockIdentifier(), new ItemEntry.BedrockItem(entry.getBedrockIdentifier(), entry.getBedrockRuntimeId(), entry.getBedrockData()));

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
        for(Map.Entry<String, ItemEntry.JavaItem> javaItems : JAVA_ITEMS.entrySet()) {
            if(javaItems.getValue().getRuntimeId() != item.getId()){
                continue;
            }
            ItemEntry.JavaItem javaItem = javaItems.getValue();
            String identifier = getBedrockIdentifier(javaItem.getIdentifier());

            if(!BEDROCK_ITEMS.containsKey(identifier)) {
                continue;
            }
            ItemEntry.BedrockItem bedrockItem = BEDROCK_ITEMS.get(identifier);

            if(item.getNbt() == null) {
                return ItemData.of(bedrockItem.getRuntimeId(), (short) getBedrockData(javaItem.getIdentifier()), item.getAmount());
            }

            return ItemData.of(bedrockItem.getRuntimeId(), (short) getBedrockData(javaItem.getIdentifier()), item.getAmount(), translateItemNBT(item.getNbt()));
        }
        return ItemData.AIR;
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

    public static ItemData translateSlotToBedrock(ItemStack item) {
        if(item == null || item.getId() == 0) {
            return ItemData.AIR;
        }

        ItemData data = translateToBedrock(item);
        return data;
    }

    public static com.nukkitx.nbt.tag.CompoundTag translateItemNBT(CompoundTag tag) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();

        if(!tag.contains("display")) {
            CompoundTagBuilder display = CompoundTagBuilder.builder();

            if (tag.contains("name")) {
                display.stringTag("Name", Message.fromString((String) tag.get("name").getValue()).getFullText());
                tag.remove("name");
            }
            if (tag.contains("lore")) {
                com.nukkitx.nbt.tag.ListTag list = (com.nukkitx.nbt.tag.ListTag) translateRawNBT(tag.get("lore"));
                display.listTag("Lore", com.nukkitx.nbt.tag.StringTag.class, list.getValue()); // TODO: fix unchecked assignment
                tag.remove("lore");
            }
            root.tag(display.build("display"));
        }

        if(tag.getValue() != null && !tag.getValue().isEmpty()) {
            for(String tagName : tag.getValue().keySet()) {
                com.nukkitx.nbt.tag.Tag bedrockTag = translateRawNBT(tag.get(tagName));
                if(bedrockTag == null) {
                    continue;
                }
                root.tag(bedrockTag);
            }
        }
        return root.buildRootTag();
    }

    public static com.nukkitx.nbt.tag.Tag translateRawNBT(Tag tag) {
        if(tag instanceof ByteArrayTag) {
            return new com.nukkitx.nbt.tag.ByteArrayTag(tag.getName(), (byte[]) tag.getValue());
        }
        if(tag instanceof StringTag) {
            return new com.nukkitx.nbt.tag.StringTag(tag.getName(), MessageTranslator.translate(((String) tag.getValue())));
        }
        if(tag instanceof ListTag) {
            ListTag listTag = (ListTag) tag;
            List<com.nukkitx.nbt.tag.Tag> tags = new ArrayList<>();

            for(Object value : listTag.getValue()) {
                if(!(value instanceof Tag)) {
                    continue;
                }
                Tag tagValue = (Tag) value;
                com.nukkitx.nbt.tag.Tag bedrockTag = translateRawNBT(tagValue);
                if(bedrockTag != null) {
                    tags.add(bedrockTag);
                }
            }
            // TODO: unchecked?
            // TODO: map element type from Java NBT tag to NukkitX tag
            return new com.nukkitx.nbt.tag.ListTag(listTag.getName(), com.nukkitx.nbt.tag.StringTag.class, tags);
        }
        if(tag instanceof CompoundTag) {
            CompoundTag compound = (CompoundTag) tag;
            CompoundTagBuilder builder = CompoundTagBuilder.builder();

            if(compound.getValue() != null && !compound.getValue().isEmpty()) {
                for(String tagName : compound.getValue().keySet()) {
                    com.nukkitx.nbt.tag.Tag bedrockTag = translateRawNBT(compound.get(tagName));
                    if(bedrockTag == null) {
                        continue;
                    }
                    builder.tag(bedrockTag);
                }
                return builder.build(tag.getName());
            }
        }

        return null;
    }
}
