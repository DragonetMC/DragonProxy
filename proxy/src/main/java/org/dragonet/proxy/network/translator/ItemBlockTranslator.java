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
	public static final int UNSUPPORTED_BLOCK_ID = 248;
	public static final String DRAGONET_COMPOUND = "DragonetNBT";
	public static final Map<Integer, Integer> PC_TO_PE_OVERRIDE = new HashMap<>();
	public static final Map<Integer, Integer> PE_TO_PC_OVERRIDE = new HashMap<>();
	public static final Map<Integer, String> NAME_OVERRIDES = new HashMap<>();

	static {
		onewayOverride(95, 241); // stained_glass
		onewayOverride(125, 157); // double_wooden_slab
		onewayOverride(126, 158); // wooden_slab
		onewayOverride(157, 28); //detector_rail
		onewayOverride(166, 95); //barrier -> invisiblebedrock
		onewayOverride(168, 248); //prismarine -> update_block
		onewayOverride(169, 248); //prismarine_bricks -> update_block
		onewayOverride(188, 85); //spruce_fence -> fence
		onewayOverride(189, 85); //birch_fence ->fence
		onewayOverride(190, 85); //jungle_fence -> fence
		onewayOverride(191, 85); //jungle_fence -> fence
		onewayOverride(192, 85); //acacia_fence -> fence
		onewayOverride(198, 208); //end_rod
		onewayOverride(199, 240); //chorus_plant
		onewayOverride(202, 201); //purpur_pillar -> purpur_block
		onewayOverride(203, 202); //purpur_stairs
		onewayOverride(208, 198); //grass_path
		onewayOverride(218, 251); //observer_block
		onewayOverride(210, 188); //repeated command block
		onewayOverride(211, 189); //chained command block
		onewayOverride(235, 220); //white_glazed_terracotta
		onewayOverride(236, 221); //orange_glazed_terracotta
		onewayOverride(237, 222); //magenta_glazed_terracotta
		onewayOverride(238, 223); //light_blue_glazed_terracotta
		onewayOverride(239, 224); //yellow_glazed_terracotta
		onewayOverride(240, 225); //lime_glazed_terracotta
		onewayOverride(241, 226); //pink_glazed_terracotta
		onewayOverride(242, 227); //gray_glazed_terracotta
		onewayOverride(243, 228); //light_gray_glazed_terracotta
		onewayOverride(244, 229); //cyan_glazed_terracotta
		onewayOverride(245, 219); //purple_glazed_terracotta
		onewayOverride(246, 231); //blue_glazed_terracotta
		onewayOverride(247, 232); //brown_glazed_terracotta
		onewayOverride(248, 233); //green_glazed_terracotta
		onewayOverride(249, 234); //red_glazed_terracotta
		onewayOverride(250, 235); //black_glazed_terracotta
		onewayOverride(251, 236); //concrete
		onewayOverride(252, 236); //concretepowder -> concrete
		onewayOverride(255, 252); //structure_block
	}

	// constructor
	public ItemBlockTranslator() {

	}

	// public
	// Query handler
	public static int translateToPE(int pcItemBlockId) {
		if (!PC_TO_PE_OVERRIDE.containsKey(pcItemBlockId)) {
			return pcItemBlockId;
		}
		int ret = PC_TO_PE_OVERRIDE.get(pcItemBlockId);
		if (pcItemBlockId >= 255 && ret == UNSUPPORTED_BLOCK_ID) {
			ret = 0; // Unsupported item becomes air
		}
		return ret;
	}

	public static int translateToPC(int peItemBlockId) {
		if (!PE_TO_PC_OVERRIDE.containsKey(peItemBlockId)) {
			return peItemBlockId;
		}
		int ret = PE_TO_PC_OVERRIDE.get(peItemBlockId);
		return ret;
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
		inv.id = translateToPE(item.getId());
		inv.damage = item.getData();
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
			item = new ItemStack(translateToPC(slot.id), slot.count, slot.damage);
		}

		return item;
	}

	// private
	private static void swap(int pcId, int peId) {
		PC_TO_PE_OVERRIDE.put(pcId, peId);
		PE_TO_PC_OVERRIDE.put(peId, pcId);
	}

	private static void onewayOverride(int fromPc, int toPe, String nameOverride) {
		onewayOverride(fromPc, toPe);
		if (nameOverride != null) {
			NAME_OVERRIDES.put(fromPc, nameOverride);
		}
	}

	private static void onewayOverride(int fromPc, int toPe) {
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
}
