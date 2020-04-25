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
package org.dragonet.proxy.network.session.cache.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.*;
import it.unimi.dsi.fastutil.ints.Int2BooleanMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.BlockEntityTranslator;
import org.dragonet.proxy.network.translator.misc.BlockTranslator;
import org.dragonet.proxy.network.translator.misc.inventory.IInventoryTranslator;
import org.dragonet.proxy.network.translator.misc.inventory.action.SlotChangeAction;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Log4j2
public class CachedWindow {
    private final int windowId;

    private ItemStack[] items;
    private IInventoryTranslator inventoryTranslator;
    private BedrockWindowType windowType;

    private String name;
    private boolean open = false;

    private Vector3i fakeBlockPosition = null;

    private AtomicInteger transactionIdCounter = new AtomicInteger(1);
    private Int2BooleanMap transactions = new Int2BooleanOpenHashMap();

    public CachedWindow(int windowId, IInventoryTranslator inventoryTranslator) {
        this.windowId = windowId;
        this.inventoryTranslator = inventoryTranslator;
        this.items = new ItemStack[inventoryTranslator.getSize()];
        this.windowType = inventoryTranslator.getBedrockWindowType();
    }

    public void open(ProxySession session) {
        Vector3i position = session.getCachedEntity().getPosition().add(1, 2, 1).toInt();
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();

        if(!windowType.isEntity()) {
            // TODO: check how far away from the players current position
            Integer runtimeId = BlockTranslator.bedrockIdToRuntime(windowType.getFakeId());
            if(runtimeId == null) {
                log.warn("Unable to get runtime id for block: " + windowType.getFakeId());
                return;
            }
            if(session.getLastClickedPosition() != null && session.getChunkCache().getBlockAt(session.getLastClickedPosition()) == runtimeId) {
                position = session.getLastClickedPosition();
            } else {
                sendFakeBlock(session, position);
            }
            containerOpenPacket.setBlockPosition(position);
        } else {
            containerOpenPacket.setBlockPosition(Vector3i.ZERO);
            containerOpenPacket.setUniqueEntityId(session.getLastClickedEntity().getProxyEid());
            //sendFakeEntity(session, position);
        }

        containerOpenPacket.setWindowId((byte) windowId);
        containerOpenPacket.setType((byte) windowType.getContainerId());
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

    public void sendInventory(ProxySession session) {
        inventoryTranslator.updateInventory(session, this);
    }

    public void sendSlot(ProxySession session, int slot) {
        inventoryTranslator.updateSlot(session, this, slot);
    }

    public boolean setItem(int slot, ItemData item) {
        if(slot > items.length) {
            log.warn("set item");
            return false;
        }
        this.items[slot] = ItemTranslatorRegistry.translateToJava(item);
        return true;
    }

    public ItemData getItem(int slot) {
        return ItemTranslatorRegistry.translateSlotToBedrock(items[slot]);
    }

    private void sendFakeEntity(ProxySession session, Vector3i position) {
        long entityId = session.getEntityCache().getNextClientEntityId().getAndIncrement();

        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setUniqueEntityId(entityId);
        addEntityPacket.setRuntimeEntityId(entityId);
        addEntityPacket.setIdentifier(windowType.getFakeId());
        addEntityPacket.setPosition(position.toFloat());
        addEntityPacket.setMotion(Vector3f.ZERO);
        addEntityPacket.setRotation(Vector3f.ZERO);
        addEntityPacket.setEntityType(BedrockEntityType.VILLAGER_V2.getType());
        addEntityPacket.getMetadata().put(EntityData.SCALE, 0.01f);

        session.sendPacket(addEntityPacket);
    }

    private void sendFakeBlock(ProxySession session, Vector3i position) {
        fakeBlockPosition = position;
        //log.warn("fake block");

        session.getChunkCache().sendFakeBlock(session, windowType.getFakeId(), position);

        BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();
        blockEntityDataPacket.setBlockPosition(position);
        blockEntityDataPacket.setData(CompoundTag.builder()
            .stringTag("id", BlockEntityTranslator.getBedrockIdentifier(windowType.getFakeId()))
            .stringTag("CustomName", name)
            .intTag("x", position.getX())
            .intTag("y", position.getY())
            .intTag("z", position.getZ())
            .buildRootTag());

        session.sendPacket(blockEntityDataPacket);
    }
}
