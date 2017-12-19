/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.inv;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;

import org.dragonet.proxy.data.inventory.InventoryType;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.IInventoryTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.protocol.packets.BlockEntityDataPacket;
import org.dragonet.proxy.protocol.packets.ContainerOpenPacket;
import org.dragonet.proxy.protocol.packets.InventoryContentPacket;
import org.dragonet.proxy.protocol.type.Slot;
import org.dragonet.proxy.utilities.BlockPosition;

public class ChestWindowTranslator implements IInventoryTranslator {

    public boolean open(UpstreamSession session, CachedWindow window) {
        Position pos = new Position((int) session.getEntityCache().getClientEntity().x,
                (int) session.getEntityCache().getClientEntity().y - 4,
                (int) session.getEntityCache().getClientEntity().z);
        session.getDataCache().put(CacheKey.WINDOW_OPENED_ID, window.windowId);
        session.getDataCache().put(CacheKey.WINDOW_BLOCK_POSITION, pos);
        session.sendFakeBlock(pos.getX(), pos.getY(), pos.getZ(), 54, 0);
        CompoundTag tag = new CompoundTag(null);
        tag.put(new StringTag("id", "Chest"));
        tag.put(new IntTag("x", pos.getX()));
        tag.put(new IntTag("y", pos.getY()));
        tag.put(new IntTag("z", pos.getZ()));
        BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
        blockEntityData.blockPosition = new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
        blockEntityData.tag = tag;
        session.sendPacket(blockEntityData);

        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = window.windowId;
        // pk. = window.size <= 27 ? (short)(InventoryType.SlotSize.CHEST & 0xFFFF) :
        // (short)(InventoryType.SlotSize.DOUBLE_CHEST & 0xFFFF);
        pk.type = window.size <= 27 ? InventoryType.PEInventory.CHEST : InventoryType.PEInventory.DOUBLE_CHEST;
        pk.position = new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
        session.sendPacket(pk);
        return true;
    }

    public void updateContent(UpstreamSession session, CachedWindow window) {
        sendContent(session, window);
    }

    public void updateSlot(UpstreamSession session, CachedWindow window, int slotIndex) {
        sendContent(session, window);// TOO LAZY LOL
    }

    private void sendContent(UpstreamSession session, CachedWindow win) {
        InventoryContentPacket pk = new InventoryContentPacket();
        pk.windowId = (byte) (win.windowId & 0xFF);
        pk.items = new Slot[win.slots.length];
        for (int i = 0; i < pk.items.length; i++) {
            pk.items[i] = ItemBlockTranslator.translateSlotToPE(win.slots[i]);
        }
        session.sendPacket(pk, true);
    }
}
