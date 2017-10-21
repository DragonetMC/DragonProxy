package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class FullChunkDataPacket extends PEPacket {

    public int x;
    public int z;

    public byte[] payload;

    @Override
    public int pid() {
        return ProtocolInfo.FULL_CHUNK_DATA_PACKET;
    }

    @Override
    public void decodePayload() {
        x = getVarInt();
        z = getVarInt();
        payload = getByteArray();
    }

    @Override
    public void encodePayload() {
        putVarInt(x);
        putVarInt(z);
        putByteArray(payload);
    }

}
