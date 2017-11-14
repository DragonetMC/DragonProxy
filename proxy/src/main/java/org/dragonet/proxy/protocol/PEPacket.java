package org.dragonet.proxy.protocol;

import lombok.Getter;
import org.dragonet.proxy.utilities.BinaryStream;

/**
 * Created on 2017/10/21.
 */
public abstract class PEPacket extends BinaryStream {

    @Getter
    private boolean encoded;

    @Getter
    private boolean decoded;

    public final void encode() {
        reset();
        encodeHeader();
        encodePayload();
        encoded = true;
    }

    public final void decode() {
        decodeHeader();
        decodePayload();
        decoded = true;
    }

    public void encodeHeader() {
        putUnsignedVarInt(pid());
        putByte((byte) 0x00);
        putByte((byte) 0x00);
    }

    public void decodeHeader() {
        getUnsignedVarInt();
        get(2);
    }

    public abstract int pid();

    public abstract void encodePayload();

    public abstract void decodePayload();

}
