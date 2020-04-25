package org.dragonet.proxy.network.translator.misc.inventory;

import com.nukkitx.protocol.bedrock.data.ContainerId;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

public class AnvilInventoryTranslator extends ContainerInventoryTranslator {

    public AnvilInventoryTranslator(int size) {
        super(BedrockWindowType.ANVIL, size);

        slotMappings.put(1, 0);
        slotMappings.put(2, 1);
        slotMappings.put(50, 2);
    }

    @Override
    public void updateInventory(ProxySession session, CachedWindow window) {

    }

    @Override
    public void updateSlot(ProxySession session, CachedWindow window, int slot) {

    }
}
