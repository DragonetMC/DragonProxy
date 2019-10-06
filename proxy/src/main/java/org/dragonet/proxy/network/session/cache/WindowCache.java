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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session.cache;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import lombok.Getter;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class WindowCache implements Cache {
    @Getter
    private Map<Integer, CachedWindow> windows = new HashMap<>();

    public WindowCache() {
        windows.put(0, new CachedWindow(0, null, 45));
    }

    public CachedWindow getPlayerInventory() {
        return windows.get(0);
    }

    public CachedWindow getById(int windowId) {
        if(windows.containsKey(windowId)) {
            return windows.get(windowId);
        }
        return null;
    }

    public CachedWindow newWindow(WindowType type, int windowId) {
        CachedWindow window = new CachedWindow(windowId, type, 50);
        windows.put(window.getWindowId(), window);
        return window;
    }

    public void destroyEntity(int windowId) {
        windows.remove(windowId);
    }

    @Override
    public void purge() {
        windows.clear();
    }
}
