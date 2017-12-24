package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.Slot;

/**
 * Created on 2017/10/21.
 */
public class InventorySlotPacket extends PEPacket {

    public int windowId;
    public int slotId;
    public Slot item;

    public InventorySlotPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.INVENTORY_SLOT_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarInt(windowId);
        putUnsignedVarInt(slotId);
        putSlot(item);
    }

    @Override
    public void decodePayload() {
        windowId = (int) getUnsignedVarInt();
        slotId = (int) getUnsignedVarInt();
        item = getSlot();
    }
}
