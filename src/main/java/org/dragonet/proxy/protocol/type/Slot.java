package org.dragonet.proxy.protocol.type;

import org.dragonet.proxy.data.nbt.tag.CompoundTag;

/**
 * Created on 2017/10/21.
 */
public class Slot {

    public static final Slot AIR = new Slot();
    public int id;
    public int damage;
    public int count;
    public CompoundTag tag;

    public Slot() {

    }

    public Slot(int id) {
        this.id = id;
    }

    public Slot(int id, int damage) {
        this.id = id;
        this.damage = damage;
    }

    public Slot(int id, int damage, int count) {
        this.id = id;
        this.damage = damage;
        this.count = count;
    }

    public Slot(int id, int damage, int count, CompoundTag tag) {
        this.id = id;
        this.damage = damage;
        this.count = count;
        this.tag = tag;
    }

    public Slot clone() {
        return new Slot(id, damage, count, tag);
    }
}
