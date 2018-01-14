/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.proxy.network.translator.itemsblocks;

import org.dragonet.common.data.itemsblocks.IItemDataTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;

/**
 * @author vincent
 */
public class EndRodDataTranslator extends IItemDataTranslator {

    @Override
    public Integer translateToPE(Integer damage) {
        // Here is the magic
        return ItemBlockTranslator.invertVerticalFacing(damage);
    }

    @Override
    public Integer translateToPC(Integer damage) {
        // Here too
        return damage;
    }

}
