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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.opennbt.tag.builtin.*;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.types.item.ItemEntry;
import org.dragonet.proxy.util.PaletteManager;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class ItemTranslator {
    public static final List<StartGamePacket.ItemEntry> ITEM_PALETTE = new ArrayList<>();
    public static final Int2ObjectMap<ItemEntry> ITEM_ENTRIES = new Int2ObjectOpenHashMap<>();

    private static final AtomicInteger javaIdAllocator = new AtomicInteger(0);

    static {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("data/runtime_item_states.json");
        if (stream == null) {
            throw new AssertionError("Static item state table not found");
        }

        ArrayList<PaletteManager.RuntimeEntry> entries;
        CollectionType type = DragonProxy.JSON_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, PaletteManager.RuntimeEntry.class);
        try {
            entries = DragonProxy.JSON_MAPPER.readValue(stream, type);
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        for (PaletteManager.RuntimeEntry entry : entries) {
            ITEM_PALETTE.add(new StartGamePacket.ItemEntry(entry.getName(), (short) entry.getId()));
        }

        stream = DragonProxy.class.getClassLoader().getResourceAsStream("item_mappings.json");
        if (stream == null) {
            throw new AssertionError("Item mapping table not found");
        }

        // TODO: This is currently using code devrived from Geyser, as i am not entirely sure how to
        //       use jackson to auto fill the class properties while also including the json keys
        //       .
        //       I also need to somehow retrieve the java item id auto increment the `javaIdAllocator`, then
        //       assign that to `ItemEntry.javaProtocolId`. fuck knows.
        JsonNode items;
        try {
            items = DragonProxy.JSON_MAPPER.readTree(stream);
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        Iterator<Map.Entry<String, JsonNode>> iterator = items.fields();
        while (iterator.hasNext()) {
            int javaId = javaIdAllocator.getAndIncrement();
            Map.Entry<String, JsonNode> entry = iterator.next();

            ITEM_ENTRIES.put(javaId, new ItemEntry(entry.getKey(), javaId, entry.getValue().get("bedrock_id").intValue(), entry.getValue().get("bedrock_data").intValue()));
        }
    }

    public static ItemData translateToBedrock(ItemStack item) {
        if(item == null) {
            return ItemData.AIR;
        }
        ItemEntry bedrockItem = ITEM_ENTRIES.get(item.getId());

        if (item.getNbt() == null) {
            return ItemData.of(bedrockItem.getBedrockRuntimeId(), (short) bedrockItem.getBedrockData(), item.getAmount());
        }
        return ItemData.of(bedrockItem.getBedrockRuntimeId(), (short) bedrockItem.getBedrockData(), item.getAmount(), translateItemNBT(item.getNbt()));
    }

    public static ItemData translateSlotToBedrock(ItemStack item) {
        if(item == null || item.getId() == 0) {
            return ItemData.AIR;
        }

        ItemData data = translateToBedrock(item);
        return data;
    }

    public static ItemStack translateToJava(ItemData item) {
        for(ItemEntry entry : ITEM_ENTRIES.values()) {
            if(entry.getBedrockRuntimeId() == item.getId()) {
                return new ItemStack(entry.getJavaProtocolId(), item.getCount());
            }
        }
        return new ItemStack(0);
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
