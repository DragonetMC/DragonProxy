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
package org.dragonet.net.packet.minecraft;

import java.util.ArrayList;

public class CraftingDataPacket extends PEPacket {

    public final static int ENTRY_SHAPELESS = 0;
    public final static int ENTRY_SHAPED = 1;
    public final static int ENTRY_FURNACE = 2;
    public final static int ENTRY_FURNACE_DATA = 3;
    public final static int ENTRY_ENCHANT = 4;

    public ArrayList<Object> recipies;

    public boolean cleanRecipies;

    private int enchants;

    public CraftingDataPacket(boolean cleanRecipies) {
        this.cleanRecipies = cleanRecipies;
    }

    @Override
    public int pid() {
        return PEPacketIDs.CRAFTING_DATA_PACKET;
    }

    @Override
    public void encode() {
        /*
        enchants = 0;
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));

            for (Object o : recipies) {
                if (Recipe.class.isAssignableFrom(o.getClass())) {
                    Recipe r = (Recipe) o;
                    if (ShapelessRecipe.class.isInstance(r)) {
                        writer.writeInt(ENTRY_SHAPELESS);
                        ByteArrayOutputStream ebos = new ByteArrayOutputStream();
                        PEBinaryWriter ewriter = new PEBinaryWriter(ebos);
                        {
                            ewriter.writeInt(((ShapelessRecipe) r).getIngredientList().size());
                            for (ItemStack stack : ((ShapelessRecipe) r).getIngredientList()) {
                                PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(stack));
                            }
                            ewriter.writeInt(1);
                            PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(r.getResult()));
                        }
                        writer.writeInt(ebos.toByteArray().length);
                        writer.write(ebos.toByteArray());
                    } else if (FurnaceRecipe.class.isInstance(r)) {
                        FurnaceRecipe f = (FurnaceRecipe) r;
                        if (f.getResult().getDurability() != 0) {
                            writer.writeInt(ENTRY_FURNACE_DATA);
                            ByteArrayOutputStream ebos = new ByteArrayOutputStream();
                            PEBinaryWriter ewriter = new PEBinaryWriter(ebos);
                            {
                                ewriter.writeInt(f.getInput().getTypeId() << 16 | f.getInput().getDurability());
                                PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(f.getResult()));
                            }
                            writer.writeInt(ebos.toByteArray().length);
                            writer.write(ebos.toByteArray());
                        } else {
                            writer.writeInt(ENTRY_FURNACE);
                            ByteArrayOutputStream ebos = new ByteArrayOutputStream();
                            PEBinaryWriter ewriter = new PEBinaryWriter(ebos);
                            {
                                ewriter.writeInt(f.getInput().getTypeId());
                                PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(f.getResult()));
                            }
                            writer.writeInt(ebos.toByteArray().length);
                            writer.write(ebos.toByteArray());
                        }
                    } else if (ShapedRecipe.class.isInstance(r)) {
                        ShapedRecipe sr = (ShapedRecipe) r;
                        writer.writeInt(ENTRY_SHAPED);
                        ByteArrayOutputStream ebos = new ByteArrayOutputStream();
                        PEBinaryWriter ewriter = new PEBinaryWriter(ebos);
                        {
                            ewriter.writeInt(sr.getShape()[0].length());
                            ewriter.writeInt(sr.getShape().length);
                            for (int pos = 0; pos > sr.getShape()[0].length(); pos++) {
                                for (String line : sr.getShape()) {
                                    ItemStack stack = sr.getIngredientMap().get(line.charAt(pos));
                                    PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(stack));
                                }
                            }
                            ewriter.writeInt(1);
                            PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(sr.getResult()));
                            long mostUUID = sr.getResult().getTypeId() << 32 | sr.getResult().getDurability() << 16 | (sr.getResult().getAmount() & 0xFFFF);
                            UUID uid = new UUID(mostUUID, mostUUID);
                            ewriter.writeUUID(uid);
                        }
                        writer.writeInt(ebos.toByteArray().length);
                        writer.write(ebos.toByteArray());
                    } else {
                        writer.writeInt(-1);
                        writer.writeInt(0);
                    }
                } else if (Enchantment.class.isAssignableFrom(o.getClass())){
                    Enchantment e = (Enchantment) o;
                                                writer.writeInt(ENTRY_ENCHANT);
                            ByteArrayOutputStream ebos = new ByteArrayOutputStream();
                            PEBinaryWriter ewriter = new PEBinaryWriter(ebos);
                            {
                                ewriter.writeInt(enchants);
                                enchants ++;
                                ewriter.writeInt(e.getId());
                                ewriter.writeInt(e.getMaxLevel()); //HACK: Always return this.
                                ewriter.writeInt(1);               //HACK: Always 1 for now. TO BE FIXED! 
                                ewriter.writeString(e.getName());
                            }
                            writer.writeInt(ebos.toByteArray().length);
                            writer.write(ebos.toByteArray());
                }
            }


            writer.writeInt(recipies.size());
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
         */
    }

    @Override
    public void decode() {
    }

}
