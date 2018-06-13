package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class FullChunkDataPacket extends PEPacket {

    public int x;
    public int z;
    public byte[] payload;

    public FullChunkDataPacket() {

    }

    public FullChunkDataPacket(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public FullChunkDataPacket(int x, int z, byte[] payload) {
        this.x = x;
        this.z = z;
        this.payload = payload;
    }

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
