package org.dragonet.protocol.type.transaction.data;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.common.data.inventory.Slot;

/**
 * @author CreeperFace
 */
public class ReleaseItemData implements TransactionData {

    public int actionType;
    public int hotbarSlot;
    public Slot itemInHand;
    public Vector3F headRot;
}
