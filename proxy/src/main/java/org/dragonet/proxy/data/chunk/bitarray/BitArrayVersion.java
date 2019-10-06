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
package org.dragonet.proxy.data.chunk.bitarray;

import org.dragonet.proxy.util.MathUtils;

public enum BitArrayVersion {
    V16(16, 2, null),
    V8(8, 4, V16),
    V6(6, 5, V8), // 2 bit padding
    V5(5, 6, V6), // 2 bit padding
    V4(4, 8, V5),
    V3(3, 10, V4), // 2 bit padding
    V2(2, 16, V3),
    V1(1, 32, V2);

    final byte bits;
    final byte entriesPerWord;
    final int maxEntryValue;
    final BitArrayVersion next;

    BitArrayVersion(int bits, int entriesPerWord, BitArrayVersion next) {
        this.bits = (byte) bits;
        this.entriesPerWord = (byte) entriesPerWord;
        this.maxEntryValue = (1 << this.bits) - 1;
        this.next = next;
    }

    public BitArray createPalette(int size) {
        return this.createPalette(size, new int[MathUtils.ceil((float) size / entriesPerWord)]);
    }

    public byte getId() {
        return bits;
    }

    public int getWordsForSize(int size) {
        return MathUtils.ceil((float) size / entriesPerWord);
    }

    public int getMaxEntryValue() {
        return maxEntryValue;
    }

    public BitArrayVersion next() {
        return next;
    }

    public BitArray createPalette(int size, int[] words) {
        if (this == V3 || this == V5 || this == V6) {
            // Padded palettes aren't able to use bitwise operations due to their padding.
            return new PaddedBitArray(this, size, words);
        } else {
            return new Pow2BitArray(this, size, words);
        }
    }

    public static BitArrayVersion get(int version, boolean read) {
        for (BitArrayVersion ver : values()) {
            if ( ( !read && ver.entriesPerWord <= version ) || ( read && ver.bits == version ) ) {
                return ver;
            }
        }
        throw new IllegalArgumentException("Invalid palette version: " + version);
    }
}
