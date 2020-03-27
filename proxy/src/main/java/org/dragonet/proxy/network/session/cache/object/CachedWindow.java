/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
package org.dragonet.proxy.network.session.cache.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import com.nukkitx.protocol.bedrock.packet.ContainerClosePacket;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.types.BlockTranslator;
import org.dragonet.proxy.util.TextFormat;

@Data
@Log4j2
public class CachedWindow {
    private final int windowId;

    private ItemStack[] items;
    private WindowType type;

    private String name;
    private boolean open = false;

    public CachedWindow(int windowId, WindowType type, int size) {
        this.windowId = windowId;
        this.type = type;
        this.items = new ItemStack[size];
    }

    public void open(ProxySession session) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setWindowId((byte) windowId);
        containerOpenPacket.setType((byte) 0);
        containerOpenPacket.setBlockPosition(Vector3i.ZERO);

        session.sendPacket(containerOpenPacket);
        open = true;
    }

    public void close(ProxySession session) {
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.setWindowId((byte) windowId);

        session.sendPacket(containerClosePacket);
        open = false;
    }
}
