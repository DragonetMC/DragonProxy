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

import com.github.steveice10.mc.protocol.data.MagicValues;
import java.util.HashMap;
import java.util.Map;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import java.util.List;
import org.dragonet.proxy.data.nbt.tag.ListTag;

import org.dragonet.proxy.protocol.type.Slot;

public class ItemBlockTranslator {

    public static final int UNSUPPORTED_BLOCK_ID = 248;
    public static final String DRAGONET_COMPOUND = "DragonetNBT";
    public static final Map<Integer, ItemEntry> PC_TO_PE_OVERRIDE = new HashMap<>();
    public static final Map<Integer, ItemEntry> PE_TO_PC_OVERRIDE = new HashMap<>();
    public static final Map<Integer, String> NAME_OVERRIDES = new HashMap<>();

    static {
        toPEOverride(36, 248); //unkown block
        swap(125, 157); // double_wooden_slab
        swap(126, 158); // wooden_slab
        swap(157, 126); //activator_rail
        swap(198, 208); //end_rod
        swap(199, 240); //chorus_plant
        swap(202, 201); //purpur_pillar -> purpur_block
        swap(208, 198); //grass_path
        swap(210, 188); //repeating_command_block
        swap(211, 189); //chain_command_block
        swap(95, 241); //stained_glass
        swap(158, 125); //dropper
        swap(160, 248); //stained_glass_pane
        swap(166, 95); //barrier -> invisiblebedrock
        swap(188, new ItemEntry(85, 1)); //spruce_fence
        swap(189, new ItemEntry(85, 2)); //birch_fence
        swap(190, new ItemEntry(85, 3)); //jungle_fence
        swap(191, new ItemEntry(85, 5)); //dark_oak_fence
        swap(192, new ItemEntry(85, 4)); //acacia_fence
        swap(203, 202); //purpur_stairs
        swap(205, 248); //purpur_slab -> unknown

        //shulker_box
        swap(219, new ItemEntry(218, 0)); //white
        swap(220, new ItemEntry(218, 1)); //orange
        swap(221, new ItemEntry(218, 2)); //magenta
        swap(222, new ItemEntry(218, 3)); //light blue
        swap(223, new ItemEntry(218, 4)); //yellow
        swap(224, new ItemEntry(218, 5)); //lime
        swap(225, new ItemEntry(218, 6)); //pink
        swap(226, new ItemEntry(218, 7)); //gray
        swap(227, new ItemEntry(218, 8)); //light gray
        swap(228, new ItemEntry(218, 9)); //cyan
        swap(229, new ItemEntry(218, 10)); //purple
        swap(230, new ItemEntry(218, 11)); //blue
        swap(231, new ItemEntry(218, 12)); //brown
        swap(232, new ItemEntry(218, 13)); //green
        swap(233, new ItemEntry(218, 14)); //red
        swap(234, new ItemEntry(218, 15)); //black

        //glazed terracota
        swap(235, 220); //white
        swap(236, 221); //orange
        swap(237, 222); //magenta
        swap(238, 223); //light blue
        swap(239, 224); //yellow
        swap(240, 225); //lime
        swap(241, 226); //pink
        swap(242, 227); //gray
        swap(243, 228); //light gray
        swap(244, 229); //cyan
        swap(245, 230); //purple
        swap(246, 231); //blue
        swap(247, 232); //brown
        swap(248, 233); //green
        swap(249, 234); //red
        swap(250, 235); //black

        swap(251, 236); //concrete
        swap(252, 237); //concretepowder

        swap(218, 251); //observer
        swap(207, 244); //beetroots
        swap(255, 252); //structure_block

        //TODO: replace podzol
    }

