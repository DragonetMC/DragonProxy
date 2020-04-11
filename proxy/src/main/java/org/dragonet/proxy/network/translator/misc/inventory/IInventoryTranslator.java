package org.dragonet.proxy.network.translator.misc.inventory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

@Getter
@RequiredArgsConstructor
public abstract class IInventoryTranslator {
    protected final BedrockWindowType bedrockWindowType;
    protected final int size;

    public abstract void updateInventory(ProxySession session, CachedWindow window);

    public abstract void updateSlot(ProxySession session, CachedWindow window, int slot);
}
