package org.dragonet.proxy.network.translator.misc.inventory;

import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

public class FurnaceInventoryTranslator extends ContainerInventoryTranslator {

    public FurnaceInventoryTranslator(int size) {
        super(BedrockWindowType.FURNACE, size);
    }

    @Override
    public void updateInventory(ProxySession session, CachedWindow window) {

    }

    @Override
    public void updateSlot(ProxySession session, CachedWindow window, int slot) {

    }
}
