package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

public class BlockPickRequestPacket extends PEPacket {

    public int x;
    public int y;
    public int z;
    public boolean addUserData;
    public int selectedSlot;

    @Override
    public int pid() {
        return ProtocolInfo.BLOCK_PICK_REQUEST_PACKET;
    }

    @Override
    public void decodePayload() {
        BlockPosition v = this.getSignedBlockPosition();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.putBoolean(this.addUserData);
        this.selectedSlot = this.getByte();
    }

    @Override
    public void encodePayload() {

    }
}
