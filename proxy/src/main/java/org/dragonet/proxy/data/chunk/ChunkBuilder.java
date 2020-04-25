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

import com.google.common.base.Preconditions;

public class ChunkBuilder {

    private final int x;
    private final int z;

    private ChunkSection[] sections;
//    private final TIntShortMap extraData = new TIntShortHashMap();
    private byte[] biomes;
    private byte[] heightMap;
    //private final List<BlockUpdate> blockUpdates = new ArrayList<>();
    private boolean dirty;
    private boolean generated;
    private boolean populated;

    public ChunkBuilder(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public ChunkBuilder sections(ChunkSection[] sections) {
        this.sections = Preconditions.checkNotNull(sections, "sections");
        return this;
    }

    public ChunkBuilder extraData(int key, short value) {
//        this.extraData.put(key, value);
        return this;
    }

    public ChunkBuilder biomes(byte[] biomes) {
        this.biomes = Preconditions.checkNotNull(biomes, "biomes");
        return this;
    }

    public ChunkBuilder heightMap(byte[] heightMap) {
        this.heightMap = Preconditions.checkNotNull(heightMap, "heightMap");
        return this;
    }

//    public ChunkBuilder blockUpdate(BlockUpdate blockUpdate) {
//        Preconditions.checkNotNull(blockUpdate, "blockUpdate");
//        this.blockUpdates.add(blockUpdate);
//        return this;
//    }

    public ChunkBuilder generated() {
        this.generated = true;
        return this;
    }

    public ChunkBuilder populated() {
        this.populated = true;
        return this;
    }

    public ChunkBuilder dirty() {
        this.dirty = true;
        return this;
    }

    public ChunkData build() {
        Preconditions.checkNotNull(this.sections, "sections");
        Preconditions.checkNotNull(this.biomes, "biomes");
        Preconditions.checkNotNull(this.heightMap, "heightMap");
//        ChunkData chunk = new ChunkData(this.x, this.z, this.sections, this.extraData, this.biomes,
//                this.heightMap/*, this.blockUpdates*/);
//        if (this.dirty) {
//            chunk.setDirty();
//        }
//        if (this.generated) {
//            chunk.setGenerated();
//        }
//        if (this.populated) {
//            chunk.setPopulated();
//        }
//        return chunk;
        return null;
    }
}
