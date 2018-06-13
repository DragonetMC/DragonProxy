/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.caches;

import com.github.steveice10.packetlib.packet.Packet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.dragonet.api.sessions.IUpstreamSession;

/**
 *
 * @author Epic
 */
public interface IWindowCache {

    public IUpstreamSession getUpstream();

    /**
     * @return the currentTransactionId
     */
    public AtomicInteger getCurrentTransactionId();

    public ICachedWindow getPlayerInventory();

    public Map<Integer, ICachedWindow> getCachedWindows();

    // We do not do translations here, do it in InventoryTranslatorRegister
    public void cacheWindow(ICachedWindow win);

    public ICachedWindow removeWindow(int id);

    public ICachedWindow get(int id);

    public boolean hasWindow(int id);

    public void newCachedPacket(int windowId, Packet packet);

    public Packet[] getCachedPackets(int windowId);
}
