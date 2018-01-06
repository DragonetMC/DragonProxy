package org.dragonet.common.mcbedrock.protocol.type.transaction.data;

import org.dragonet.common.mcbedrock.maths.Vector3F;
import org.dragonet.common.mcbedrock.protocol.type.Slot;

/**
 * @author CreeperFace
 */
public class ReleaseItemData implements TransactionData {

    public int actionType;
    public int hotbarSlot;
    public Slot itemInHand;
    public Vector3F headRot;
}
