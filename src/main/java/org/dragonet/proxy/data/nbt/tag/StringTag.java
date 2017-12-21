package org.dragonet.proxy.data.nbt.tag;

import org.dragonet.proxy.data.nbt.stream.NBTInputStream;
import org.dragonet.proxy.data.nbt.stream.NBTOutputStream;

import java.io.IOException;

public class StringTag extends Tag {
    public String data;

    public StringTag(String name) {
        super(name);
    }

    public StringTag(String name, String data) {
        super(name);
        this.data = data;
        if (data == null) throw new IllegalArgumentException("Empty string not allowed");
    }

    @Override
    void write(NBTOutputStream dos) throws IOException {
        dos.writeUTF(data);
    }

    @Override
    void load(NBTInputStream dis) throws IOException {
        data = dis.readUTF();
    }

    @Override
    public byte getId() {
        return TAG_String;
    }
    
    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Tag copy() {
        return new StringTag(getName(), data);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            StringTag o = (StringTag) obj;
            return ((data == null && o.data == null) || (data != null && data.equals(o.data)));
        }
        return false;
    }

}
