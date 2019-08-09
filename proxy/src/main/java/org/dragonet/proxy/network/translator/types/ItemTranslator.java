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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.types.item.ItemEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        loadBedrockItems();
        loadJavaItems();
        addToArray();
    }

    private void loadBedrockItems() {
        log.info("Loading bedrock items");
        // Load bedrock items
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("runtime_item_ids.json");
        if (stream == null) {
            throw new AssertionError("Bedrock item Runtime ID table not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, ItemEntry.BedrockItem.class);

        ArrayList<ItemEntry.BedrockItem> entries = new ArrayList<>();
        try {
            entries = mapper.readValue(stream, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(ItemEntry.BedrockItem item : entries) {
            BEDROCK_ITEMS.put(item.getIdentifier(), item);
        }
    }

    private void loadJavaItems() {
        log.info("Loading java items");
        // Load java items
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("java_items.json");
        if (stream == null) {
            throw new AssertionError("Java item Runtime ID table not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, ItemEntry.JavaItem.class);

        ArrayList<ItemEntry.JavaItem> entries = new ArrayList<>();
        try {
            entries = mapper.readValue(stream,  type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(ItemEntry.JavaItem item : entries) {
            JAVA_ITEMS.put(item.getIdentifier(), item);
        }
    }

    private void addToArray() {
        Map<ItemEntry.JavaItem, List<ItemEntry.BedrockItem>> conversions = new HashMap<>();

        JsonArray array = new JsonArray();

        for(Map.Entry<String, ItemEntry.JavaItem> javaItem : JAVA_ITEMS.entrySet()) {
            for(Map.Entry<String, ItemEntry.BedrockItem> bedrockItem : BEDROCK_ITEMS.entrySet()) {
                if(bedrockItem.getValue().getIdentifier().equalsIgnoreCase(javaItem.getKey())) {
                    ItemEntry.JavaItem item = javaItem.getValue();
                    conversions.computeIfAbsent(item, (x) -> new ArrayList<>());
                    conversions.get(item).add(bedrockItem.getValue());
                }
            }
        }

        for(Map.Entry<ItemEntry.JavaItem, List<ItemEntry.BedrockItem>> entry : conversions.entrySet()) {
            for(ItemEntry.BedrockItem item : entry.getValue()) {
                JAVA_TO_BEDROCK_MAP.computeIfAbsent(entry.getKey().getIdentifier(), (x) -> new HashMap<>());
                BEDROCK_TO_JAVA_MAP.computeIfAbsent(item.getIdentifier(), (x) -> new HashMap<>());
                Map<String, Object> map = JAVA_TO_BEDROCK_MAP.get(entry.getKey().getIdentifier());

                map.put("name", item.getIdentifier());
                map.put("id", item.getIdentifier());
                map.put("data", 0);

                // This is what we will use in the future
                JsonObject object = new JsonObject();
                object.addProperty("java_identifier", entry.getKey().getIdentifier());
                object.addProperty("java_protocol_id", entry.getKey().getRuntimeId());
                object.addProperty("bedrock_identifier", item.getIdentifier());
                object.addProperty("bedrock_runtime_id", item.getRuntimeId());
                object.addProperty("bedrock_data", 0); // TODO
                array.add(object);

                BEDROCK_TO_JAVA_MAP.get(item.getIdentifier()).put(0, entry.getKey().getIdentifier());
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DragonProxy.INSTANCE.getFolder().toFile().getAbsolutePath() + "/item_mappings.json"));
            writer.write(array.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
            if(item.getNBT() == null) {
                return ItemData.of(bedrockItem.getRuntimeId(), (short) 0, item.getAmount());
            }

            return ItemData.of(bedrockItem.getRuntimeId(), (short) 0, item.getAmount(), translateItemNBT(item.getNBT()));
        }
        return ItemData.AIR;
    }

    private static String getBedrockIdentifier(String javaIdentifier) {
        if (!JAVA_TO_BEDROCK_MAP.containsKey(javaIdentifier)) {
            return javaIdentifier;
        }
        return (String) JAVA_TO_BEDROCK_MAP.get(javaIdentifier).get("id");
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
