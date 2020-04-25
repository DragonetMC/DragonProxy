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

import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.misc.inventory.IInventoryTranslator;
import org.dragonet.proxy.network.translator.misc.inventory.PlayerInventoryTranslator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class WindowCache implements Cache {
    private Int2ObjectMap<CachedWindow> windows = new Int2ObjectOpenHashMap<>();
    private AtomicInteger javaActionIdAllocator = new AtomicInteger(1);

    private AtomicInteger localWindowIdAllocator = new AtomicInteger(1000);
    private AtomicInteger transactionIdCounter = new AtomicInteger(0);

    public WindowCache() {
        windows.put(0, new CachedWindow(0, new PlayerInventoryTranslator(45)));
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

    public CachedWindow newWindow(IInventoryTranslator inventoryTranslator, int windowId) {
        CachedWindow window = new CachedWindow(windowId, inventoryTranslator);
        windows.put(window.getWindowId(), window);
        return window;
    }

    public void destroyWindow(int windowId) {
        windows.remove(windowId);
    }

    @Override
    public void purge() {
        windows.clear();
    }
}
