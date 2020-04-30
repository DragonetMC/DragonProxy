/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;

@BlockEntityRegisterInfo(bedrockId = "Beacon")
public class BeaconBlockEntityTranslator implements IBlockEntityTranslator {

    @Override
    public void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag) {
        // TODO: validation
        builder.intTag("Primary", (int) javaTag.get("Primary").getValue());
        builder.intTag("Secondary", (int) javaTag.get("Secondary").getValue());
        builder.intTag("Levels", (int) javaTag.get("Levels").getValue());
        builder.stringTag("Lock", "");
    }
}