package org.dragonet.protocol.packets;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class RespawnPacket extends PEPacket {

    public Vector3F position;

    public RespawnPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.RESPAWN_PACKET;
    }

    @Override
    public void encodePayload() {
        putVector3F(position);
    }

    @Override
    public void decodePayload() {
        position = getVector3F();
    }
}
