package org.dragonet.protocol.packets;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class InteractPacket extends PEPacket {

    public static final byte ACTION_RIGHT_CLICK = 1;
    public static final byte ACTION_LEFT_CLICK = 2;
    public static final byte ACTION_LEAVE_VEHICLE = 3;
    public static final byte ACTION_MOUSEOVER = 4;
    public static final byte ACTION_OPEN_INVENTORY = 6;

    public byte type;
    public long targetRtid;
    public Vector3F position;

    public InteractPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.INTERACT_PACKET;
    }

    @Override
    public void encodePayload() {
        putByte(type);
        putUnsignedVarLong(targetRtid);
        if (type == ACTION_MOUSEOVER) {
            putVector3F(position);
        }
    }

    @Override
    public void decodePayload() {
        type = (byte) (getByte() & 0xFF);
        targetRtid = getUnsignedVarLong();
        if (type == ACTION_MOUSEOVER) {
            position = getVector3F();
        }
    }
}
