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
import org.dragonet.api.caches.ICachedWindow;

public class CachedWindow implements ICachedWindow {

    private final int windowId;
    private boolean isOpen = false;
    /**
     * The type of this window on remote side, -1 for player inventory.
     */
    private final WindowType pcType;
    private final int size;
    private String title = "Window";
    private final Map<Integer, Integer> properties = Collections.synchronizedMap(new HashMap<>());
    private ItemStack[] slots;

    public CachedWindow(int windowId, WindowType pcType, int size) {
        this.windowId = windowId;
        this.pcType = pcType;
        this.size = size;
        slots = new ItemStack[this.size];
    }

    /**
     * @return the windowId
     */
    @Override
    public int getWindowId() {
        return windowId;
    }

    /**
     * @return the isOpen
     */
    @Override
    public boolean isIsOpen() {
        return isOpen;
    }

    /**
     * @param isOpen the isOpen to set
     */
    @Override
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * @return the pcType
     */
    @Override
    public WindowType getPcType() {
        return pcType;
    }

    /**
     * @return the size
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the properties
     */
    @Override
    public Map<Integer, Integer> getProperties() {
        return properties;
    }

    /**
     * @return the slots
     */
    @Override
    public ItemStack[] getSlots() {
        return slots;
    }

    /**
     * @param slots the slots to set
     */
    @Override
    public void setSlots(ItemStack[] slots) {
        this.slots = slots;
    }
}
