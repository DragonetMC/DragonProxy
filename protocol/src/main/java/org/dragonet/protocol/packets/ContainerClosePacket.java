package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class ContainerClosePacket extends PEPacket {

    public int windowId;

    public ContainerClosePacket() {

    }

    public ContainerClosePacket(int windowId) {
        this.windowId = windowId;
    }

    @Override
    public int pid() {
        return ProtocolInfo.CONTAINER_CLOSE_PACKET;
    }

    @Override
    public void encodePayload() {
        putByte((byte) (windowId & 0xFF));
    }

    @Override
    public void decodePayload() {
        windowId = getByte();
    }
}
