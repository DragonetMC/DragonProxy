package org.dragonet.protocol.packets;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class MovePlayerPacket extends PEPacket {

    public static final byte MODE_NORMAL = 0;
    public static final byte MODE_RESET = 1;
    public static final byte MODE_TELEPORT = 2;
    public static final byte MODE_PITCH = 3;

    public long rtid;
    public Vector3F position;
    public float pitch;
    public float yaw;
    public float headYaw;
    public byte mode;
    public boolean onGround;
    public long ridingRuntimeId;

    public MovePlayerPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.MOVE_PLAYER_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        putVector3F(position);
        putLFloat(pitch);
        putLFloat(yaw);
        putLFloat(headYaw);
        putByte(mode);
        putBoolean(onGround);
        putUnsignedVarLong(ridingRuntimeId);
        if (mode == MODE_TELEPORT) {
            putLInt(0);
            putLInt(0);
        }
    }

    @Override
    public void decodePayload() {
        rtid = getUnsignedVarLong();
        position = getVector3F();
        pitch = getLFloat();
        yaw = getLFloat();
        headYaw = getLFloat();
        mode = (byte) (getByte() & 0xFF);
        onGround = getBoolean();
        ridingRuntimeId = getUnsignedVarLong();
        // if(mode == MODE_TELEPORT) {
        // getLInt();
        // getLInt();
        // }
    }
}
