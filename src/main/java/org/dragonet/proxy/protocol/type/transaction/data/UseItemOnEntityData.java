package org.dragonet.proxy.protocol.type.transaction.data;

import org.dragonet.proxy.protocol.type.Slot;
import org.dragonet.proxy.protocol.type.transaction.data.TransactionData;
import org.dragonet.proxy.utilities.BlockPosition;

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
