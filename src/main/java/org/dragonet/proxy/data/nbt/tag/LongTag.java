package org.dragonet.proxy.data.nbt.tag;

import org.dragonet.proxy.data.nbt.stream.NBTInputStream;
import org.dragonet.proxy.data.nbt.stream.NBTOutputStream;

import java.io.IOException;

public class LongTag extends NumberTag<Long> {
    public long data;

    @Override
    public Long getData() {
        return data;
    }

    @Override
    public void setData(Long data) {
        this.data = data == null ? 0 : data;
    }

    public LongTag(String name) {
        super(name);
    }

    public LongTag(String name, long data) {
        super(name);
        this.data = data;
    }

    @Override
    void write(NBTOutputStream dos) throws IOException {
        dos.writeLong(data);
    }

    @Override
    void load(NBTInputStream dis) throws IOException {
        data = dis.readLong();
    }

    @Override
    public byte getId() {
        return TAG_Long;
    }
    
    @Override
    public Object getValue() {
        return this.data;
    }

    @Override
    public Tag copy() {
        return new LongTag(getName(), data);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            LongTag o = (LongTag) obj;
            return data == o.data;
        }
        return false;
    }

}
