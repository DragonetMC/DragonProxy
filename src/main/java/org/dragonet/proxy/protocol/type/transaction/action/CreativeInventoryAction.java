package org.dragonet.proxy.protocol.type.transaction.action;

import org.dragonet.proxy.protocol.type.Slot;

/**
 * @author CreeperFace
 */
public class CreativeInventoryAction extends InventoryAction {
    /**
     * Player put an item into the creative window to destroy it.
     */
    public static final int TYPE_DELETE_ITEM = 0;
    /**
     * Player took an item from the creative window.
     */
    public static final int TYPE_CREATE_ITEM = 1;

    protected int actionType;

    public CreativeInventoryAction(Slot source, Slot target, int action) {
        super(source, target);
    }
}
