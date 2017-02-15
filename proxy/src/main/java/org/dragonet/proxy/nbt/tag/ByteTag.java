package org.dragonet.proxy.nbt.tag;

import org.dragonet.proxy.nbt.stream.NBTInputStream;
import org.dragonet.proxy.nbt.stream.NBTOutputStream;

import java.io.IOException;

public class ByteTag extends NumberTag<Byte> {
    public byte data;

    @Override
    public Byte getData() {
        return data;
    }

    @Override
    public void setData(Byte data) {
        this.data = data == null ? 0 : data;
    }

    public ByteTag(String name) {
        super(name);
    }

    public ByteTag(String name, byte data) {
        super(name);
        this.data = data;
    }

    @Override
    void write(NBTOutputStream dos) throws IOException {
        dos.writeByte(data);
    }

    @Override
    void load(NBTInputStream dis) throws IOException {
        data = dis.readByte();
    }

    @Override
    public byte getId() {
        return TAG_Byte;
    }

    @Override
    public String toString() {
        String hex = Integer.toHexString(this.data & 0xff);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return "ByteTag " + this.getName() + " (data: 0x" + hex + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            ByteTag byteTag = (ByteTag) obj;
            return data == byteTag.data;
        }
        return false;
    }

    @Override
    public Tag copy() {
        return new ByteTag(getName(), data);
    }
}
