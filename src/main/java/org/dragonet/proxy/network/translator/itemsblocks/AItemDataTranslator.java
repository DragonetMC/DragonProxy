package org.dragonet.proxy.network.translator.itemsblocks;

/**
 * @author vincent
 */
public abstract class AItemDataTranslator {

    public abstract void setOriginalDamage(Integer originalDamage);
    
    public abstract Integer translateToPE(Integer damage);

    public abstract Integer translateToPC(Integer damage);
}
