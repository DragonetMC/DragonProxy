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
package org.dragonet.proxy.network.translator;

import java.util.HashMap;
import java.util.Map;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;

import org.dragonet.proxy.protocol.type.Slot;

public class ItemBlockTranslator {
	// vars
	public static final int UNSUPPORTED_BLOCK_ID = 165;
	public static final String DRAGONET_COMPOUND = "DragonetNBT";
	public static final Map<Integer, ItemEntry> PC_TO_PE_OVERRIDE = new HashMap<>();
	public static final Map<Integer, ItemEntry> PE_TO_PC_OVERRIDE = new HashMap<>();
	public static final Map<Integer, String> NAME_OVERRIDES = new HashMap<>();

	static {
		swap(125, 157); // Double Slab <-> Activator Rail
		toPEOverride(126, 158); // Slab <-> NULL
		toPEOverride(157, 126); //activator rail
		toPEOverride(198, 208); //end rod
		toPEOverride(199, 240); //chorus plant
		toPEOverride(202, 201); //purpur pillar -> purpu block
		toPEOverride(208, 198); //grass path
		toPEOverride(210, 188); //repeating command block
		toPEOverride(211, 189); //chain command block
		toPEOverride(95, 241); // Stained Glass
		toPEOverride(36, 248);
		toPEOverride(166, 95); //barrier -> invisible bedrock
		toPEOverride(188, new ItemEntry(85, 1));
		toPEOverride(189, new ItemEntry(85, 2));
		toPEOverride(190, new ItemEntry(85, 3));
		toPEOverride(191, new ItemEntry(85, 5));
		toPEOverride(192, new ItemEntry(85, 4));
		toPEOverride(203, 202); //purpur stairs
		toPEOverride(205, 248); //purpur slab -> unknown

		toPEOverride(219, new ItemEntry(218, 0)); //shulker boxes
		toPEOverride(220, new ItemEntry(218,1));
		toPEOverride(221, new ItemEntry(218, 2));
		toPEOverride(222, new ItemEntry(218, 3));
		toPEOverride(223, new ItemEntry(218, 4));
		toPEOverride(224, new ItemEntry(218, 5));
		toPEOverride(225, new ItemEntry(218, 6));
		toPEOverride(226, new ItemEntry(218, 7));
		toPEOverride(227, new ItemEntry(218, 8));
		toPEOverride(228, new ItemEntry(218, 9));
		toPEOverride(229, new ItemEntry(218, 10));
		toPEOverride(230, new ItemEntry(218, 11));
		toPEOverride(231, new ItemEntry(218, 12));
		toPEOverride(232, new ItemEntry(218, 13));
		toPEOverride(233, new ItemEntry(218, 14));
		toPEOverride(234, new ItemEntry(218, 15));

		toPEOverride(235, 220); //glazed terracota
		toPEOverride(236, 221);
		toPEOverride(237, 222);
		toPEOverride(238, 223);
		toPEOverride(239, 224);
		toPEOverride(240, 225);
		toPEOverride(241, 226);
		toPEOverride(242, 227);
		toPEOverride(243, 228);
		toPEOverride(244, 229);
		toPEOverride(245, 230);
		toPEOverride(246, 231);
		toPEOverride(247, 232);
		toPEOverride(248, 233);
		toPEOverride(249, 234);
		toPEOverride(250, 235);

		toPEOverride(251, 236); //concrete
		toPEOverride(252, 237); //concrete powder

		toPEOverride(218, 251); //observer
		toPEOverride(255, 252); //structure block

		//TODO: replace podzol
	}

	// constructor
	public ItemBlockTranslator() {

	}

	// public
	// Query handler
	public static ItemEntry translateToPE(int pcItemBlockId, int damage) {
		ItemEntry entry = new ItemEntry(pcItemBlockId, damage);

		if (!PC_TO_PE_OVERRIDE.containsKey(pcItemBlockId)) {
			return entry;
		}
		entry = PC_TO_PE_OVERRIDE.get(pcItemBlockId);
		if(entry.damage == null) {
			entry.damage = damage;
		}

		if (pcItemBlockId >= 255 && entry.id == UNSUPPORTED_BLOCK_ID) {
			entry.id = 0; // Unsupported item becomes air
			entry.damage = 0;
		}
		return entry;
	}

	public static ItemEntry translateToPC(int peItemBlockId, int damage) {
		ItemEntry entry = new ItemEntry(peItemBlockId, damage);

		if (!PE_TO_PC_OVERRIDE.containsKey(peItemBlockId)) {
			return entry;
		}

		entry = PE_TO_PC_OVERRIDE.get(peItemBlockId);

		return entry;
	}

