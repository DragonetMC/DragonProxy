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
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import java.util.List;
import org.dragonet.proxy.data.itemsblocks.ItemEntry;
import org.dragonet.proxy.data.nbt.tag.ListTag;
import org.dragonet.proxy.network.translator.itemsblocks.*;

import org.dragonet.proxy.protocol.type.Slot;

public class ItemBlockTranslator {

    public static final int UNSUPPORTED_BLOCK_ID = 248;
    public static final String DRAGONET_COMPOUND = "DragonetNBT";
    public static final Map<Integer, ItemEntry> PC_TO_PE_OVERRIDE = new HashMap<>();
    public static final Map<Integer, ItemEntry> PE_TO_PC_OVERRIDE = new HashMap<>();
    public static final Map<Integer, String> NAME_OVERRIDES = new HashMap<>();

    static {
        //BLOCKS
        toPEOverride(36, 248); //unkown block
        translateData(26, new BedDataTranslator()); //bed
        translateData(77, new ButtonDataTranslator()); //stone_button
        translateData(143, new ButtonDataTranslator()); //wooden_button
        translateData(96, new TrapDoorDataTranslator()); //trapdoor
        translateData(167, new TrapDoorDataTranslator()); //iron_trapdoor
        swap(125, 157); // double_wooden_slab
        swap(126, 158); // wooden_slab
        swap(157, 126); //activator_rail
        swap(198, 208, new EndRodDataTranslator()); //end_rod
        swap(199, 240); //chorus_plant
        swap(202, 201); //purpur_pillar -> purpur_block
        swap(208, 198); //grass_path
        swap(210, 188); //repeating_command_block
        swap(211, 189); //chain_command_block
        swap(95, 241); //stained_glass
        swap(158, 125); //dropper
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

        //ITEMS
        toPEOverride(343, 342); //minecart_furnace (unavailable on PE)
        swap(416, 425); //armor_stand horsearmorleather
        toPCOverride(425, 417); //horsearmorleather horsearmoriron (unavailable on PC)

        swap(443, 444); //elytra

        swap(444, new ItemEntry(333, 1)); //spruce_boat
        swap(445, new ItemEntry(333, 2)); //birch_boat
        swap(446, new ItemEntry(333, 3)); //jungle_boat
        swap(447, new ItemEntry(333, 4)); //acacia_boat
        swap(448, new ItemEntry(333, 5)); //dark_oak_boat

        swap(449, 450); //totem
        swap(450, 445); //shulker_shell

        swap(2256, 500); //record_13
        swap(2257, 501); //record_cat
        swap(2258, 502); //record_blocks
        swap(2259, 503); //record_chirp
        swap(2260, 504); //record_far
        swap(2261, 505); //record_far
        swap(2262, 506); //record_mellohi
        swap(2263, 507); //record_stal
        swap(2264, 508); //record_strad
        swap(2265, 509); //record_ward
        swap(2265, 510); //record_11
        swap(2265, 511); //record_wait

        //TODO: replace podzol
    }

    public static ItemEntry translateToPE(int pcId, int damage) {
        // see https://minecraft.gamepedia.com/Block_states
        if (!PC_TO_PE_OVERRIDE.containsKey(pcId))
            return new ItemEntry(pcId, damage);
        return PC_TO_PE_OVERRIDE.get(pcId).setDamage(damage);
    }

    public static ItemEntry translateToPC(int peId, int damage) {
        if (!PE_TO_PC_OVERRIDE.containsKey(peId))
            return new ItemEntry(peId, damage);
        return PE_TO_PC_OVERRIDE.get(peId).setDamage(damage);
    }

    public static Slot translateSlotToPE(ItemStack item) {
        if (item == null || item.getId() == 0)
            return null;
        Slot slot = new Slot();

        ItemEntry entry = translateToPE(item.getId(), item.getData());
        slot.id = entry.getId();
        slot.damage = entry.getPEDamage() != null ? entry.getPEDamage() : item.getData();
        slot.count = (item.getAmount() & 0xff);
        slot.tag = translateRawNBT(item.getId(), item.getNBT(), null);
        return slot;
    }

