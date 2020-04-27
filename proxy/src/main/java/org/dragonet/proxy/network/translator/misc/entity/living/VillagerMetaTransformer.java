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
package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.VillagerData;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.AbstractInsentientMetaTranslator;

public class VillagerMetaTransformer extends AbstractInsentientMetaTranslator {

    private static final Int2IntMap VILLAGER_VARIANTS = new Int2IntOpenHashMap();
    private static final Int2IntMap VILLAGER_REGIONS = new Int2IntOpenHashMap();

    static {
        // Java villager profession IDs -> Bedrock
        VILLAGER_VARIANTS.put(0, 0);
        VILLAGER_VARIANTS.put(1, 8);
        VILLAGER_VARIANTS.put(2, 11);
        VILLAGER_VARIANTS.put(3, 6);
        VILLAGER_VARIANTS.put(4, 7);
        VILLAGER_VARIANTS.put(5, 1);
        VILLAGER_VARIANTS.put(6, 2);
        VILLAGER_VARIANTS.put(7, 4);
        VILLAGER_VARIANTS.put(8, 12);
        VILLAGER_VARIANTS.put(9, 5);
        VILLAGER_VARIANTS.put(10, 13);
        VILLAGER_VARIANTS.put(11, 14);
        VILLAGER_VARIANTS.put(12, 3);
        VILLAGER_VARIANTS.put(13, 10);
        VILLAGER_VARIANTS.put(14, 9);

        VILLAGER_REGIONS.put(0, 1);
        VILLAGER_REGIONS.put(1, 2);
        VILLAGER_REGIONS.put(2, 0);
        VILLAGER_REGIONS.put(3, 3);
        VILLAGER_REGIONS.put(4, 4);
        VILLAGER_REGIONS.put(5, 5);
        VILLAGER_REGIONS.put(6, 6);
    }

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if (metadata.getId() == 17) {
            VillagerData villagerData = (VillagerData) metadata.getValue();
            // Profession
            dictionary.putInt(EntityData.VARIANT, VILLAGER_VARIANTS.get(villagerData.getProfession()));
            // Region
            dictionary.putInt(EntityData.MARK_VARIANT, VILLAGER_REGIONS.get(villagerData.getType()));
            // Trade tier - different indexing in Bedrock
            dictionary.putInt(EntityData.TRADE_TIER, villagerData.getLevel() - 1);
        }

        super.translateToBedrock(session, dictionary, metadata);
    }
}
