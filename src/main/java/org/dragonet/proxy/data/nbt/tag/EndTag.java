package org.dragonet.proxy.data.nbt.tag;

import org.dragonet.proxy.data.nbt.stream.NBTInputStream;
import org.dragonet.proxy.data.nbt.stream.NBTOutputStream;

import java.io.IOException;

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
    public Object getValue() {
        return null;
    }

    @Override
    public Tag copy() {
        return new EndTag();
    }

}
