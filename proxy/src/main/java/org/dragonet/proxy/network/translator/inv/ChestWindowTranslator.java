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

import org.dragonet.inventory.InventoryType;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.proxy.protocol.packet.WindowItemsPacket;
import org.dragonet.proxy.protocol.packet.WindowOpenPacket;
import org.dragonet.proxy.protocol.packet.BlockEntityDataPacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.InventoryTranslator;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.spacehq.mc.protocol.data.game.Position;

public class ChestWindowTranslator implements InventoryTranslator {

    @Override
    public boolean open(UpstreamSession session, CachedWindow window) {
        Position pos = new Position((int)session.getEntityCache().getClientEntity().x, (int)session.getEntityCache().getClientEntity().y - 4, (int)session.getEntityCache().getClientEntity().z);
        session.getDataCache().put(CacheKey.WINDOW_OPENED_ID, window.windowId);
        session.getDataCache().put(CacheKey.WINDOW_BLOCK_POSITION, pos);
        session.sendFakeBlock(pos.getX(), pos.getY(), pos.getZ(), 54, 0);
        CompoundTag tag = new CompoundTag()
            .putString("id", "Chest")
            .putInt("x", (int)pos.getX())
            .putInt("y", (int)pos.getY())
            .putInt("z", (int)pos.getZ());
        session.sendPacket(new BlockEntityDataPacket((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), tag));
        WindowOpenPacket pk = new WindowOpenPacket();
        pk.windowID = (byte)(window.windowId & 0xFF);
        pk.slots = window.size <= 27 ? (short)(InventoryType.SlotSize.CHEST & 0xFFFF) : (short)(InventoryType.SlotSize.DOUBLE_CHEST & 0xFFFF);
        pk.type = window.size <= 27 ? InventoryType.PEInventory.CHEST : InventoryType.PEInventory.DOUBLE_CHEST;
        pk.x = pos.getX();
        pk.y = pos.getY();
        pk.z= pos.getZ();
        session.sendPacket(pk);
        return true;
    }

    @Override
    public void updateContent(UpstreamSession session, CachedWindow window) {
        sendContent(session, window);
    }

    @Override
    public void updateSlot(UpstreamSession session, CachedWindow window, int slotIndex) {
        sendContent(session, window);//TOO LAZY LOL
    }
    
    private void sendContent(UpstreamSession session, CachedWindow win){
        WindowItemsPacket pk = new WindowItemsPacket();
        pk.windowID = (byte)(win.windowId & 0xFF);
        pk.slots = new PEInventorySlot[win.slots.length];
        for(int i = 0; i < pk.slots.length; i++){
            pk.slots[i] = PEInventorySlot.fromItemStack(win.slots[i]);
        }
        session.sendPacket(pk, true);
    }
}
