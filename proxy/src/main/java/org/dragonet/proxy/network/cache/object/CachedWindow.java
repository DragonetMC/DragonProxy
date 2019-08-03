package org.dragonet.proxy.network.cache.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import lombok.Data;

@Data
public class CachedWindow {
    private final int windowId;

    private ItemStack[] items;
    private WindowType windowType;

    private boolean open = false;

    public CachedWindow(int windowId, ItemStack[] items, int size) {
        this.windowId = windowId;
        this.items = items;
        this.items = new ItemStack[size];
    }
}
