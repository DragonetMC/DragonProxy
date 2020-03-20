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
package org.dragonet.proxy.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTInputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v388.BedrockUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import lombok.Getter;
import org.dragonet.proxy.DragonProxy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class PaletteManager {
    private ByteBuf cachedPalette;
    private List<StartGamePacket.ItemEntry> itemEntries;
    private static final Int2IntArrayMap legacyToRuntimeId = new Int2IntArrayMap();
    private static final Int2IntArrayMap runtimeIdToLegacy = new Int2IntArrayMap();
    private static final AtomicInteger runtimeIdAllocator = new AtomicInteger(0);

    private ArrayList<RuntimeEntry> entries = new ArrayList<>();

    public static final CompoundTag BIOME_ENTRIES;

    static {
        // Load biome data
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("data/biome_definitions.dat");
        if (stream == null) {
            throw new AssertionError("Biome data table not found");
        }

        try(NBTInputStream biomenbtInputStream = NbtUtils.createNetworkReader(stream)) {
            BIOME_ENTRIES = (CompoundTag) biomenbtInputStream.readTag();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public PaletteManager() {
        //loadBlocks();
        //loadItems();
    }

    private void loadBlocks() {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("data/runtimeid_table.json");
        if (stream == null) {
            throw new AssertionError("Static Runtime ID table not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, RuntimeEntry.class);

        //ArrayList<RuntimeEntry> entries = new ArrayList<>();
        try {
            entries = mapper.readValue(stream, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cachedPalette = Unpooled.buffer();

        VarInts.writeUnsignedInt(cachedPalette, entries.size());

        for (RuntimeEntry entry : entries) {
            registerMapping((entry.id << 4) | entry.data);
            BedrockUtils.writeString(cachedPalette, entry.name);
            cachedPalette.writeShortLE(entry.data);
            cachedPalette.writeShortLE(entry.id);
        }

    }

    private void loadItems() {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("runtime_item_ids.json");
        if (stream == null) {
            throw new AssertionError("Static Runtime Item ID table not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, RuntimeEntry.class);

        ArrayList<RuntimeEntry> entries = new ArrayList<>();
        try {
            entries = mapper.readValue(stream, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        itemEntries = new ArrayList<>();

        for(RuntimeEntry entry : entries) {
            itemEntries.add(new StartGamePacket.ItemEntry(entry.name, (short) entry.id));
        }
    }

    public static int getOrCreateRuntimeId(int id, int meta) {
        return getOrCreateRuntimeId((id << 4) | meta);
    }

    public static int getOrCreateRuntimeId(int legacyId) throws NoSuchElementException {
        int runtimeId = legacyToRuntimeId.get(legacyId);
        if (runtimeId == -1) {
            throw new NoSuchElementException("Unmapped block registered id:" + (legacyId >>> 4) + " meta:" + (legacyId & 0xf));
        }
        return runtimeId;
    }

    public static int fromLegacy(int id, byte data) {
        int runtimeId;
        if ((runtimeId = legacyToRuntimeId.get((id << 4) | data)) == -1) {
            throw new IllegalArgumentException("Unknown legacy id");
        }
        return runtimeId;
    }

    public static int getLegacyId(int runtimeId) {
        int legacyId;
        if ((legacyId = runtimeIdToLegacy.get(runtimeId)) == -1) {
            throw new IllegalArgumentException("Unknown runtime id");
        }
        return legacyId;
    }

    private static int registerMapping(int legacyId) {
        int runtimeId = runtimeIdAllocator.getAndIncrement();
        runtimeIdToLegacy.put(runtimeId, legacyId);
        legacyToRuntimeId.put(legacyId, runtimeId);
        return runtimeId;
    }

    @Getter
    public static class RuntimeEntry {
        @JsonProperty("name")
        private String name;
        @JsonProperty("id")
        private int id;
        @JsonProperty("data")
        private int data;

        public RuntimeEntry() {}

        public RuntimeEntry(String name, int id) {
            this.id = id;
            this.name = name;
            this.data = 0;
        }

        public RuntimeEntry(String name, int id, int data) {
            this.id = id;
            this.name = name;
            this.data = data;
        }
    }
}
