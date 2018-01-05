package org.dragonet.proxy.network.translator.itemsblocks;

import org.dragonet.proxy.data.itemsblocks.ItemEntry;

/**
 * @author vincent
 */
public abstract class AItemDataTranslator {

    public abstract void setOriginalDamage(Integer originalDamage);
    
    public abstract Integer translateToPE(Integer damage);

    public abstract Integer translateToPC(Integer damage);
}
