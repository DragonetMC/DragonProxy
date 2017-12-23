package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Vector3F;

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
