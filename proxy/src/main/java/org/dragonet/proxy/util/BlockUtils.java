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

import com.google.common.base.Preconditions;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.item.ToolTier;
import org.dragonet.proxy.data.item.ToolType;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.BlockTranslator;
import org.dragonet.proxy.network.translator.misc.item.ItemEntry;

@Log4j2
public class BlockUtils {

    public static boolean isCorrectTool(ItemEntry itemEntry, BlockTranslator.BlockMappingEntry blockEntry) {
        return blockEntry.getToolType() == null || blockEntry.getToolType().equals(itemEntry.getToolType());
    }

    /**
     * Modified from NukkitX.
     */
    private static double toolBreakTimeBonus0(ToolType toolType, ToolTier toolTier, boolean isWoolBlock, boolean isCobweb) {
        if (toolType == ToolType.SWORD) return isCobweb ? 15.0 : 1.0;
        if (toolType == ToolType.SHEARS) return isWoolBlock ? 5.0 : 15.0;
        if (toolType == null) return 1.0;

        switch (toolTier) {
            case WOODEN:
                return 2.0;
            case STONE:
                return 4.0;
            case IRON:
                return 6.0;
            case DIAMOND:
                return 8.0;
            case GOLDEN:
                return 12.0;
        }
        return 1.0;
    }

    public static double getBreakTime(ItemData item, int blockId) {
        BlockTranslator.BlockMappingEntry blockEntry = BlockTranslator.ID_TO_ENTRY.get(blockId);
        ItemEntry itemEntry = ItemTranslatorRegistry.bedrockToJavaMap.get(item.getId());

        if(blockEntry == null) {
            log.info("(debug) block entry is null in getBreakTime, id: " + blockId);
            return 0;
        }
        if(itemEntry == null) {
            log.info("(debug) item entry is null in getBreakTime, id: " + item.getId());
            return 0;
        }

        boolean woolBlock = blockEntry.getBedrockIdentifier().equals("minecraft:wool");
        boolean cobwebBlock = blockEntry.getBedrockIdentifier().equals("minecraft:cobweb");
        boolean correctTool = isCorrectTool(itemEntry, blockEntry);

        double hardness = blockEntry.getHardness();
        double baseTime = (correctTool ? 1.5 : 5.0) * hardness;
        double speed = 1.0 / baseTime;

        if(hardness == 0) {
            return 0;
        }
        if(correctTool) {
            speed *= toolBreakTimeBonus0(itemEntry.getToolType(), itemEntry.getToolTier(), woolBlock, cobwebBlock);
        }
        return 1.0 / speed;
    }
}
