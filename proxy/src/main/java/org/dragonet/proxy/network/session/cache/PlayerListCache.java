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
package org.dragonet.proxy.network.session.cache;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.nukkitx.protocol.bedrock.data.ImageData;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.dragonet.proxy.data.PlayerListInfo;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerListCache implements Cache {
    @Setter
    private String header;
    @Setter
    private String footer;

    private Object2ObjectMap<UUID, PlayerListInfo> playerInfo = new Object2ObjectOpenHashMap<>();
    private Object2LongMap<UUID> playerEntityIds = new Object2LongOpenHashMap<>();

    private Object2ObjectMap<UUID, ImageData> remoteSkinCache = new Object2ObjectOpenHashMap<>();

    public void updateDisplayName(GameProfile profile, String displayName) {
        playerInfo.get(profile.getId()).setDisplayName(displayName);
    }

    @Override
    public void purge() {

    }
}
