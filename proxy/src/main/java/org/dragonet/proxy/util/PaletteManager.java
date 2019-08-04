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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v361.BedrockUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import org.dragonet.proxy.DragonProxy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PaletteManager {
    private ByteBuf cachedPalette;
    private List<StartGamePacket.ItemEntry> itemEntries;

    public PaletteManager() {
        loadBlocks();
        loadItems();
    }

    private void loadBlocks() {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("data/runtimeid_table.json");
        if (stream == null) {
            throw new AssertionError("Static Runtime ID table not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, RuntimeEntry.class);

        ArrayList<RuntimeEntry> entries = new ArrayList<>();
        try {
            entries = mapper.readValue(stream, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cachedPalette = Unpooled.buffer();

        VarInts.writeUnsignedInt(cachedPalette, entries.size());

        for (RuntimeEntry entry : entries) {
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

    private static class RuntimeEntry {
        @JsonProperty("name")
        private String name;
        @JsonProperty("id")
        private int id;
        @JsonProperty("data")
        private int data;

        public RuntimeEntry() {}

        public RuntimeEntry(String name, int id, int data) {
            this.id = id;
            this.name = name;
            this.data = data;
        }
    }
}
