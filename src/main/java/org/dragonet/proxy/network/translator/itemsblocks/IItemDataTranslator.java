package org.dragonet.proxy.network.translator.itemsblocks;

/**
 *
 * @author vincent
 */
public abstract class IItemDataTranslator {
    
    public abstract Integer translateToPE(Integer damage);

    public abstract Integer translateToPC(Integer damage);
}
