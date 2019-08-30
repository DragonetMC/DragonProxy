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

import java.util.Comparator;

public class AroundPointChunkComparator implements Comparator<ChunkData> {
    private final int spawnX;
    private final int spawnZ;

    public AroundPointChunkComparator(int spawnX, int spawnZ) {
        this.spawnX = spawnX;
        this.spawnZ = spawnZ;
    }

    @Override
    public int compare(ChunkData o1, ChunkData o2) {
        return Integer.compare(distance(o1.getX(), o1.getZ()), distance(o2.getX(), o2.getZ()));
    }

    private int distance(int x, int z) {
        int dx = spawnX - x;
        int dz = spawnZ - z;
        return dx * dx + dz * dz;
    }
}
