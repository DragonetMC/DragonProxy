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
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.types.BlockEntityTranslator;
import org.dragonet.proxy.network.translator.types.BlockTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.concurrent.TimeUnit;

@Data
@Log4j2
public class CachedWindow {
    private final int windowId;

    private ItemStack[] items;
    private BedrockWindowType windowType;

    private String name;
    private boolean open = false;

    private Vector3i fakeBlockPosition = null;

    public CachedWindow(int windowId, BedrockWindowType windowType, int size) {
        this.windowId = windowId;
        this.windowType = windowType;
        this.items = new ItemStack[size];
    }

    /**
     * TODO: Only send fake blocks if its a virtual inventory. It may be possible to
     *       measure the time before the last block click and then the inventory that appears.
     *
     *       This will fix the fact that command blocks will not save your command, as the bedrock
     *       client only knows about the fake one and obviously it doesn't exist on the remote server,
     *       so the command is not set.
     *
     *       I could fix the command block issue fairly easily, but it'll just be a hack, so i won't.
     */
    public void open(ProxySession session) {
        Vector3i position = session.getCachedEntity().getPosition().sub(1, 3, 1).toInt();

        if(session.getLastClickedPosition() != null && session.getChunkCache().getBlockAt(session.getLastClickedPosition()) ==
            BlockTranslator.bedrockIdToRuntime(windowType.getFakeBlockId())) {

            position = session.getLastClickedPosition();
        } else {
            fakeBlockPosition = position;
            //log.warn("fake block");

            session.getChunkCache().sendFakeBlock(session, windowType.getFakeBlockId(), position);

            BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();
            blockEntityDataPacket.setBlockPosition(position);
            blockEntityDataPacket.setData(CompoundTag.builder()
                .stringTag("id", BlockEntityTranslator.getBedrockIdentifier(windowType.getFakeBlockId()))
                .stringTag("CustomName", name)
                .intTag("x", position.getX())
                .intTag("y", position.getY())
                .intTag("z", position.getZ())
                .buildRootTag());

            session.sendPacket(blockEntityDataPacket);
        }

        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setWindowId((byte) windowId);
        containerOpenPacket.setType((byte) windowType.getContainerId());
        containerOpenPacket.setBlockPosition(position);

        session.sendPacket(containerOpenPacket);
        open = true;
    }

    public void close(ProxySession session) {
        if(fakeBlockPosition != null) {
            int originalBlockId = session.getChunkCache().getBlockAt(fakeBlockPosition);
            session.getChunkCache().sendFakeBlock(session, originalBlockId, fakeBlockPosition);
            fakeBlockPosition = null;
        }

        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.setWindowId((byte) windowId);

        session.sendPacket(containerClosePacket);
        open = false;

        // TODO: should we remove the window from the cache at this point? i'll leave it for now.
    }
}
