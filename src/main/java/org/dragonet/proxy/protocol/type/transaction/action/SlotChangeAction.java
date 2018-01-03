package org.dragonet.proxy.protocol.type.transaction.action;

import java.util.HashSet;
import java.util.Set;

import org.dragonet.proxy.protocol.type.Slot;

/**
 * @author CreeperFace
 */
public class SlotChangeAction extends InventoryAction {

    protected int inventory;
    private int inventorySlot;

    public SlotChangeAction(int inventory, int inventorySlot, Slot sourceItem, Slot targetItem) {
        super(sourceItem, targetItem);
        this.inventorySlot = inventorySlot;
    }

    /**
     * Returns the inventorySlot in the inventory which this action modified.
     */
    public int getSlot() {
        return inventorySlot;
    }
}
