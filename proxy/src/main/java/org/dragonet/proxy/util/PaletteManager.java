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
package org.dragonet.proxy.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.nbt.tag.ListTag;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v388.BedrockUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.types.ItemTranslator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Log4j2
public class PaletteManager {
    public static final List<StartGamePacket.ItemEntry> ITEM_PALETTE = new ArrayList<>();
    public static ListTag<CompoundTag> BLOCK_PALETTE;

    public static CompoundTag BIOME_ENTRIES;
    public static final List<RuntimeCreativeItemEntry> CREATIVE_ITEMS = new ArrayList<>();

    static {
        loadBlocks();
        loadItems();
        loadCreativeItems();
        loadBiomeData();
    }

    /**
     * Loads the block runtime data to cache for later use.
     */
    private static void loadBlocks() {
        // TODO: this is currently handled in BlockTranslator
    }

    private static void loadItems() {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("data/runtime_item_states.json");
        if (stream == null) {
            throw new AssertionError("Static item state table not found");
        }

        List<RuntimeItemEntry> entries;
        try {
            entries = DragonProxy.JSON_MAPPER.readValue(stream, new TypeReference<List<RuntimeItemEntry>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        entries.forEach(entry -> ITEM_PALETTE.add(new StartGamePacket.ItemEntry(entry.getName(), entry.getId())));
    }

    /**
     * Loads creative items data and then maps the legacy ids
     * to runtime ids.
     */
    private static void loadCreativeItems() {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("data/creative_items.json");
        if (stream == null) {
            throw new RuntimeException("Creative item data not found");
        }

        ObjectReader objectReader = DragonProxy.JSON_MAPPER.reader(new TypeReference<List<RuntimeCreativeItemEntry>>() {}).withRootName("items");
        try {
            CREATIVE_ITEMS.addAll(objectReader.readValue(stream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the biome definitions to cache for later use.
     * This is currently not used anywhere, but will be at some point.
     */
    private static void loadBiomeData() {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("data/biome_definitions.dat");
        if (stream == null) {
            throw new AssertionError("Biome data table not found");
        }

        try(NBTInputStream nbtInputStream = NbtUtils.createNetworkReader(stream)) {
            BIOME_ENTRIES = (CompoundTag) nbtInputStream.readTag();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    public static class RuntimeItemEntry {
        private String name;
        private short id;
    }

    @Getter
    public static class RuntimeCreativeItemEntry {
        private int id;
        private int damage;
        // TODO: nbt
    }
}
