package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.data.inventory.Slot;

/**
 * Created on 2017/10/21.
 */
public class InventoryContentPacket extends PEPacket {

    public int windowId;
    public Slot[] items;

    @Override
    public int pid() {
        return ProtocolInfo.INVENTORY_CONTENT_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarInt(windowId);
        if (items != null && items.length > 0) {
            putUnsignedVarInt(items.length);
            for (Slot s : items) {
                putSlot(s);
            }
        } else {
            putUnsignedVarInt(0);
        }
    }

    @Override
    public void decodePayload() {
        windowId = (int) getUnsignedVarInt();
        int count = (int) getUnsignedVarInt();
        items = new Slot[count];
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                items[i] = getSlot();
            }
        }
    }
}
