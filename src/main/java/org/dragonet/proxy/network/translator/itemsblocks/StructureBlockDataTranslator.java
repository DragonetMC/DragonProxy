/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.proxy.network.translator.itemsblocks;

/**
 * @author vincent
 */
public class StructureBlockDataTranslator extends IItemDataTranslator {

    //https://minecraft.gamepedia.com/Structure_Block

    @Override
    public Integer translateToPE(Integer damage) {
        // Here is the magic
        int variant = damage & 0x03;
        variant = translateVariant(variant);
        return variant;
    }

    @Override
    public Integer translateToPC(Integer damage) {
        // Here too
        return damage;
    }

    private int translateVariant(int input) {
        if (input == 0) // Save
            input = 2;
        else if (input == 1) // Load
            input = 3;
        else if (input == 2) // Corner
            input = 0;
        else if (input == 3) // Data
            input = 1;
        return input;
    }

}
