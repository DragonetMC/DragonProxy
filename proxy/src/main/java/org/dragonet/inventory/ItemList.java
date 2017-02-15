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
package org.dragonet.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

public class ItemList {

    private List<ItemStack> items;

    public ItemList() {
        this.items = new ArrayList<>();
    }

    public ItemList(ArrayList<ItemStack> items) {
        this.items = items;
    }

    public ItemList(ItemStack[] items) {
        this.items = Arrays.asList(items);
    }

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
                //We found the item
                if (amount > toRemove) {
                    //SUCCESS
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

    private ArrayList<ItemStack> cloneList() {
        ArrayList<ItemStack> cloned = new ArrayList<>();
        for (ItemStack item : this.items) {
            cloned.add(new ItemStack(item.getId(), item.getAmount(), item.getData(), item.getNBT()));
        }
        return cloned;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public ItemStack[] getContents() {
        return this.items.toArray(new ItemStack[0]);
    }

}
