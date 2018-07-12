package org.dragonet.protocol.packets;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class MoveEntityAbsolutePacket extends PEPacket {

    public long rtid;
    public Vector3F position;
    public float yaw;
    public float headYaw;
    public float pitch;
    public boolean onGround;
    public boolean teleported;

    public MoveEntityAbsolutePacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.MOVE_ENTITY_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        byte flags = 0;
        flags |= teleported ? 0b1 : 0;
        flags |= onGround ? 0b10 : 0;
        putByte(flags);
        putVector3F(position);
        putByteRotation(pitch);
        putByteRotation(headYaw);
        putByteRotation(yaw);
    }

    @Override
    public void decodePayload() {
        rtid = getUnsignedVarLong();
        int flags = getByte();
        teleported = (flags & 0b1) != 0;
        onGround = (flags & 0b10) != 0;
        position = getVector3F();
        pitch = getByteRotation();
        headYaw = getByteRotation();
        yaw = getByteRotation();
    }
}
