package org.dragonet.proxy.protocol;

import lombok.Getter;
import org.dragonet.proxy.nbt.stream.NBTInputStream;
import org.dragonet.proxy.nbt.stream.NBTOutputStream;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.dragonet.proxy.nbt.tag.Tag;
import org.dragonet.proxy.protocol.type.Slot;
import org.dragonet.proxy.utilities.BinaryStream;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.Vector3F;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
