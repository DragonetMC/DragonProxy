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
package org.dragonet.proxy.data.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemList {
    // vars

    private List<ItemStack> items;

    // constructor
    public ItemList() {
        this.items = new ArrayList<>();
    }

    public ItemList(ArrayList<ItemStack> items) {
        this.items = items;
    }

    public ItemList(ItemStack[] items) {
        this.items = Arrays.asList(items);
    }

    // public
    public boolean tryToRemove(ItemStack item) {
        ArrayList<ItemStack> original = this.cloneList();
        if (item == null || item.getId() == 0) {
            return true;
        }
        int toRemove = item.getAmount();
        for (int i = 0; i < items.size(); i++) {
            if (toRemove == 0) {
                break;
            }
            if (items.get(i) == null) {
                continue;
            }
            int typeID = items.get(i).getId();
            int damage = items.get(i).getData();
            int amount = items.get(i).getAmount();
            CompoundTag nbt = items.get(i).getNBT();
            if (typeID == item.getId() && damage == item.getData()) {
                // We found the item
                if (amount > toRemove) {
                    // SUCCESS
                    items.set(i, new ItemStack(typeID, amount - toRemove, damage, nbt));
                    return true;
                } else {
                    items.set(i, null);
                    toRemove -= amount;
                }
            }
        }
        if (toRemove <= 0) {
            return true;
        } else {
            this.items = original;
            return false;
        }
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public ItemStack[] getContents() {
        return this.items.toArray(new ItemStack[0]);
    }

    private ArrayList<ItemStack> cloneList() {
        ArrayList<ItemStack> cloned = new ArrayList<>();
        for (ItemStack item : this.items) {
            cloned.add(new ItemStack(item.getId(), item.getAmount(), item.getData(), item.getNBT()));
        }
        return cloned;
    }

    // private
}
