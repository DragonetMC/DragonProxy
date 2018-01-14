package org.dragonet.protocol.type.transaction.data;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.common.data.inventory.Slot;
import org.dragonet.common.maths.BlockPosition;


/**
 * @author CreeperFace
 */
public class UseItemData implements TransactionData {

    public int actionType;
    public BlockPosition blockPos;
    public int face;
    public int hotbarSlot;
    public Slot itemInHand;
    public Vector3F playerPos;
    //placement in block
    public Vector3F clickPos;

}
