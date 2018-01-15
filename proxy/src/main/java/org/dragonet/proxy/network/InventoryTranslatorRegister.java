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
package org.dragonet.proxy.network;

import java.util.HashMap;
import java.util.Map;

import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.dragonet.common.data.inventory.ContainerId;
import org.dragonet.common.data.inventory.Slot;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.ContainerClosePacket;
import org.dragonet.protocol.packets.InventoryContentPacket;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.inv.*;
import org.dragonet.proxy.network.translator.IInventoryTranslator;
import org.dragonet.proxy.DragonProxy;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

public final class InventoryTranslatorRegister {

    // PC Type => PE Translator
    private static final Map<WindowType, IInventoryTranslator> TRANSLATORS = new HashMap<>();

    static {
        TRANSLATORS.put(WindowType.CHEST, new ChestWindowTranslator());
    }

    public static PEPacket[] sendPlayerInventory(UpstreamSession session) {
        CachedWindow win = session.getWindowCache().getPlayerInventory();
        // Translate and send
        InventoryContentPacket ret = new InventoryContentPacket();
        ret.windowId = ContainerId.INVENTORY.getId();
        ret.items = new Slot[40];
        // hotbar
        for (int i = 36; i < 45; i++) {
            ret.items[i - 36] = ItemBlockTranslator.translateSlotToPE(win.slots[i]);
        }
        // inventory
        for (int i = 9; i < 36; i++) {
            // TODO: Add NBT support
            ret.items[i] = ItemBlockTranslator.translateSlotToPE(win.slots[i]);
        }
        // armors
        for (int i = 5; i < 9; i++) {
            ret.items[i + 31] = ItemBlockTranslator.translateSlotToPE(win.slots[i]);
        }
        // TODO: Add armor support
        return new PEPacket[]{ret};
    }

    public static void open(UpstreamSession session, ServerOpenWindowPacket win) {
        closeOpened(session, true);
        if (TRANSLATORS.containsKey(win.getType())) {
            CachedWindow cached = new CachedWindow(win.getWindowId(), win.getType(), win.getSlots() + 36/* player inventory */);
            session.getWindowCache().cacheWindow(cached);
            final IInventoryTranslator translator = TRANSLATORS.get(win.getType());
            translator.prepare(session, cached);
//            DragonProxy.getInstance().getGeneralThreadPool().schedule(() -> { // with this -> double chest, without -> content....
                translator.open(session, cached);
                com.github.steveice10.packetlib.packet.Packet[] items = session.getWindowCache().getCachedPackets(win.getWindowId());
                System.out.println("items : " + items.length);
                for (com.github.steveice10.packetlib.packet.Packet item : items) {
                    if (item != null) {
                        if (ServerWindowItemsPacket.class.isAssignableFrom(item.getClass())) {
                            updateContent(session, (ServerWindowItemsPacket) item);
                        } else {
                            updateSlot(session, (ServerSetSlotPacket) item);
                        }
                    }
                }
//            }, 100, TimeUnit.MILLISECONDS);
        } else {
            // Not supported
            session.getDownstream().send(new ClientCloseWindowPacket(win.getWindowId()));
        }
    }

    public static void closeOpened(UpstreamSession session, boolean byServer) {
        if (session.getDataCache().containsKey(CacheKey.WINDOW_OPENED_ID)) {
            // There is already a window opened
            int id = (int) session.getDataCache().remove(CacheKey.WINDOW_OPENED_ID);
            // clean cache
            session.getWindowCache().getCachedWindows().remove(id);
            if (byServer) {
                session.sendPacket(new ContainerClosePacket(id), true);
            } else {
                session.getDownstream().send(new ClientCloseWindowPacket(id));
            }
            if (session.getDataCache().containsKey(CacheKey.WINDOW_BLOCK_POSITION)) {
                if (session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION) instanceof BlockPosition)
                {
                    BlockPosition pos = (BlockPosition) session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION);
                    // Already a block was replaced to Chest, reset it
                    // Set to stone since we don't know what it was, server will correct it once client interacts it
                    session.sendFakeBlock(pos.x, pos.y, pos.z, 1, 0);
                }
                else if (session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION) instanceof ArrayList)
                {
                    for(BlockPosition pos : (List<BlockPosition>)session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION))
                    {
                        session.sendFakeBlock(pos.x, pos.y, pos.z, 1, 0);
                    }
                }
            }
        }
    }

    public static void updateContent(UpstreamSession session, ServerWindowItemsPacket packet) {
        if (packet.getWindowId() == 0) {
            return; // We don't process player inventory updates here.
        }
        if (!session.getDataCache().containsKey(CacheKey.WINDOW_OPENED_ID)
            || !session.getWindowCache().hasWindow(packet.getWindowId())) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        int openedId = (int) session.getDataCache().get(CacheKey.WINDOW_OPENED_ID);
        if (packet.getWindowId() != openedId) {
            // Hmm
            closeOpened(session, true);
            return;
        }

        CachedWindow win = session.getWindowCache().get(openedId);
        IInventoryTranslator t = TRANSLATORS.get(win.pcType);
        if (t == null) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        win.slots = packet.getItems();
        t.updateContent(session, win);
    }

    public static void updateSlot(UpstreamSession session, ServerSetSlotPacket packet) {
        if (packet.getWindowId() == 0) {
            return; // We don't process player inventory updates here.
        }
        if (!session.getDataCache().containsKey(CacheKey.WINDOW_OPENED_ID)
            || !session.getWindowCache().hasWindow(packet.getWindowId())) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        int openedId = (int) session.getDataCache().get(CacheKey.WINDOW_OPENED_ID);
        if (packet.getWindowId() != openedId) {
            // Hmm
            closeOpened(session, true);
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        CachedWindow win = session.getWindowCache().get(openedId);
        if (win.size <= packet.getSlot()) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        IInventoryTranslator t = TRANSLATORS.get(win.pcType);
        if (t == null) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        win.slots[packet.getSlot()] = packet.getItem(); // Update here
        t.updateSlot(session, win, packet.getSlot());
    }
}
