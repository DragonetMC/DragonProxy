/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details.
 *
 * @author The Dragonet Team
 */
package org.dragonet.inventory;

public final class InventoryType {

    public final static class PEInventory {

        public final static byte CHEST = (byte) 0;
        public final static byte DOUBLE_CHEST = (byte) 0;
        public final static byte PLAYER = (byte) 0;
        public final static byte FURNACE = (byte) 2;
        public final static byte CRAFTING = (byte) 1;
        public final static byte WORKBENCH = (byte) 1;
        public final static byte STONECUTTER = (byte) 1;
        public final static byte BREWING_STAND = (byte) 5;
        public final static byte ANVIL = (byte) 6;
        public final static byte ENCHANT_TABLE = (byte) 4;

        public final static byte toPEInventory(byte bytePC, int slots) {
            switch (bytePC) {
                case PCInventory.CHEST:
                    if (slots > 64) //Large
                    {
                        return PEInventory.DOUBLE_CHEST;
                    } else {
                        return PEInventory.CHEST;
                    }
                case PCInventory.WORKBENCH:
                    return PEInventory.WORKBENCH;
                case PCInventory.FURNANCE:
                    return PEInventory.FURNACE;
                //case PCInventory.DISPENSER:
                //case PCInventory.ENCHANTING_TABLE:
                //case PCInventory.BREWING_STAND:  
                //case PCInventory.NPC_TRADE:
                //case PCInventory.BEACON:
                //case PCInventory.ANVIL:
                //case PCInventory.HOPPER:
                //case PCInventory.DROPPER:
                //case PCInventory.HORSE:
                default:
                    return (byte) 0xFF;
            }
        }

    }

    public final static class PCInventory {

        public final static byte CHEST = (byte) 0x0;
        public final static byte WORKBENCH = (byte) 0x1;
        public final static byte FURNANCE = (byte) 0x2;
        public final static byte DISPENSER = (byte) 0x3;
        public final static byte ENCHANTING_TABLE = (byte) 0x4;
        public final static byte BREWING_STAND = (byte) 0x5;
        public final static byte NPC_TRADE = (byte) 0x6;
        public final static byte BEACON = (byte) 0x7;
        public final static byte ANVIL = (byte) 0x8;
        public final static byte HOPPER = (byte) 0x9;
        public final static byte DROPPER = (byte) 0x10;
        public final static byte HORSE = (byte) 0xa;

        public final static byte fromString(String str) {
            if (str.equals("minecraft:chest")) {
                return PCInventory.CHEST;
            } else if (str.equals("minecraft:crafting_table")) {
                return PCInventory.WORKBENCH;
            } else if (str.equals("minecraft:furnance")) {
                return PCInventory.FURNANCE;
            } else if (str.equals("minecraft:dispenser")) {
                return PCInventory.DISPENSER;
            } else if (str.equals("minecraft:enchanting_table")) {
                return PCInventory.ENCHANTING_TABLE;
            } else if (str.equals("minecraft:brewing_stand")) {
                return PCInventory.BREWING_STAND;
            } else if (str.equals("minecraft:villager")) {
                return PCInventory.NPC_TRADE;
            } else if (str.equals("minecraft:beacon")) {
                return PCInventory.BEACON;
            } else if (str.equals("minecraft:hopper")) {
                return PCInventory.HOPPER;
            } else if (str.equals("minecraft:dropper")) {
                return PCInventory.DROPPER;
            } else if (str.equals("EntityHorse")) {
                return PCInventory.HORSE;
            }
            return (byte) 0xFF;
        }
    }

    public final static class SlotSize {

        public final static int CHEST = 27;
        public final static int DOUBLE_CHEST = 27 + 27;
        public final static int PLAYER = 36;
        public final static int FURNACE = 3;
        public final static int CRAFTING = 5;
        public final static int WORKBENCH = 10;
        public final static int STONECUTTER = 10;
        public final static int ANVIL = 3;

    }
}
