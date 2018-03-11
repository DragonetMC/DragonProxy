package org.dragonet.protocol.type.transaction.data;

import org.dragonet.common.data.inventory.Slot;
import org.dragonet.common.maths.Vector3F;

/**
 * @author CreeperFace
 */
public class UseItemOnEntityData implements TransactionData {

    public long entityRuntimeId;
    public int actionType;
    public int hotbarSlot;
    public Slot itemInHand;
    public Vector3F playerPos;
    public Vector3F clickPos;

}
