package org.dragonet.proxy.data.nbt.tag;

import org.dragonet.proxy.data.nbt.stream.NBTInputStream;
import org.dragonet.proxy.data.nbt.stream.NBTOutputStream;
import org.dragonet.proxy.utilities.Binary;

import java.io.IOException;
import java.util.Arrays;

public class ByteArrayTag extends Tag {
    public byte[] data;

    public ByteArrayTag(String name) {
        super(name);
    }

    public ByteArrayTag(String name, byte[] data) {
        super(name);
        this.data = data;
    }

    @Override
    void write(NBTOutputStream dos) throws IOException {
        if (data == null) {
            dos.writeInt(0);
            return;
        }
        dos.writeInt(data.length);
        dos.write(data);
    }

    @Override
    void load(NBTInputStream dis) throws IOException {
        int length = dis.readInt();
        data = new byte[length];
        dis.readFully(data);
    }

    @Override
    public byte getId() {
        return TAG_Byte_Array;
    }
    
    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            ByteArrayTag byteArrayTag = (ByteArrayTag) obj;
            return ((data == null && byteArrayTag.data == null) || (data != null && Arrays.equals(data, byteArrayTag.data)));
        }
        return false;
    }

    @Override
    public Tag copy() {
        byte[] cp = new byte[data.length];
        System.arraycopy(data, 0, cp, 0, data.length);
        return new ByteArrayTag(getName(), cp);
    }
}
