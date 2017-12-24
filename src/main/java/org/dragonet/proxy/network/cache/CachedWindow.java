/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.cache;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CachedWindow {

    public final int windowId;
    /**
     * The type of this window on remote side, -1 for player inventory.
     */
    public final WindowType pcType;
    public final int size;
    public String title = "Window";
    public final Map<Integer, Integer> properties = Collections.synchronizedMap(new HashMap<Integer, Integer>());
    public ItemStack[] slots;

    public CachedWindow(int windowId, WindowType pcType, int size) {
        this.windowId = windowId;
        this.pcType = pcType;
        this.size = size;
        slots = new ItemStack[this.size];
    }
}
