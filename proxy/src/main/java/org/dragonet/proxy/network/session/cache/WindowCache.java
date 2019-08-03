package org.dragonet.proxy.network.session.cache;

import lombok.Getter;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

import java.util.HashMap;
import java.util.Map;

@Getter
public class WindowCache implements Cache {
    // Java window id -> cached window
    private Map<Integer, CachedWindow> windows = new HashMap<>();

    public WindowCache() {
        windows.put(0, new CachedWindow(0, null, 45));
    }

    public CachedWindow getPlayerInventory() {
        return windows.get(0);
    }

    @Override
    public void purge() {
        windows.clear();
    }
}
