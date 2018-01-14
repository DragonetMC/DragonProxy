package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class SetTimePacket extends PEPacket {

    public int time;

    public SetTimePacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.SET_TIME_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarInt(time);
    }

    @Override
    public void decodePayload() {
        time = getVarInt();
    }
}
