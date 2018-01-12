package org.dragonet.common.data.itemsblocks;

/**
 * @author vincent
 */
public abstract class AItemDataTranslator {

    public abstract void setOriginalDamage(Integer originalDamage);
    
    public abstract Integer translateToPE(Integer damage);

    public abstract Integer translateToPC(Integer damage);
}