    public static ItemEntry translateToPE(int pcItemBlockId, int damage) {
        ItemEntry entry = new ItemEntry(pcItemBlockId, damage);
        
        //here translate item data value (color / facing ....) need another translator items specific

        if (!PC_TO_PE_OVERRIDE.containsKey(pcItemBlockId)) {
            return entry;
        }
        entry = PC_TO_PE_OVERRIDE.get(pcItemBlockId);
        if (entry.damage == null) {
            entry.damage = damage;
        }

        if (pcItemBlockId >= 255 && entry.id == UNSUPPORTED_BLOCK_ID) {
            entry.id = UNSUPPORTED_BLOCK_ID; // Unsupported item becomes air
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

    public static Slot translateSlotToPE(ItemStack item) {
        if (item == null || item.getId() == 0) {
            return null;
        }
        Slot slot = new Slot();

        ItemEntry entry = translateToPE(item.getId(), item.getData());
        slot.id = entry.id;
        slot.damage = entry.damage != null ? entry.damage : item.getData();
        slot.count = (item.getAmount() & 0xff);
        org.dragonet.proxy.data.nbt.tag.CompoundTag tag = new org.dragonet.proxy.data.nbt.tag.CompoundTag();
        tag.putShort("id", item.getId());
        tag.putShort("amount", item.getAmount());
        tag.putShort("data", item.getData());
        org.dragonet.proxy.data.nbt.tag.CompoundTag rootTag = new org.dragonet.proxy.data.nbt.tag.CompoundTag();
        rootTag.put(DRAGONET_COMPOUND, tag);
        slot.tag = rootTag;
        translateRawNBT(item.getId(), item.getNBT(), slot.tag);
        return slot;
    }

    @SuppressWarnings("unchecked")
    public static org.dragonet.proxy.data.nbt.tag.CompoundTag translateRawNBT(int id, Tag pcTag, org.dragonet.proxy.data.nbt.tag.CompoundTag target) {
        if (pcTag != null) {
            String name = pcTag.getName() != null ? pcTag.getName() : "";
            if (target == null) {
                target = new org.dragonet.proxy.data.nbt.tag.CompoundTag(name);
            }
            switch (pcTag.getClass().getSimpleName()) {
                case "ByteArrayTag":
                    target.putByteArray(name, (byte[]) pcTag.getValue());
                    break;
                case "ByteTag":
                    target.putByte(name, (byte) pcTag.getValue());
                    break;
                case "DoubleTag":
                    target.putDouble(name, (double) pcTag.getValue());
                    break;
                case "FloatTag":
                    target.putFloat(name, (float) pcTag.getValue());
                    break;
                case "IntArrayTag":
                    target.putIntArray(name, (int[]) pcTag.getValue());
                    break;
                case "IntTag":
                    target.putInt(name, (int) pcTag.getValue());
                    break;
                case "LongTag":
                    target.putLong(name, (long) pcTag.getValue());
                    break;
                case "ShortTag":
                    target.putShort(name, (short) pcTag.getValue());
                    break;
                case "StringTag":
                    target.putString(name, (String) pcTag.getValue());
                    break;
                case "CompoundTag":
                    for (String subName : ((CompoundTag) pcTag).getValue().keySet()) {
                        translateRawNBT(0, ((CompoundTag) pcTag).getValue().get(subName), target);
                    }
                    break;
                case "ListTag":
                    ListTag listTag = new ListTag();
                    for (Tag subTag : (List<Tag>) pcTag.getValue()) {
                        listTag.add(translateRawNBT(0, subTag, new org.dragonet.proxy.data.nbt.tag.CompoundTag()));
                    }
                    target.putList(listTag);
                    break;
                default:
                    System.out.println("TAG not implemented : " + pcTag.getClass().getSimpleName());
                    break;
            }
        }
        return target;
    }

    //WIP
    public static org.dragonet.proxy.data.nbt.tag.CompoundTag translateBlockEntityToPE(com.github.steveice10.opennbt.tag.builtin.CompoundTag input) {
        if (input == null) {
            return null;
        }
        org.dragonet.proxy.data.nbt.tag.CompoundTag output = translateRawNBT(0, input, null);
        if (output.contains("id")) {
            switch (output.getString("id")) {
                case "minecraft:bed":
                    output.putString("id", "Bed");
                    output.putByte("color", output.getInt("color")); //TODO check colors
                    break;
                case "minecraft:chest":
                    output.putString("id", "Chest");
                    break;
                case "minecraft:ender_chest":
                    output.putString("id", "EnderChest");
                    break;
                case "minecraft:command_block":
                    output.putString("id", "CommandBlock");
                    break;
                case "minecraft:sign":
                    output.putString("id", "Sign");
                    break;
                case "minecraft:flower_pot":
                    output.putString("id", "FlowerPot");
                    break;
                case "minecraft:hopper":
                    output.putString("id", "Hopper");
                    break;
                case "minecraft:dropper":
                    output.putString("id", "Dropper");
                    break;
                case "minecraft:dispenser":
                    output.putString("id", "Dispenser");
                    break;
                case "minecraft:daylight_detector":
                    output.putString("id", "DaylightDetector");
                    break;
                case "minecraft:shulker_box":
                    output.putString("id", "ShulkerBox");
                    break;
                case "minecraft:furnace":
                    output.putString("id", "Furnace");
                    break;
                case "minecraft:structure_block":
                    output.putString("id", "StructureBlock");
                    break;
                case "minecraft:end_gateway":
                    output.putString("id", "EndGateway");
                    break;
                case "minecraft:beacon":
                    output.putString("id", "Beacon");
                    break;
                case "minecraft:end_portal":
                    output.putString("id", "EndPortal");
                    break;
                case "minecraft:mob_spawner":
                    output.putString("id", "MobSpawner");
                    break;
                case "minecraft:skull":
                    output.putString("id", "Skull");
                    break;
                case "minecraft:banner":
                    output.putString("id", "Banner");
                    break;
                case "minecraft:comparator":
                    output.putString("id", "Comparator");
                    break;
                case "minecraft:item_frame":
                    output.putString("id", "ItemFrame");
                    break;
                case "minecraft:jukebox":
                    output.putString("id", "Jukebox");
                    break;
                case "minecraft:piston":
                    output.putString("id", "PistonArm");
                    break;
            }
        }
        output.putBoolean("isMovable", false);
        return output;
    }

    public static ItemStack translateToPC(Slot slot) {
        ItemStack item;
        org.dragonet.proxy.data.nbt.tag.CompoundTag tag = slot.tag;
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

    public static BlockFace translateToPC(int face) {
        return BlockFace.values()[Math.abs(face % 6)];
    }

    private static void swap(int pcId, int peId) {
        PC_TO_PE_OVERRIDE.put(pcId, new ItemEntry(peId));
        PE_TO_PC_OVERRIDE.put(peId, new ItemEntry(pcId));
    }

    private static void swap(int pcId, ItemEntry toPe) {
        PC_TO_PE_OVERRIDE.put(pcId, toPe);
        PE_TO_PC_OVERRIDE.put(toPe.id, new ItemEntry(pcId));
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
