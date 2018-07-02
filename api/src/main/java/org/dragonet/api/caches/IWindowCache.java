/*
 * DragonProxy API
 * Copyright © 2016 Dragonet Foundation (https://github.com/DragonetMC/DragonProxy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
