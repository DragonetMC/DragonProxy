/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.data.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BedrockAttributeType {
    MOVEMENT_SPEED("minecraft:movement", 0f, 1024f, 0.10f),
    KNOCKBACK_RESISTANCE("minecraft:knockback_resistance", 0f, 1f, 0f),
    ATTACK_DAMAGE("minecraft:attack_damage", 0f, 2048f, 1f),
    HUNGER("minecraft:player.hunger", 0f, 20f, 20f),
    SATURATION("minecraft:player.saturation", 0f, 20f, 20f),
    HEALTH("minecraft:health", 0f, 1024f, 20f);

    private String identifier;
    private float minimumValue;
    private float maximumValue;
    private float defaultValue;
}
