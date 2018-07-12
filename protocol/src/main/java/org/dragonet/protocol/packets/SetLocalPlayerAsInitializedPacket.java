package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

public class SetLocalPlayerAsInitializedPacket extends PEPacket {
    public long rtid;

    @Override
    public int pid() {
        return ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarInt(rtid);
    }

    @Override
    public void decodePayload() {
        rtid = getUnsignedVarInt();
    }
}
