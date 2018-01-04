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
import org.dragonet.proxy.DragonProxy;

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
        translateData(44, new SlabDataTranslator()); //slab
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
        swap(255, 252, new StructureBlockDataTranslator()); //structure_block

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
        slot.tag = translateItemNBT(item.getId(), item.getNBT(), null);
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
                    output.putByte("color", output.getInt("color")); //TODO check colors
                    output.putByte("isMovable", 0x00);
                    break;
                case "minecraft:chest":
                    output.putString("id", "Chest");
                    break;
                case "minecraft:ender_chest":
                    output.putString("id", "EnderChest");
                    output.putByte("isMovable", 0x00);
                    break;
                case "minecraft:command_block":
                    output.putString("id", "CommandBlock");
                    break;
                case "minecraft:sign":
                    output.putString("id", "Sign");
                    if (output.contains("Text1") && output.contains("Text2") && output.contains("Text3") && output.contains("Text4"))
                    {
                        StringBuilder signText = new StringBuilder();
                        for (int line = 1; line <= 4; line++)
                        {
                            signText.append(MessageTranslator.translate(output.getString("Text" + line)));
                            if (line != 4)
                                signText.append("\n");
                            output.remove("Text" + line);
                        }
                        output.putString("Text", signText.toString());
                    }
                    break;
                case "minecraft:flower_pot":
                    output.putString("id", "FlowerPot");
                    output.putInt("data", output.getInt("Data"));
                    output.remove("Data");
                    FlowerPot item = FlowerPot.byName(output.getString("Item"));
                    if (item != null)
                    {
                        output.putShort("item", item.getId());
                    }
                    else
                        output.putShort("item", 0);
                        output.remove("Item");
                    output.putByte("isMovable", 0x00);
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
//                case "minecraft:structure_block":
//                    output.putString("id", "StructureBlock");
//                    break;
//                case "minecraft:end_gateway":
//                    output.putString("id", "EndGateway");
//                    break;
//                case "minecraft:beacon":
//                    output.putString("id", "Beacon");
//                    break;
                case "minecraft:end_portal":
                    output.putString("id", "EndPortal");
                    break;
//                case "minecraft:mob_spawner":
//                    output.putString("id", "MobSpawner");
//                    break;
                case "minecraft:skull":
                    output.putString("id", "Skull");
                    output.putByte("isMovable", 0x00);
                    break;
//                case "minecraft:banner":
//                    output.putString("id", "Banner");
//                    break;
                case "minecraft:comparator":
                    output.putString("id", "Comparator");
                    break;
//                case "minecraft:item_frame":
//                    output.putString("id", "ItemFrame");
//                    break;
                case "minecraft:jukebox":
                    output.putString("id", "Jukebox");
                    break;
//                case "minecraft:piston":
//                    output.putString("id", "PistonArm");
//                    break;
                case "minecraft:noteblock":
                    output.putString("id", "Noteblock");
                    break;
                case "minecraft:enchanting_table":
                    output.putString("id", "EnchantTable");
                    break;
                case "minecraft:brewing_stand":
                    output.putString("id", "BrewingStand");
                    break;
                default :
//                    DragonProxy.getInstance().getLogger().debug("Block entitiy not handled : " + output.getString("id") + " " + output.toString());
                    return null;
            }
