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

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.IItemTranslator;
import org.dragonet.proxy.util.registry.ItemRegisterInfo;

@Log4j2
@ItemRegisterInfo(bedrockId = 358)
public class MapItemTranslator implements IItemTranslator {

    @Override
    public ItemData translateToBedrock(ItemStack item, ItemEntry itemEntry) {
        if(item.getNbt() == null) {
            return itemEntry.toItemData(item.getAmount());
        }
        CompoundTagBuilder root = CompoundTagBuilder.builder();
        CompoundTag javaTag = item.getNbt();

        root.tag(ItemTranslatorRegistry.translateItemNBT(javaTag));

        if(javaTag.contains("map")) {
            root.longTag("map_uuid", ((IntTag) javaTag.get("map")).getValue().longValue());
            root.intTag("map_name_index", ((IntTag) javaTag.get("map")).getValue());
        }
        return itemEntry.toItemData(item.getAmount(), root.buildRootTag());
    }
}
