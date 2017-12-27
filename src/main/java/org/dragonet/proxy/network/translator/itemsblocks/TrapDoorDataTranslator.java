/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.proxy.network.translator.itemsblocks;

import org.dragonet.proxy.network.translator.ItemBlockTranslator;

/**
 *
 * @author vincent
 */
public class TrapDoorDataTranslator extends IItemDataTranslator {

    //https://minecraft.gamepedia.com/Trapdoor
    
    @Override
    public Integer translateToPE(Integer damage) {
        // Here is the magic
        int facing = damage & 0x03;
        boolean open = (damage & 0x04) != 0;
        boolean halfTop = (damage & 0x08) != 0;
        return translateFacing(facing) + (halfTop ? 0x04 : 0x00) + (open ? 0x08 : 0x00); // invert half and state
    }

    @Override
    public Integer translateToPC(Integer damage) {
        // Here too
        return damage;
    }
    
    private int translateFacing(int input)
    {
        // translate facing
        if (input == 0) // NORTH
            input = 3;
        else if (input == 1) // EAST
            input = 2;
        else if (input == 2) // SOUTH
            input = 1;
        else if (input == 3) // WEST
            input = 0;
        return input;
    }

}
