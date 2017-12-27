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
package org.dragonet.proxy.data.itemsblocks;

import org.dragonet.proxy.network.translator.itemsblocks.IItemDataTranslator;

/**
 *
 * @author vincent
 */
public class ItemEntry {

    private Integer id;
    private Integer damage;
    private IItemDataTranslator translator;

    public ItemEntry(Integer id) {
        this(id, 0);
    }

    public ItemEntry(Integer id, Integer damage) {
        this.id = id;
        this.damage = damage;
    }

    public ItemEntry(Integer id, Integer damage, IItemDataTranslator translator) {
        this.id = id;
        this.damage = damage;
        this.translator = translator;
    }

    public Integer getId() {
        return this.id;
    }
    
    public void setDamage(int damage)
    {
        this.damage = damage;
    }

    public Integer getPEDamage() {
        if (this.translator != null) {
            return this.translator.translateToPE(this.damage);
        }
        return damage;
    }

    public Integer getPCDamage() {
        if (this.translator != null) {
            return this.translator.translateToPC(this.damage);
        }
        return damage;
    }
}
