package org.dragonet.protocol.type.transaction.action;

import org.dragonet.common.data.inventory.Slot;

/**
 * @author CreeperFace
 */
public class DropItemAction extends InventoryAction {

    public DropItemAction(Slot source, Slot target) {
        super(source, target);
    }
}
