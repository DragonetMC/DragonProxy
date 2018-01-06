package org.dragonet.common.mcbedrock.protocol.type.transaction.data;

import org.dragonet.common.mcbedrock.protocol.type.Slot;
import org.dragonet.common.mcbedrock.utilities.BlockPosition;

/**
 * @author CreeperFace
 */
public class UseItemOnEntityData implements TransactionData {

    public long entityRuntimeId;
    public int actionType;
    public int hotbarSlot;
    public Slot itemInHand;
    public BlockPosition vector1;
    public BlockPosition vector2;

}
