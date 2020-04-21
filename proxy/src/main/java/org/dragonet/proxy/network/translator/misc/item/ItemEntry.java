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
package org.dragonet.proxy.network.translator.misc.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemEntry {
    @JsonProperty
    private String javaIdentifier;
    @JsonProperty("javaId")
    private int javaProtocolId;

    // TODO: add back support for bedrock identifier, as the geyser mappings dont include it
    //private String bedrockIdentifier;
    @JsonProperty("bedrockId")
    private int bedrockRuntimeId;
    @JsonProperty
    private int bedrockData;

    public ItemData toItemData(int amount) {
        return ItemData.of(bedrockRuntimeId, (short) bedrockData, amount);
    }

    public ItemData toItemData(int amount, CompoundTag tag) {
        return ItemData.of(bedrockRuntimeId, (short) bedrockData, amount, tag);
    }
}
