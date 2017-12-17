package org.dragonet.proxy.protocol.type.transaction.data;

import org.dragonet.proxy.protocol.type.Slot;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.Vector3F;


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
