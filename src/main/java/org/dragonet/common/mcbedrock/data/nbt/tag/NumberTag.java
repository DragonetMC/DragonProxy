package org.dragonet.common.mcbedrock.data.nbt.tag;

/**
 * author: MagicDroidX Nukkit Project
 */
public abstract class NumberTag<T extends Number> extends Tag {

    protected NumberTag(String name) {
        super(name);
    }

    public abstract T getData();

    public abstract void setData(T data);
}
