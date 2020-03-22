/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * The code in this file is from the NukkitX project, and it has been
 * used with permission.
 *
 * Copyright (C) 2019 NukkitX
 * https://github.com/NukkitX/Nukkit
 */
package org.dragonet.proxy.data.chunk;

import com.nukkitx.network.VarInts;
import gnu.trove.list.array.TIntArrayList;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.chunk.bitarray.BitArray;
import org.dragonet.proxy.data.chunk.bitarray.BitArrayVersion;
import org.dragonet.proxy.util.PaletteManager;

@Log4j2
public class BlockStorage {

    private static final int SIZE = 4096;

    @Getter
    private final TIntArrayList palette;
    @Getter
    private BitArray bitArray;

    public BlockStorage() {
        this(BitArrayVersion.V2);
    }

    public BlockStorage(BitArrayVersion version) {
        this.bitArray = version.createPalette(SIZE);
        this.palette = new TIntArrayList(16, -1);
        this.palette.add(0); // Air is at the start of every palette.
    }

    private BlockStorage(BitArray bitArray, TIntArrayList palette) {
        this.palette = palette;
        this.bitArray = bitArray;
    }

    public synchronized int getFullBlock(int index) {
        return this.palette.get(this.bitArray.get(index));
    }

    public synchronized void setFullBlock(int index, int runtimeId) {
        int idx = this.idFor(runtimeId);

//        int idx = this.idFor(legacyId);
        this.bitArray.set(index, idx);
    }

    public synchronized void writeToNetwork(ByteBuf buffer) {
        buffer.writeByte(getPaletteHeader(bitArray.getVersion(), true));

        for (int word : bitArray.getWords()) {
            buffer.writeIntLE(word);
        }

        VarInts.writeInt(buffer, palette.size());
        palette.forEach(id -> {
            VarInts.writeInt(buffer, id);
            return true;
        });
    }

    public synchronized void writeToStorage(ByteBuf buffer) {
        buffer.writeByte(getPaletteHeader(bitArray.getVersion(), false));
        for (int word : bitArray.getWords()) {
            buffer.writeIntLE(word);
        }

        //TODO: Write persistent NBT tags
    }

    private void onResize(BitArrayVersion version) {
        BitArray newBitArray = version.createPalette(SIZE);

        for (int i = 0; i < SIZE; i++) {
            newBitArray.set(i, this.bitArray.get(i));
        }
        this.bitArray = newBitArray;
    }

    private int idFor(int runtimeId) {
        int index = this.palette.indexOf(runtimeId);
        if (index != -1) {
            return index;
        }

        index = this.palette.size();
        this.palette.add(runtimeId);
        BitArrayVersion version = this.bitArray.getVersion();
        if (index > version.getMaxEntryValue()) {
            BitArrayVersion next = version.next();
            if (next != null) {
                this.onResize(next);
            }
        }
        return index;
    }

    private static int getPaletteHeader(BitArrayVersion version, boolean runtime) {
        return (version.getId() << 1) | (runtime ? 1 : 0);
    }

    private static BitArrayVersion getVersionFromHeader(byte header) {
        return BitArrayVersion.get(header >> 1, true);
    }

    public boolean isEmpty() {
        // TODO CHECK
        if (this.palette.size() == 1) {
            return true;
        }
        for (int word : this.bitArray.getWords()) {
            if (word != 0) {
                return false;
            }
        }
        return true;
    }

    public BlockStorage copy() {
        return new BlockStorage(this.bitArray.copy(), new TIntArrayList(this.palette));
    }
}
