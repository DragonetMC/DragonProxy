/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.caches;

import com.github.steveice10.packetlib.packet.Packet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.api.sessions.IUpstreamSession;

/**
 *
 * @author Epic
 */
public interface IEntityCache {

    public IUpstreamSession getUpstream();

    public Map<Long, ICachedEntity> getEntities();

    public void reset(boolean clear);

    public ICachedEntity getClientEntity();

    public void updateClientEntity(Packet packet);

    public ICachedEntity getByRemoteEID(long eid);

    public ICachedEntity getByLocalEID(long eid);

    public ICachedEntity removeByRemoteEID(long eid);

    public boolean isRemoteEIDPlayerEntity(long eid);

    public AtomicLong getNextAtomicLong();

    public void onTick();
}
