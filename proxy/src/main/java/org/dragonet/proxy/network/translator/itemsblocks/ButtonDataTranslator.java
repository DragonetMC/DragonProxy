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
public class ButtonDataTranslator extends IItemDataTranslator {

    @Override
    public Integer translateToPE(Integer damage) {
        // Here is the magic
        int facing = damage > 5 ? damage - 0x08 : damage;
        boolean activated = (damage & 0x08) > 0;
        facing = ItemBlockTranslator.translateFacing(facing);
        return facing + (activated ? 0x08 : 0x00);
    }

    @Override
    public Integer translateToPC(Integer damage) {
        // Here too
        return damage;
    }

}
