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
package org.dragonet.proxy.network.session.data;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Represents all possible device operating system types
 * specified in the bedrock LoginPacket.
 */
public enum DeviceOS {
    @JsonEnumDefaultValue
    UNKNOWN,
    ANDROID,
    IOS,
    OSX,
    FIREOS,
    GEARVR,
    HOLOLENS,
    WIN10,
    WIN32,
    DEDICATED,
    TV_OS,
    ORBIS,
    NX,
    XBOXONE
}
