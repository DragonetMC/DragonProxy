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

import java.io.IOException;
import java.nio.ByteOrder;

import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.InventoryTranslator;
import org.spacehq.mc.protocol.data.game.entity.metadata.Position;

import org.dragonet.inventory.PEInventorySlot;

public class ChestWindowTranslator implements InventoryTranslator {

    @Override
    public boolean open(ClientConnection session, CachedWindow window) {
        /*Position pos = new Position((int)session.getEntityCache().getClientEntity().x, (int)session.getEntityCache().getClientEntity().y - 4, (int)session.getEntityCache().getClientEntity().z);
        session.getDataCache().put(CacheKey.WINDOW_OPENED_ID, window.windowId);
        session.getDataCache().put(CacheKey.WINDOW_BLOCK_POSITION, pos);
        session.sendFakeBlock(pos.getX(), pos.getY(), pos.getZ(), 54, 0);
        CompoundTag tag = new CompoundTag()
            .putString("id", "Chest")
            .putInt("x", (int)pos.getX())
            .putInt("y", (int)pos.getY())
            .putInt("z", (int)pos.getZ());
        
        
        BlockEntityDataPacket pkBlockData = new BlockEntityDataPacket();
        pkBlockData.x = pos.getX();
        pkBlockData.y = pos.getY();
        pkBlockData.z = pos.getZ();
        try {
			pkBlockData.namedTag = NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
        session.sendPacket(pkBlockData);
        
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowid = (byte)(window.windowId & 0xFF);
        pk.slots = window.size <= 27 ? (short)(InventoryType.CHEST.getNetworkType() & 0xFFFF) : (short)(InventoryType.DOUBLE_CHEST.getNetworkType() & 0xFFFF);
        pk.type = (byte) (window.size <= 27 ? InventoryType.CHEST.getNetworkType() : InventoryType.DOUBLE_CHEST.getNetworkType());
        pk.x = pos.getX();
        pk.y = pos.getY();
        pk.z= pos.getZ();
        session.sendPacket(pk);*/
        return true;
    }

    @Override
    public void updateContent(ClientConnection session, CachedWindow window) {
        sendContent(session, window);
    }

    @Override
    public void updateSlot(ClientConnection session, CachedWindow window, int slotIndex) {
        sendContent(session, window);//TOO LAZY LOL
    }
    
    private void sendContent(ClientConnection session, CachedWindow win){
        /*ContainerSetContentPacket pk = new ContainerSetContentPacket();
        pk.windowid = (byte)(win.windowId & 0xFF);
        pk.slots = new Item[win.slots.length];
        for(int i = 0; i < pk.slots.length; i++){
            pk.slots[i] = PEInventorySlot.fromItemStack(win.slots[i]);
        }*/
        //session.sendPacket(pk, true);
    }
}