//        if (output.getString("id") == "Sign") //debug
//            System.out.println("translateBlockEntityToPE " + output.toString());
        return output;
    }
    
    public static org.dragonet.proxy.data.nbt.tag.CompoundTag translateItemNBT(int id, Tag input, org.dragonet.proxy.data.nbt.tag.CompoundTag target) {
        if (input == null)
            return null;
        //do the magic
        org.dragonet.proxy.data.nbt.tag.CompoundTag output = translateRawNBT(id, input, target);
        // 298 leather_helmet       CompoundTag { {color=IntTag(color) { 7039851 }} }
        // 299 leather_chestplate
        // 300 leather_leggings
        // 301 leather_boots
        // 397 skull                CompoundTag { {=ListTag { [CompoundTag { {Signature=StringTag(Signature) { Dfpupzf34kyZaa52cSxbbJhrT2KQ+H3DXEz0Ivsws75/pK3RMglcQrT8MMvfcax79DPFsiVHLvO9TD7AH76Oev5+VxjpJKkx9vnSI1Dnl4cpQ/160cHkc1gJaJaVyQhG7x1epH7h1u87U1yiHLw07ri4YkyLk7Zqa4RgGrgNIOrpXgexJ6gepgb14kxO3y+C6mW/9QIKjjlyXXc1XVQc3kYkoWwHB1qatTzYmq4ZB0u50OyIMzcXR7CcrYelm0u+DAIYIcbhwmcpUE6sLkieNLCdlRB9Qi7Bs/tIKs5nVAD8TgEJEWVxxS7iGCirjcjZuk7GoTqleX0TMFvHXyQ6K86VxRafNT9u3znuiKGev40Wl3969/93HtVHvFisH9bdquW5r00zd49h3kQs8xa7gQ5oE3uZ2xWPzOTuHrjEiZu2U3MlT3bnTRuQGIPzDCsRHRseZPEKzObIgyN+UB+EUO2EEiPED42jiFv6j4QFFAYkqxahAbnGlXXJPZ0Vq2GmwPwlIBvxmPuMZT/U9ECEg+j2TsIE766eYCm4GBZwC46Q9061eSKzPMPPrrSapo7i30yeKPuL1M8SnXGTGPVpDEpr63yqmHOEu0HJwjoSSeyViP0nphXOdVGEhHja6MIqyUl6gB9pAaiIDKVWQpFNO6z000FPGw+GcaDT8rc1pdQ= }, Value=StringTag(Value) { eyJ0aW1lc3RhbXAiOjE1MTI5MjM1NTc4MjAsInByb2ZpbGVJZCI6ImVjNTYxNTM4ZjNmZDQ2MWRhZmY1MDg2YjIyMTU0YmNlIiwicHJvZmlsZU5hbWUiOiJBbGV4Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7fX0= }} }] }, Id=StringTag(Id) { ec561538-f3fd-461d-aff5-086b22154bce }, Name=StringTag(Name) { Alex }} }
        
        boolean handle = false;
        org.dragonet.proxy.data.nbt.tag.CompoundTag display = new org.dragonet.proxy.data.nbt.tag.CompoundTag();
        if (output.contains("Name")) {
            display.putString("name", output.getString("Name"));
//            System.out.println(output.getString("Name"));
            output.remove("Name");
            handle = true;
        }
        if (output.contains("Lore")) {
            display.putByteArray("Lore", output.getByteArray("Lore"));
            output.remove("Lore");
            handle = true;
        }
        
        if (!display.isEmpty())
            output.putCompound("display", display);

//        if (!handle)
//            DragonProxy.getInstance().getLogger().debug("Item NBT not handled : id " + id + ", NBT : " + output.toString());
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
    
    public enum FlowerPot {
        empty(0),
        red_flower(1),
        blue_orchid(2),
        allium(3),
        houstonia(4),
        red_tulip(5),
        orange_tulip(5),
        white_tulip(5),
        pink_tulip(5),
        oxeye_daisy(6),
        yellow_flower(7),
        sapling(8),
        spruce_sapling(8),
        birch_sapling(8),
        jungle_sapling(8),
        acacia_sapling(8),
        dark_oak_sapling(8),
        red_mushroom(9),
        brown_mushroom(10),
        deadbush(11),
        fern(12),
        cactus(13);
        
        private int id;
        
        private FlowerPot(int id)
        {
            
        }
        
        public int getId()
        {
            return this.id;
        }
        
        public static FlowerPot byName(String value)
        {
            for (FlowerPot entry : values())
            {
                if (("minecraft:" + entry.name()).equals(value))
                    return entry;
            }
//            DragonProxy.getInstance().getLogger().debug("FlowerPot item not found : " + value);
            return null;
        }
    }
}