	@SuppressWarnings("unchecked")
	public static org.dragonet.proxy.nbt.tag.CompoundTag translateNBT(int id, CompoundTag pcTag, org.dragonet.proxy.nbt.tag.CompoundTag target) {
		if (pcTag != null && pcTag.contains("display")) {
			CompoundTag pcDisplay = pcTag.get("display");
			org.dragonet.proxy.nbt.tag.CompoundTag peDisplay;
			if(target.contains("display")) {
				peDisplay = target.getCompound("display");
			} else {
				peDisplay = new org.dragonet.proxy.nbt.tag.CompoundTag();
				target.put("display", peDisplay);
			}
			if (pcDisplay.contains("Name")) {
				peDisplay.put("Name", new org.dragonet.proxy.nbt.tag.StringTag("Name",
						((StringTag)pcDisplay.get("Name")).getValue()));
			}
		} else {
			if (NAME_OVERRIDES.containsKey(id)) {
				org.dragonet.proxy.nbt.tag.CompoundTag peDisplay;
				if(target.contains("display")) {
					peDisplay = target.getCompound("display");
				} else {
					peDisplay = new org.dragonet.proxy.nbt.tag.CompoundTag();
					target.put("display", peDisplay);
				}
				target.put("display", peDisplay);
				peDisplay.put("Name", new org.dragonet.proxy.nbt.tag.StringTag("Name", NAME_OVERRIDES.get(id)));
			}
		}
		return target;
	}

	public static Slot translateSlotToPE(ItemStack item) {
		if (item == null || item.getId() == 0)
			return null;
		Slot inv = new Slot();

		ItemEntry entry = translateToPE(item.getId(), item.getData());
		inv.id = entry.id;
		inv.damage = entry.damage != null ? entry.damage : item.getData();
		inv.count = (item.getAmount() & 0xff);
		org.dragonet.proxy.nbt.tag.CompoundTag tag = new org.dragonet.proxy.nbt.tag.CompoundTag();
		tag.putShort("id", item.getId());
		tag.putShort("amount", item.getAmount());
		tag.putShort("data", item.getData());
		org.dragonet.proxy.nbt.tag.CompoundTag rootTag = new org.dragonet.proxy.nbt.tag.CompoundTag();
		rootTag.put(DRAGONET_COMPOUND, tag);
		inv.tag = rootTag;
		translateNBT(item.getId(), item.getNBT(), inv.tag);
		return inv;
	}

	public static ItemStack translateToPC(Slot slot) {
		ItemStack item;
		org.dragonet.proxy.nbt.tag.CompoundTag tag = slot.tag;
		if (tag != null && tag.contains(DRAGONET_COMPOUND)) {
			item = new ItemStack(tag.getCompound(DRAGONET_COMPOUND).getShort("id"),
					tag.getCompound(DRAGONET_COMPOUND).getShort("amount"),
					tag.getCompound(DRAGONET_COMPOUND).getShort("data"));
		} else {
			ItemEntry entry = translateToPC(slot.id, slot.damage);
			item = new ItemStack(entry.id, slot.count, entry.damage != null ? entry.damage : slot.damage);
		}

		return item;
	}

	// private
	private static void swap(int pcId, int peId) {
		PC_TO_PE_OVERRIDE.put(pcId, new ItemEntry(peId));
		PE_TO_PC_OVERRIDE.put(peId, new ItemEntry(pcId));
	}

	private static void toPEOverride(int fromPc, int toPe, String nameOverride) {
		toPEOverride(fromPc, toPe);
		if (nameOverride != null) {
			NAME_OVERRIDES.put(fromPc, nameOverride);
		}
	}

	private static void toPEOverride(int fromPc, int toPe) {
		toPEOverride(fromPc, new ItemEntry(toPe, null));
	}

	private static void toPEOverride(int fromPc, ItemEntry toPe) {
		PC_TO_PE_OVERRIDE.put(fromPc, toPe);
	}

	public static CompoundTag newTileTag(String id, int x, int y, int z) {
		CompoundTag t = new CompoundTag(null);
		t.put(new StringTag("id", id));
		t.put(new IntTag("x", x));
		t.put(new IntTag("y", y));
		t.put(new IntTag("z", z));
		return t;
	}

	public static class ItemEntry {

		public Integer id;
		public Integer damage;

		public ItemEntry(Integer id) {
			this(id, null);
		}

		public ItemEntry(Integer id, Integer damage) {
			this.id = id;
			this.damage = damage;
		}
	}
}
