package org.dragonet.proxy.nbt.tag;

import org.dragonet.proxy.nbt.stream.NBTInputStream;
import org.dragonet.proxy.nbt.stream.NBTOutputStream;

import java.io.IOException;

public class DoubleTag extends NumberTag<Double> {
    public double data;

    @Override
    public Double getData() {
        return data;
    }

    @Override
    public void setData(Double data) {
        this.data = data == null ? 0 : data;
    }

    public DoubleTag(String name) {
        super(name);
    }

    public DoubleTag(String name, double data) {
        super(name);
        this.data = data;
    }

    @Override
    void write(NBTOutputStream dos) throws IOException {
        dos.writeDouble(data);
    }

    @Override
    void load(NBTInputStream dis) throws IOException {
        data = dis.readDouble();
    }

    @Override
    public byte getId() {
        return TAG_Double;
    }
    
    @Override
    public Object getValue()
    {
        return this.data;
    }

    @Override
    public Tag copy() {
        return new DoubleTag(getName(), data);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            DoubleTag o = (DoubleTag) obj;
            return data == o.data;
        }
        return false;
    }

}
