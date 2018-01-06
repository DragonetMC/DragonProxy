package org.dragonet.common.mcbedrock.protocol.type.transaction.data;

import org.dragonet.common.mcbedrock.protocol.type.Slot;
import org.dragonet.common.mcbedrock.utilities.Vector3F;


/**
 * @author CreeperFace
 */
public class ReleaseItemData implements TransactionData {

    public int actionType;
    public int hotbarSlot;
    public Slot itemInHand;
    public Vector3F headRot;
}
