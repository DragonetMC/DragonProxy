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
import java.util.List;
import org.dragonet.common.data.inventory.InventoryType;
import org.dragonet.common.data.inventory.Slot;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.IInventoryTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.protocol.packets.*;

public class ChestWindowTranslator implements IInventoryTranslator {

    public boolean prepare(UpstreamSession session, CachedWindow window) {
        BlockPosition pos = new BlockPosition((int) session.getEntityCache().getClientEntity().x,
                (int) session.getEntityCache().getClientEntity().y - 4,
                (int) session.getEntityCache().getClientEntity().z);

        session.getDataCache().put(CacheKey.WINDOW_BLOCK_POSITION, pos);
        session.sendFakeBlock(pos.x, pos.y, pos.z, 54, 0);

        BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
        blockEntityData.blockPosition = pos;
        blockEntityData.tag = ItemBlockTranslator.translateBlockEntityToPE(ItemBlockTranslator.newTileTag("minecraft:chest", pos.x, pos.y, pos.z));
        if (window.size - 36 > 27) { //DoubleChest
            blockEntityData.tag.putInt("pairx", pos.x + 1);
            blockEntityData.tag.putInt("pairz", pos.z);
        } else //SimpleChest
            session.sendPacket(blockEntityData);

        if (window.size - 36 > 27) { //DoubleChest
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
            session.getDataCache().put(CacheKey.WINDOW_BLOCK_POSITION, posList);
        }
        System.out.println("ChestWindowTranslator.prepare " + window.windowId);
        return true;
    }

    public boolean open(UpstreamSession session, CachedWindow window) {

//        System.out.println("size : " + window.size);
//        if (window.size - 36 > 27)
//            System.out.println("Double chest opened");
//        else
//            System.out.println("Simple chest opened");
        session.getDataCache().put(CacheKey.WINDOW_OPENED_ID, window.windowId);
        session.getDataCache().put(CacheKey.WINDOW_OPENED_SIZE, window.size - 36); //-36 for the player inv size

        BlockPosition pos = new BlockPosition((int) session.getEntityCache().getClientEntity().x,
                (int) session.getEntityCache().getClientEntity().y - 4,
                (int) session.getEntityCache().getClientEntity().z);

        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = window.windowId;
        pk.type = 0;
        pk.position = pos;
        session.sendPacket(pk);
        System.out.println("ChestWindowTranslator.open " + window.windowId);
        return true;
    }

    public void updateContent(UpstreamSession session, CachedWindow window) {
        sendContent(session, window);
    }

    public void updateSlot(UpstreamSession session, CachedWindow win, int slotIndex) {
        InventorySlotPacket pk = new InventorySlotPacket();
        pk.item = ItemBlockTranslator.translateSlotToPE(win.slots[slotIndex]);
        pk.slotId = slotIndex;
        pk.windowId = win.windowId;
        session.sendPacket(pk, true);
    }

    private void sendContent(UpstreamSession session, CachedWindow win) {
        InventoryContentPacket pk = new InventoryContentPacket();
        pk.windowId = win.windowId;
        pk.items = new Slot[win.slots.length];
        for (int i = 0; i < pk.items.length; i++)
            pk.items[i] = ItemBlockTranslator.translateSlotToPE(win.slots[i]);
        session.sendPacket(pk, true);
    }
}
