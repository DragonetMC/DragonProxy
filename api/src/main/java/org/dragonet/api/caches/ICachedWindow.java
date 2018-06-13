/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.caches;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import java.util.Map;

/**
 *
 * @author Epic
 */
public interface ICachedWindow {
    /**
     * @return the windowId
     */
    public int getWindowId();

    /**
     * @return the isOpen
     */
    public boolean isIsOpen();

    /**
     * @param isOpen the isOpen to set
     */
    public void setIsOpen(boolean isOpen);

    /**
     * @return the pcType
     */
    public WindowType getPcType();

    /**
     * @return the size
     */
    public int getSize();

    /**
     * @return the title
     */
    public String getTitle();

    /**
     * @param title the title to set
     */
    public void setTitle(String title);

    /**
     * @return the properties
     */
    public Map<Integer, Integer> getProperties();

    /**
     * @return the slots
     */
    public ItemStack[] getSlots();

    /**
     * @param slots the slots to set
     */
    public void setSlots(ItemStack[] slots);
}
