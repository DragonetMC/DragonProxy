/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.common.data.inventory;

import org.dragonet.common.data.nbt.tag.CompoundTag;

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

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Slot{");
        buffer.append("id=" + id);
        buffer.append(", damage=" + damage);
        buffer.append(", count=" + count);
        buffer.append(", tag=" + tag);
        buffer.append("}");
        return buffer.toString();
    }
}
