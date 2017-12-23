package org.dragonet.proxy.protocol.type.transaction.action;

import org.dragonet.proxy.protocol.type.Slot;


/**
 * @author CreeperFace
 */
public abstract class InventoryAction {

    private long creationTime;

    protected Slot sourceItem;

    protected Slot targetItem;

    public InventoryAction(Slot sourceItem, Slot targetItem) {
        this.sourceItem = sourceItem;
        this.targetItem = targetItem;

        this.creationTime = System.currentTimeMillis();
    }

    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Returns the item that was present before the action took place.
     *
     * @return Item
     */
    public Slot getSourceItem() {
        return sourceItem.clone();
    }

    /**
     * Returns the item that the action attempted to replace the source item with.
     */
    public Slot getTargetItem() {
        return targetItem.clone();
    }
}
