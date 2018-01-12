package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/22.
 */
public class RequestChunkRadiusPacket extends PEPacket {

    public int radius;

    public RequestChunkRadiusPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarInt(radius);
    }

    @Override
    public void decodePayload() {
        radius = getVarInt();
    }
}
