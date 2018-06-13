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
package org.dragonet.proxy.network.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.steveice10.packetlib.packet.Packet;

import java.util.concurrent.atomic.AtomicInteger;
import org.dragonet.api.caches.ICachedWindow;
import org.dragonet.api.caches.IWindowCache;
import org.dragonet.api.sessions.IUpstreamSession;

import org.dragonet.proxy.network.UpstreamSession;

public final class WindowCache implements IWindowCache {

    private final IUpstreamSession upstream;
    private final Map<Integer, ICachedWindow> windows = Collections.synchronizedMap(new HashMap<>());
    private final Map<Integer, List<Packet>> cachedItems = Collections.synchronizedMap(new HashMap<>());

    private final AtomicInteger currentTransactionId = new AtomicInteger();

    public WindowCache(IUpstreamSession upstream) {
        this.upstream = upstream;

        CachedWindow inv = new CachedWindow(0, null, 45);
        windows.put(0, inv);
    }

    // public
    @Override
    public IUpstreamSession getUpstream() {
        return upstream;
    }

    @Override
    public ICachedWindow getPlayerInventory() {
        return windows.get(0);
    }

    @Override
    public Map<Integer, ICachedWindow> getCachedWindows() {
        return windows;
    }

    // We do not do translations here, do it in InventoryTranslatorRegister
    public void cacheWindow(ICachedWindow win) {
        windows.put(win.getWindowId(), win);
    }

    @Override
    public ICachedWindow removeWindow(int id) {
        return windows.remove(id);
    }

    @Override
    public ICachedWindow get(int id) {
        return windows.get(id);
    }

    @Override
    public boolean hasWindow(int id) {
        return windows.containsKey(id);
    }

    @Override
    public void newCachedPacket(int windowId, Packet packet) {
        List<Packet> packets = null;
        synchronized (cachedItems) {
            packets = cachedItems.get(windowId);
            if (packets == null) {
                packets = new ArrayList<>();
                cachedItems.put(windowId, packets);
            }
        }
        packets.add(packet);
    }

    @Override
    public Packet[] getCachedPackets(int windowId) {
        List<Packet> packets = null;
        packets = cachedItems.remove(windowId);
        if (packets == null)
            return new Packet[]{};
        return packets.toArray(new Packet[0]);
    }

    /**
     * @return the currentTransactionId
     */
    public AtomicInteger getCurrentTransactionId() {
        return currentTransactionId;
    }
}
