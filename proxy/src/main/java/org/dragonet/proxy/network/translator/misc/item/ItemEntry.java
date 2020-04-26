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

import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dragonet.proxy.data.item.ToolTier;
import org.dragonet.proxy.data.item.ToolType;

@Getter
@AllArgsConstructor
public class ItemEntry {
    private String javaIdentifier;
    private int javaProtocolId;
    private int bedrockRuntimeId;
    private int bedrockData;
    private ToolType toolType;
    private ToolTier toolTier;

    public ItemData toItemData(int amount) {
        return ItemData.of(bedrockRuntimeId, (short) bedrockData, amount);
    }

    public ItemData toItemData(int amount, CompoundTag tag) {
        return ItemData.of(bedrockRuntimeId, (short) bedrockData, amount, tag);
    }
}
