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
import lombok.Getter;
import org.dragonet.proxy.network.UpstreamSession;
import org.spacehq.packetlib.packet.Packet;

public final class WindowCache {

    @Getter
    private final UpstreamSession upstream;

    private final Map<Integer, CachedWindow> windows = Collections.synchronizedMap(new HashMap<Integer, CachedWindow>());

    private final Map<Integer, List<Packet>> cachedItems = Collections.synchronizedMap(new HashMap<Integer, List<Packet>>());

    public WindowCache(UpstreamSession upstream) {
        this.upstream = upstream;

        CachedWindow inv = new CachedWindow(0, null, 45);
        windows.put(0, inv);
    }

    public CachedWindow getPlayerInventory() {
        return windows.get(0);
    }

    // We do not do translations here, do it in InventoryTranslatorRegister
    public void cacheWindow(CachedWindow win) {
        windows.put(win.windowId, win);
    }

    public CachedWindow removeWindow(int id) {
        return windows.remove(id);
    }

    public CachedWindow get(int id) {
        return windows.get(id);
    }

    public boolean hasWindow(int id) {
        return windows.containsKey(id);
    }

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

    public Packet[] getCachedPackets(int windowId) {
        List<Packet> packets = null;
        packets = cachedItems.remove(windowId);
        if (packets == null) {
            return null;
        }
        return packets.toArray(new Packet[0]);
    }
}
