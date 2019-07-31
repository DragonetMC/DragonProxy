/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.v361.BedrockUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import org.dragonet.proxy.DragonProxy;

import java.io.InputStream;
import java.util.ArrayList;

public class PaletteManager {

    @Getter
    private ByteBuf cachedPalette;

    public PaletteManager() {
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
