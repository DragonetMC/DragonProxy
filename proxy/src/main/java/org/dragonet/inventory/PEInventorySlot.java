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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import org.dragonet.proxy.nbt.PENBT;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;
import org.spacehq.mc.protocol.data.game.ItemStack;

public class PEInventorySlot {

    public final static PEInventorySlot AIR = new PEInventorySlot();

    public int id;
    public int count;
    public int meta;
    public CompoundTag nbt;

    public PEInventorySlot() {
        this(0, 0, 0);
    }

    public PEInventorySlot(int id, int count, int meta) {
        this.id = id;
        this.count = count;
        this.meta = meta;
        nbt = new CompoundTag("");
    }

    public PEInventorySlot(int id, int count, int meta, CompoundTag nbt) {
        this.id = id;
        this.count = count;
        this.meta = meta;
        this.nbt = nbt;
    }

    public static PEInventorySlot readSlot(PEBinaryReader reader) throws IOException {
        short id = (short) (reader.readVarInt() & 0xFFFF); //Unsigned
        if (id <= 0) {
            return new PEInventorySlot((short) 0, (byte) 0, (short) 0);
        }
        int mixedData = reader.readVarInt();
        int count = reader.readByte() & 0xFF;
        int meta = mixedData >> 8;
        if(meta == -1){
            meta = 0;
        }
        reader.switchEndianness();
        short lNbt = reader.readShort();
        reader.switchEndianness();
        if (lNbt <= 0) {
            return new PEInventorySlot(id, count, meta);
        }
        byte[] nbtData = reader.read(lNbt);
        CompoundTag nbt = PENBT.read(new DataInputStream(new ByteArrayInputStream(nbtData)), ByteOrder.LITTLE_ENDIAN);
        return new PEInventorySlot(id, count, meta, nbt);
    }

    public static void writeSlot(PEBinaryWriter writer, PEInventorySlot slot) throws IOException {
        if (slot == null || slot.id == 0) {
            writer.writeShort((short) 0);
            return;
        }
        writer.writeVarInt(slot.id);
        int mixedData = slot.meta << 8 | slot.count;
        writer.writeVarInt(mixedData);
        if (slot.nbt == null) {
            writer.writeShort((short) 0);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PENBT.write(slot.nbt, new DataOutputStream(bos), ByteOrder.LITTLE_ENDIAN);
            byte[] nbtdata = bos.toByteArray();
            writer.switchEndianness();
            writer.writeShort((short) (nbtdata.length & 0xFFFF));
            writer.switchEndianness();
            writer.write(nbtdata);
        }
    }

    public static PEInventorySlot fromItemStack(ItemStack item) {
        return ItemBlockTranslator.translateToPE(item);
    }

    @Override
    public String toString() {
        return "{PE Item: ID=" + this.id + ", Count=" + (this.count & 0xFF) + ", Data=" + this.meta + "}";
    }
}
