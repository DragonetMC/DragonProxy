package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/22.
 */
public class InteractPacket extends PEPacket {

    public final static byte ACTION_LEAVE_VEHICLE = 3;
    public final static byte ACTION_MOUSEOVER = 4;

    public final static byte ACTION_OPEN_INVENTORY = 6;

    public byte type;
    public long targetRtid;

    public Vector3F position;

    @Override
    public int pid() {
        return ProtocolInfo.INTERACT_PACKET;
    }

    @Override
    public void encodePayload() {
        putByte(type);
        putUnsignedVarLong(targetRtid);
        if(type == ACTION_MOUSEOVER) {
            putVector3F(position);
        }
    }

    @Override
    public void decodePayload() {
        type = (byte) (getByte() & 0xFF);
        targetRtid = getUnsignedVarLong();
        if(type == ACTION_MOUSEOVER) {
            position = getVector3F();
        }
    }
}
