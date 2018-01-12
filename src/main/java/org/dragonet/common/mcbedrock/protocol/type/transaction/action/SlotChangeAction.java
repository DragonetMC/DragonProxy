package org.dragonet.common.mcbedrock.protocol.type.transaction.action;

import org.dragonet.common.mcbedrock.protocol.type.Slot;

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
