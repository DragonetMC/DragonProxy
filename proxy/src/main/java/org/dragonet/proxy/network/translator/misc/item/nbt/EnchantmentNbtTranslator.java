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
package org.dragonet.proxy.network.translator.misc.item.nbt;

import com.github.steveice10.opennbt.tag.builtin.*;
import com.nukkitx.nbt.CompoundTagBuilder;
import org.dragonet.proxy.data.EnchantmentType;

public class EnchantmentNbtTranslator implements ItemNbtTranslator {

    @Override
    public com.nukkitx.nbt.tag.CompoundTag translateToBedrock(CompoundTag javaTag) {
        if(!javaTag.contains("Enchantments")) {
            return null;
        }
        CompoundTagBuilder root = CompoundTagBuilder.builder();

        ListTag enchantTag = javaTag.get("Enchantments");
        enchantTag.forEach(tag -> {
            if(tag instanceof CompoundTag) {
                StringTag id = ((CompoundTag) tag).get("id");
                ShortTag lvl = ((CompoundTag) tag).get("lvl");

                EnchantmentType enchantmentType = EnchantmentType.valueOf(id.getValue().replace("minecraft:", "").toUpperCase());
                if(enchantmentType == null) {
                    return;
                }

                root.shortTag("id", (short) enchantmentType.ordinal());
                root.shortTag("lvl", lvl.getValue());
            }
        });
        return root.buildRootTag();
    }
}
