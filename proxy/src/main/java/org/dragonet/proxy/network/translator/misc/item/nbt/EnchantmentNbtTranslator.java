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

import com.fasterxml.jackson.databind.JsonNode;
import com.github.steveice10.opennbt.tag.builtin.*;
import com.nukkitx.nbt.CompoundTagBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class EnchantmentNbtTranslator implements ItemNbtTranslator {
    private static final Object2IntMap<String> enchantMap = new Object2IntLinkedOpenHashMap<>();

    static {
        InputStream stream = FileUtils.getResource("mappings/1.15/enchantments.json");
        if(stream == null) {
            throw new AssertionError("Cannot find enchantment mappings");
        }

        try {
            JsonNode rootNode = DragonProxy.JSON_MAPPER.readTree(stream);
            rootNode.fields().forEachRemaining(entry -> enchantMap.put(entry.getKey(), entry.getValue().intValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

                if(enchantMap.containsKey(id.getValue())) {
                    root.shortTag("id", (short) enchantMap.getInt(id));
                    root.shortTag("lvl", lvl.getValue());
                }
            }
        });
        return root.buildRootTag();
    }
}
