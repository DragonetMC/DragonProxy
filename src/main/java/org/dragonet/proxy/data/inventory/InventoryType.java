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
package org.dragonet.proxy.data.inventory;

public final class InventoryType {
	public static final class PEInventory {
		public static final byte CHEST = (byte) 0;
		public static final byte DOUBLE_CHEST = (byte) 0;
		public static final byte PLAYER = (byte) 0;
		public static final byte FURNACE = (byte) 2;
		public static final byte CRAFTING = (byte) 1;
		public static final byte WORKBENCH = (byte) 1;
		public static final byte STONECUTTER = (byte) 1;
		public static final byte BREWING_STAND = (byte) 5;
		public static final byte ANVIL = (byte) 6;
		public static final byte ENCHANT_TABLE = (byte) 4;

		public static final byte toPEInventory(byte bytePC, int slots) {
			switch (bytePC) {
			case PCInventory.CHEST:
				if (slots > 64) // Large
				{
					return PEInventory.DOUBLE_CHEST;
				} else {
					return PEInventory.CHEST;
				}
			case PCInventory.WORKBENCH:
				return PEInventory.WORKBENCH;
			case PCInventory.FURNANCE:
				return PEInventory.FURNACE;
			// case PCInventory.DISPENSER:
			// case PCInventory.ENCHANTING_TABLE:
			// case PCInventory.BREWING_STAND:
			// case PCInventory.NPC_TRADE:
			// case PCInventory.BEACON:
			// case PCInventory.ANVIL:
			// case PCInventory.HOPPER:
			// case PCInventory.DROPPER:
			// case PCInventory.HORSE:
			default:
				return (byte) 0xFF;
			}
		}

	}

	public static final class PCInventory {
		public static final byte CHEST = (byte) 0x0;
		public static final byte WORKBENCH = (byte) 0x1;
		public static final byte FURNANCE = (byte) 0x2;
		public static final byte DISPENSER = (byte) 0x3;
		public static final byte ENCHANTING_TABLE = (byte) 0x4;
		public static final byte BREWING_STAND = (byte) 0x5;
		public static final byte NPC_TRADE = (byte) 0x6;
		public static final byte BEACON = (byte) 0x7;
		public static final byte ANVIL = (byte) 0x8;
		public static final byte HOPPER = (byte) 0x9;
		public static final byte DROPPER = (byte) 0x10;
		public static final byte HORSE = (byte) 0xa;

		public static final byte fromString(String str) {
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

	public static final class SlotSize {
		public static final int CHEST = 27;
		public static final int DOUBLE_CHEST = 27 + 27;
		public static final int PLAYER = 36;
		public static final int FURNACE = 3;
		public static final int CRAFTING = 5;
		public static final int WORKBENCH = 10;
		public static final int STONECUTTER = 10;
		public static final int ANVIL = 3;
	}
}
