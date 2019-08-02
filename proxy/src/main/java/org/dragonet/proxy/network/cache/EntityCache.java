package org.dragonet.proxy.network.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.network.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.ProxySession;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class EntityCache implements Cache {
    @Getter
    private Map<Long, CachedEntity> entities = new HashMap<>();

    private int fakePlayersIds;

    public CachedEntity getById(long entityId) {
        // TODO: convert to proxy entity id first?
        return entities.get(entityId);
    }

    public int nextFakePlayerid() {
        return fakePlayersIds++;
    }

    @Override
    public void purge() {
        entities.clear();
    }
}
