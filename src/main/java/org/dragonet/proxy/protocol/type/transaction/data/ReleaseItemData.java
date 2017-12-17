package org.dragonet.proxy.protocol.type.transaction.data;

import org.dragonet.proxy.protocol.type.Slot;
import org.dragonet.proxy.utilities.Vector3F;


/**
 * @author CreeperFace
 */
public class ReleaseItemData implements TransactionData {

    public int actionType;
    public int hotbarSlot;
    public Slot itemInHand;
    public Vector3F headRot;
}
