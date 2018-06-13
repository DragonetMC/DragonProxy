package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.utilities.Binary;

public class PlayerHotbarPacket extends PEPacket {

    public int selectedHotbarSlot;
    public int windowId = -1;

//    public int[] slots;

    public boolean selectHotbarSlot = true;

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_HOTBAR_PACKET;
    }

    @Override
    public void decodePayload() {
        this.selectedHotbarSlot = (int) this.getUnsignedVarInt();
        this.windowId = this.getByte();
//        int count = (int) this.getUnsignedVarInt();
//        slots = new int[count];
//
//        for (int i = 0; i < count; ++i) {
//            this.slots[i] = Binary.signInt((int) this.getUnsignedVarInt());
//        }
        this.selectHotbarSlot = this.getBoolean();
    }

    @Override
    public void encodePayload() {
        this.reset();
        this.putUnsignedVarInt(this.selectedHotbarSlot);
        this.putByte((byte) this.windowId);
//        this.putUnsignedVarInt(this.slots.length);
//        for (int i : slots) {
//            this.putUnsignedVarInt(i);
//        }
        this.putBoolean(this.selectHotbarSlot);
    }
}
