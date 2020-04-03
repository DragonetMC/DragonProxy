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
package org.dragonet.proxy.data.window;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The bedrock inventory types.
 *
 * A lot of them re-use the same container ids, presumably because they
 * are the same type of inventory (e.g. chest and ender chest and the exact same)
 */
@RequiredArgsConstructor
@Getter
public enum BedrockWindowType {
    CHEST(0, "minecraft:chest"),
    ENDER_CHEST(0, "minecraft:ender_chest"),
    DOUBLE_CHEST(0, "minecraft:chest"),
    SHULKER_BOX(0, "minecraft:shulker_box"),
    CRAFTING_TABLE(1, "minecraft:crafting_table"),
    FURNACE(2, "minecraft:furnace"),
    ENCHANT_TABLE(3, "minecraft:enchanting_table"),
    BREWING_STAND(4, "minecraft:brewing_stand"),
    ANVIL(5, "minecraft:anvil"),
    DISPENSER(6, "minecraft:dispenser"),
    DROPPER(7, "minecraft:dropper"),
    HOPPER(8, "minecraft:hopper"),
    CAULDRON(9, "minecraft:cauldron"),
    //MINECART_CHEST(10, ""),
    //MINECART_HOPPER(11, ""),
    //HORSE(12, ""),
    BEACON(13, "minecraft:beacon"),
    STRUCTURE_BLOCK(14, "minecraft:structure_block"),
    //TRADING(15, ""),
    COMMAND_BLOCK(16, "minecraft:command_block"),
    JUKEBOX(17, "minecraft:jukebox");

    private final int containerId;
    private final String fakeBlockId;
}
