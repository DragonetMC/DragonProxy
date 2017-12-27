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
public class BedDataTranslator extends IItemDataTranslator {

    //https://minecraft.gamepedia.com/Trapdoor
    
    @Override
    public Integer translateToPE(Integer damage) {
        // Here is the magic
//        System.out.println("INPUT " + damage);
        int facing = damage & 0x03;
        boolean headOccupied = (damage & 0x04) > 0; // head bed occupied
        boolean footOccupied = (damage & 0x08) > 0; // foot bed occupied
//        System.out.println("Bed facing " + facing + (headOccupied ? " headOccupied" : "") + (footOccupied ? " footOccupied" : ""));
//        facing = translateFacing(facing);
        return facing + (headOccupied ? 0x04 : 0x00) + (footOccupied ? 0x08 : 0x00);
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
            input = 0;
        else if (input == 1) // EAST
            input = 1;
        else if (input == 2) // SOUTH
            input = 3;
        else if (input == 3) // WEST
            input = 3;
        return input;
    }

}
