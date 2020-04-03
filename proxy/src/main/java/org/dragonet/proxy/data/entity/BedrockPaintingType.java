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
package org.dragonet.proxy.data.entity;

import com.nukkitx.protocol.bedrock.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.w3c.dom.Attr;

/**
 * This class uses values from the NukkitX Project.
 * https://github.com/NukkitX/Nukkit/blob/master/src/main/java/cn/nukkit/entity/item/EntityPainting.java#L91-#L136
 */
@Getter
@AllArgsConstructor
public enum BedrockPaintingType {
    KEBAB("Kebab", 1, 1),
    AZTEC("Aztec", 1, 1),
    ALBAN("Alban", 1, 1),
    AZTEC2("Aztec2", 1, 1),
    BOMB("Bomb", 1, 1),
    PLANT("Plant", 1, 1),
    WASTELAND("Wasteland", 1, 1),
    WANDERER("Wanderer", 1, 2),
    GRAHAM("Graham", 1, 2),
    POOL("Pool", 2, 1),
    COURBET("Courbet", 2, 1),
    SUNSET("Sunset", 2, 1),
    SEA("Sea", 2, 1),
    CREEBET("Creebet", 2, 1),
    MATCH("Match", 2, 2),
    BUST("Bust", 2, 2),
    STAGE("Stage", 2, 2),
    VOID("Void", 2, 2),
    SKULL_AND_ROSES("SkullAndRoses", 2, 2),
    WITHER("Wither", 2, 2),
    FIGHTERS("Fighters", 4, 2),
    SKELETON("Skeleton", 4, 3),
    DONKEY_KONG("DonkeyKong", 4, 3),
    POINTER("Pointer", 4, 4),
    PIG_SCENE("Pigscene", 4, 4),
    FLAMING_SKULL("Flaming Skull", 4, 4);

    private final String title;
    private final int width;
    private final int height;
}
