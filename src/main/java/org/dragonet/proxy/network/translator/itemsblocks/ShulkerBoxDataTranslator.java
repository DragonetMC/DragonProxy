/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.proxy.network.translator.itemsblocks;

import org.dragonet.proxy.network.translator.ItemBlockTranslator;

/**
 * @author Epic
 */
public class ShulkerBoxDataTranslator extends IItemDataTranslator {

    //Shulker box datas:
    // MC java : 0 - 5 -> facing
    //
    // MC bedrock 0 -> 16 color (0x0F) + 0 - 5 facing
    
    
    @Override
    public Integer translateToPE(Integer damage) {
        // Here is the magic
        int result = (getOriginalDamage() & 0x0F) /*+ (damage & 0x??)*/; //missing facing data offset, 
        return result;
    }

    @Override
    public Integer translateToPC(Integer damage) {
        // Here too
        return damage;
    }
}
