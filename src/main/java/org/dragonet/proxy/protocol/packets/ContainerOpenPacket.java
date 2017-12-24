package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

/**
 * Created on 2017/10/21.
 */
public class ContainerOpenPacket extends PEPacket {

    public int windowId;
    public int type;
    public BlockPosition position;
    public long eid;

    @Override
    public int pid() {
        return ProtocolInfo.CONTAINER_OPEN_PACKET;
    }

    @Override
    public void encodePayload() {
        putByte((byte) (windowId));
        putByte((byte) (type));
        putBlockPosition(position);
        putVarLong(eid);
    }

    @Override
    public void decodePayload() {
        windowId = getByte();
        type = getByte();
        position = getBlockPosition();
        eid = getVarLong();
    }

    //private
}
