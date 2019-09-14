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

//        for(PaletteManager.RuntimeEntry entry : DragonProxy.INSTANCE.getPaletteManager().getEntries() ) {
//            log.info("1: " + entry.getId() + " // 2: " + (entry.getId() | entry.getData()) + " // 3: " + PaletteManager.getOrCreateRuntimeId(entry.getId(), entry.getData()));
//            this.palette.add(PaletteManager.getOrCreateRuntimeId(entry.getId(), entry.getData()));
//        }
    }

    private BlockStorage(BitArray bitArray, TIntArrayList palette) {
        this.palette = palette;
        this.bitArray = bitArray;
    }

    public synchronized int getFullBlock(int index) {
        return this.legacyIdFor(this.bitArray.get(index));
    }

    public synchronized void setFullBlock(int index, int legacyId) {
        int runtimeId = PaletteManager.getOrCreateRuntimeId(legacyId);
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

//    public synchronized void readFromStorage(ByteBuf buffer) {
//        BitArrayVersion version = getVersionFromHeader(buffer.readByte());
//
//        int expectedWordCount = version.getWordsForSize(SIZE);
//        int[] words = new int[expectedWordCount];
//        for (int i = 0; i < expectedWordCount; i++) {
//            words[i] = buffer.readIntLE();
//        }
//        this.bitArray = version.createPalette(SIZE, words);
//
//        this.palette.clear();
//        this.palette.add(0);
//        int paletteSize = buffer.readIntLE();
//        try (ByteBufInputStream stream = new ByteBufInputStream(buffer); NBTInputStream nbtStream =
//            new NBTInputStream(stream, ByteOrder.LITTLE_ENDIAN, false)) {
//            for (int i = 0; i < paletteSize; i++) {
//                CompoundTag tag = (CompoundTag) Tag.readNamedTag(nbtStream);
//                int id = PaletteManager.getLegacyIdFromName(tag.getString("name"));
//                int data = tag.getShort("data");
//                if (id == 0) {
//                    continue;
//                }
//
//                this.palette.add(PaletteManager.getOrCreateRuntimeId(id, data));
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

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

    private int legacyIdFor(int index) {
        int runtimeId = this.palette.get(index);
        return PaletteManager.getLegacyId(runtimeId);
    }

    private static int getPaletteHeader(BitArrayVersion version, boolean runtime) {
        return (version.getId() << 1) | (runtime ? 1 : 0);
    }

    private static BitArrayVersion getVersionFromHeader(byte header) {
        return BitArrayVersion.get(header >> 1, true);
    }

    public boolean isEmpty() {
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
