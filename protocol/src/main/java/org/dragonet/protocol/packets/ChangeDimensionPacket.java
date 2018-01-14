package org.dragonet.protocol.packets;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/11/15.
 */
public class ChangeDimensionPacket extends PEPacket {

    public int dimension;
    public Vector3F position;
    public boolean respawn;

    public ChangeDimensionPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.CHANGE_DIMENSION_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarInt(dimension);
        putVector3F(position);
        putBoolean(respawn);
    }

    @Override
    public void decodePayload() {

    }
}
