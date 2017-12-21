package org.dragonet.proxy.protocol.type.transaction.action;

import org.dragonet.proxy.protocol.type.Slot;

/**
 * @author CreeperFace
 */
public class DropItemAction extends InventoryAction {

    public DropItemAction(Slot source, Slot target) {
        super(source, target);
    }
}
