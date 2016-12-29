package org.dragonet.proxy.nbt.tag;

import java.io.IOException;

import org.dragonet.proxy.nbt.stream.NBTInputStream;
import org.dragonet.proxy.nbt.stream.NBTOutputStream;

public class EndTag extends Tag {

    public EndTag() {
        super(null);
    }

    @Override
    void load(NBTInputStream dis) throws IOException {
    }

    @Override
    void write(NBTOutputStream dos) throws IOException {
    }

    @Override
    public byte getId() {
        return TAG_End;
    }

    @Override
    public String toString() {
        return "EndTag";
    }

    @Override
    public Tag copy() {
        return new EndTag();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
