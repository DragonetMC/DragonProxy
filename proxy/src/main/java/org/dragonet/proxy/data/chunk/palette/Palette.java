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
package org.dragonet.proxy.data.chunk.palette;

public interface Palette {

    void set(int index, int value);

    int get(int index);

    int size();

    int[] getWords();

    PaletteVersion getVersion();

    Palette copy();
}