    @SuppressWarnings("unchecked")
    public static org.dragonet.proxy.data.nbt.tag.CompoundTag translateRawNBT(int id, Tag pcTag, org.dragonet.proxy.data.nbt.tag.CompoundTag target) {
        if (pcTag != null) {
            String name = pcTag.getName() != null ? pcTag.getName() : "";
            if (target == null)
                target = new org.dragonet.proxy.data.nbt.tag.CompoundTag(name);
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
                    for (String subName : ((CompoundTag) pcTag).getValue().keySet())
                        translateRawNBT(0, ((CompoundTag) pcTag).getValue().get(subName), target);
                    break;
                case "ListTag":
                    ListTag listTag = new ListTag();
                    for (Tag subTag : (List<Tag>) pcTag.getValue())
                        listTag.add(translateRawNBT(0, subTag, new org.dragonet.proxy.data.nbt.tag.CompoundTag()));
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
        if (input == null)
            return null;
        org.dragonet.proxy.data.nbt.tag.CompoundTag output = translateRawNBT(0, input, null);
        if (output.contains("id"))
            switch (output.getString("id")) {
                case "minecraft:bed":
                    output.putString("id", "Bed");
                    output.putByte("color", 0); //TODO check colors
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
                    if (!output.contains("Text"))
                        output.putString("Text",
                                Message.fromString(output.getString("Text1")).getFullText()
                                + "\n" + Message.fromString(output.getString("Text2")).getFullText()
                                + "\n" + Message.fromString(output.getString("Text3")).getFullText()
                                + "\n" + Message.fromString(output.getString("Text4")).getFullText());
                    output.remove("Text1");
                    output.remove("Text2");
                    output.remove("Text3");
                    output.remove("Text4");
                    break;
                case "minecraft:flower_pot":
                    output.putString("id", "FlowerPot");
                    output.putInt("data", output.getInt("Data"));
                    output.remove("Data");
                    output.putShort("item", 0);
                    output.remove("Item");
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
//        if (output.getString("id") == "ShulkerBox" || output.getString("id") == "Bed" || output.getString("id") == "Chest")
//            System.out.println("translateBlockEntityToPE " + output.toString());
        return output;
    }

    public static ItemStack translateToPC(Slot slot) {
        ItemEntry entry = translateToPC(slot.id, slot.damage);
        return new ItemStack(entry.getId(), slot.count, entry.getPCDamage() != null ? entry.getPCDamage() : slot.damage); //TODO NBT
    }

    private static void swap(int pcId, int peId) {
        swap(pcId, peId, null);
    }

    private static void swap(int pcId, int peId, IItemDataTranslator translator) {
        PC_TO_PE_OVERRIDE.put(pcId, new ItemEntry(peId, null, translator));
        PE_TO_PC_OVERRIDE.put(peId, new ItemEntry(pcId, null, translator));
    }

    private static void translateData(int id, IItemDataTranslator translator) {
        PC_TO_PE_OVERRIDE.put(id, new ItemEntry(id, null, translator));
        PE_TO_PC_OVERRIDE.put(id, new ItemEntry(id, null, translator));
    }

    private static void swap(int pcId, ItemEntry toPe) {
        PC_TO_PE_OVERRIDE.put(pcId, toPe);
        PE_TO_PC_OVERRIDE.put(toPe.getId(), new ItemEntry(pcId));
    }

    private static void toPEOverride(int fromPc, int toPe, String nameOverride) {
        toPEOverride(fromPc, toPe);
        if (nameOverride != null)
            NAME_OVERRIDES.put(fromPc, nameOverride);
    }

    private static void toPEOverride(int fromPc, int toPe) {
        toPEOverride(fromPc, new ItemEntry(toPe, null));
    }

    private static void toPEOverride(int fromPc, ItemEntry toPe) {
        PC_TO_PE_OVERRIDE.put(fromPc, toPe);
    }

    private static void toPCOverride(int fromPc, int toPc) {
        PE_TO_PC_OVERRIDE.put(fromPc, new ItemEntry(toPc));
    }

    private static void toPCOverride(int fromPc, ItemEntry toPe) {
        PE_TO_PC_OVERRIDE.put(fromPc, toPe);
    }

    public static CompoundTag newTileTag(String id, int x, int y, int z) {
        CompoundTag t = new CompoundTag(null);
        t.put(new StringTag("id", id));
        t.put(new IntTag("x", x));
        t.put(new IntTag("y", y));
        t.put(new IntTag("z", z));
        return t;
    }

    public static BlockFace translateToPC(int face) {
        return BlockFace.values()[Math.abs(face % 6)];
    }

    public static Integer translateFacing(int input) {
        // translate facing
        if (input == 0) // DOWN
            input = 0;
        else if (input == 1) //EAST
            input = 5;
        else if (input == 2) //WEST
            input = 4;
        else if (input == 3) //SOUTH
            input = 3;
        else if (input == 4) //NORTH
            input = 2;
        else if (input == 5) //UP
            input = 1;
        return input;
    }
    
    public static Integer invertVerticalFacing(int input) {
        // translate facing
        if (input == 5) //EAST
            input = 4;
        else if (input == 4) //WEST
            input = 5;
        else if (input == 3) //SOUTH
            input = 2;
        else if (input == 2) //NORTH
            input = 3;
        return input;
    }
}
