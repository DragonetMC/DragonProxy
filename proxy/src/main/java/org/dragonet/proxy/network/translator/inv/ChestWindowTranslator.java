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

import java.util.ArrayList;
import org.dragonet.api.caches.ICachedWindow;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.common.data.inventory.Slot;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.api.translators.IInventoryTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.protocol.packets.*;

public class ChestWindowTranslator implements IInventoryTranslator {

    @Override
    public boolean prepare(IUpstreamSession session, ICachedWindow window) {
        BlockPosition pos = new BlockPosition((int) session.getEntityCache().getClientEntity().getX(),
                (int) session.getEntityCache().getClientEntity().getY() - 4,
                (int) session.getEntityCache().getClientEntity().getZ());

        session.getDataCache().put(CacheKey.CURRENT_WINDOW_POSITION, pos);
        session.sendFakeBlock(pos.x, pos.y, pos.z, 54, 0);

        BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
        blockEntityData.blockPosition = pos;
        blockEntityData.tag = ItemBlockTranslator.translateBlockEntityToPE(ItemBlockTranslator.newTileTag("minecraft:chest", pos.x, pos.y, pos.z));
        if (window.getSize() - 36 > 27) { //DoubleChest
            blockEntityData.tag.putInt("pairx", pos.x + 1);
            blockEntityData.tag.putInt("pairz", pos.z);
        } else //SimpleChest
            session.sendPacket(blockEntityData);

        if (window.getSize() - 36 > 27) { //DoubleChest
            BlockPosition pos2 = new BlockPosition(pos.x + 1, pos.y, pos.z);
            session.sendFakeBlock(pos2.x, pos2.y, pos2.z, 54, 0);
            BlockEntityDataPacket blockEntityData2 = new BlockEntityDataPacket();
            blockEntityData2.blockPosition = pos2;
            blockEntityData2.tag = ItemBlockTranslator.translateBlockEntityToPE(ItemBlockTranslator.newTileTag("minecraft:chest", pos2.x, pos2.y, pos2.z));
            blockEntityData2.tag.putInt("pairx", pos.x);
            blockEntityData2.tag.putInt("pairz", pos.z);
            session.sendPacket(blockEntityData2);
            session.sendPacket(blockEntityData);
            ArrayList<BlockPosition> posList = new ArrayList<>();
            posList.add(pos);
            posList.add(pos2);
            session.getDataCache().put(CacheKey.CURRENT_WINDOW_POSITION, posList);
        }
//        System.out.println("ChestWindowTranslator.prepare " + window.windowId);
        return true;
    }

    @Override
    public boolean open(IUpstreamSession session, ICachedWindow window) {

        BlockPosition pos = new BlockPosition((int) session.getEntityCache().getClientEntity().getX(),
                (int) session.getEntityCache().getClientEntity().getY() - 4,
                (int) session.getEntityCache().getClientEntity().getZ());

        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = window.getWindowId();
        pk.type = 0;
        pk.position = pos;
        session.putCachePacket(pk);
//        System.out.println("ChestWindowTranslator.open " + window.windowId);
        return true;
    }

    @Override
    public void updateContent(IUpstreamSession session, ICachedWindow window) {
        InventoryContentPacket pk = new InventoryContentPacket();
        pk.windowId = window.getWindowId();
        pk.items = new Slot[window.getSlots().length];
        for (int i = 0; i < pk.items.length; i++)
            pk.items[i] = ItemBlockTranslator.translateSlotToPE(window.getSlots()[i]);
        session.putCachePacket(pk);
    }

    @Override
    public void updateSlot(IUpstreamSession session, ICachedWindow win, int slotIndex) {
        InventorySlotPacket pk = new InventorySlotPacket();
        pk.item = ItemBlockTranslator.translateSlotToPE(win.getSlots()[slotIndex]);
        pk.slotId = slotIndex;
        pk.windowId = win.getWindowId();
        session.putCachePacket(pk);
    }
}
