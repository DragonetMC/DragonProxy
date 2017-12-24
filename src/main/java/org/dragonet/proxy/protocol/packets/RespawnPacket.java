package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Vector3F;

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
