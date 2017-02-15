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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.values.window.WindowType;

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
