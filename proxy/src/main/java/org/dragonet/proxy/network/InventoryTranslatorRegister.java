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
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.proxy.protocol.packet.PEPacket;
import org.dragonet.proxy.protocol.packet.WindowClosePacket;
import org.dragonet.proxy.protocol.packet.WindowItemsPacket;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.inv.ChestWindowTranslator;
import org.dragonet.proxy.network.translator.InventoryTranslator;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.window.WindowType;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.spacehq.packetlib.packet.Packet;

public final class InventoryTranslatorRegister {

    public final static int[] HOTBAR_CONSTANTS = new int[]{36, 37, 38, 39, 40, 41, 42, 43, 44};

    public static PEPacket[] sendPlayerInventory(UpstreamSession session) {
        CachedWindow win = session.getWindowCache().getPlayerInventory();
        //Translate and send
        WindowItemsPacket ret = new WindowItemsPacket();
        ret.windowID = PEWindowConstantID.PLAYER_INVENTORY;
        ret.slots = new PEInventorySlot[45];
        for (int i = 9; i < win.slots.length; i++) {
            //TODO: Add NBT support
            if (win.slots[i] != null) {
                ret.slots[i - 9] = PEInventorySlot.fromItemStack(win.slots[i]);
            }
        }
        for (int i = 36; i < 45; i++) {
            ret.slots[i] = ret.slots[i - 9];    //Duplicate
        }
        ret.hotbar = HOTBAR_CONSTANTS;

        //TODO: Add armor support
        return new PEPacket[]{ret};
    }

    // PC Type => PE Translator
    private final static Map<WindowType, InventoryTranslator> TRANSLATORS = new HashMap<>();

    static {
        TRANSLATORS.put(WindowType.CHEST, new ChestWindowTranslator());
    }

    public static void closeOpened(UpstreamSession session, boolean byServer) {
        if (session.getDataCache().containsKey(CacheKey.WINDOW_OPENED_ID)) {
            //There is already a window opened
            int id = (int) session.getDataCache().remove(CacheKey.WINDOW_OPENED_ID);
            if (!byServer) {
                session.getDownstream().send(new ClientCloseWindowPacket(id));
            }
            if (session.getDataCache().containsKey(CacheKey.WINDOW_BLOCK_POSITION)) {
                //Already a block was replaced to Chest, reset it
                session.sendFakeBlock(
                        ((Position) session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION)).getX(),
                        ((Position) session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION)).getY(),
                        ((Position) session.getDataCache().remove(CacheKey.WINDOW_BLOCK_POSITION)).getZ(),
                        1, //Set to stone since we don't know what it was, server will correct it once client interacts it
                        0);
            }
            if (byServer) {
                WindowClosePacket pkClose = new WindowClosePacket();
                pkClose.windowID = (byte) (id & 0xFF);
                session.sendPacket(pkClose, true);
            }
        }
    }

    public static void open(UpstreamSession session, ServerOpenWindowPacket win) {
        closeOpened(session, true);
        if (TRANSLATORS.containsKey(win.getType())) {
            CachedWindow cached = new CachedWindow(win.getWindowId(), win.getType(), 36 + win.getSlots());
            session.getWindowCache().cacheWindow(cached);
            TRANSLATORS.get(win.getType()).open(session, cached);

            Packet[] items = session.getWindowCache().getCachedPackets(win.getWindowId());
            for (Packet item : items) {
                if (item != null) {
                    if (ServerWindowItemsPacket.class.isAssignableFrom(item.getClass())) {
                        updateContent(session, (ServerWindowItemsPacket) item);
                    } else {
                        updateSlot(session, (ServerSetSlotPacket) item);
                    }
                }
            }
        } else {
            //Not supported
            session.getDownstream().send(new ClientCloseWindowPacket(win.getWindowId()));
        }
    }

    public static void updateSlot(UpstreamSession session, ServerSetSlotPacket packet) {
        if (packet.getWindowId() == 0) {
            return;   //We don't process player inventory updates here. 
        }
        if (!session.getDataCache().containsKey(CacheKey.WINDOW_OPENED_ID) || !session.getWindowCache().hasWindow(packet.getWindowId())) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        int openedId = (int) session.getDataCache().get(CacheKey.WINDOW_OPENED_ID);
        if (packet.getWindowId() != openedId) {
            //Hmm
            closeOpened(session, true);
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        CachedWindow win = session.getWindowCache().get(openedId);
        System.out.println("WIN=" + win.slots.length + ", REQ_SLOT=" + packet.getSlot());
        if (win.size <= packet.getSlot()) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        InventoryTranslator t = TRANSLATORS.get(win.pcType);
        if (t == null) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        win.slots[packet.getSlot()] = packet.getItem(); //Update here
        t.updateSlot(session, win, packet.getSlot());
    }

    public static void updateContent(UpstreamSession session, ServerWindowItemsPacket packet) {
        if (packet.getWindowId() == 0) {
            return;   //We don't process player inventory updates here. 
        }
        if (!session.getDataCache().containsKey(CacheKey.WINDOW_OPENED_ID) || !session.getWindowCache().hasWindow(packet.getWindowId())) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        int openedId = (int) session.getDataCache().get(CacheKey.WINDOW_OPENED_ID);
        if (packet.getWindowId() != openedId) {
            //Hmm
            closeOpened(session, true);
            return;
        }

        CachedWindow win = session.getWindowCache().get(openedId);
        InventoryTranslator t = TRANSLATORS.get(win.pcType);
        if (t == null) {
            session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));
            return;
        }
        win.slots = packet.getItems();
        t.updateContent(session, win);
    }
}
