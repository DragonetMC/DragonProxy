package org.dragonet.proxy.network.translator.itemsblocks;

import org.dragonet.proxy.data.itemsblocks.ItemEntry;

/**
 * @author Epic
 */
public class IItemDataTranslator extends AItemDataTranslator{

    private Integer originalDamage;

    public void setOriginalDamage(Integer originalDamage) {
        this.originalDamage = originalDamage;
    }
    
    public Integer getOriginalDamage(){
        return this.originalDamage;
    }

    public Integer translateToPE(Integer damage) {
        return damage;
    }

    public Integer translateToPC(Integer damage) {
        return damage;
    }
}
