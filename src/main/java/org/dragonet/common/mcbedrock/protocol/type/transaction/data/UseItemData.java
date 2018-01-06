package org.dragonet.common.mcbedrock.protocol.type.transaction.data;

import org.dragonet.common.mcbedrock.protocol.type.Slot;
import org.dragonet.common.mcbedrock.utilities.BlockPosition;
import org.dragonet.common.mcbedrock.utilities.Vector3F;


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
