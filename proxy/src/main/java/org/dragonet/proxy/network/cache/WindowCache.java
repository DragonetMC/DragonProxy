package org.dragonet.proxy.network.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.network.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.ProxySession;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class WindowCache implements Cache {

    @Override
    public void purge() {

    }
}
