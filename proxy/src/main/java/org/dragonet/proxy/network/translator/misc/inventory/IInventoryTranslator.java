package org.dragonet.proxy.network.translator.misc.inventory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.data.InventoryActionData;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

@Getter
@RequiredArgsConstructor
public abstract class IInventoryTranslator {
    protected static ItemData UNUSABLE_INVENTORY_SPACE_BLOCK;

    protected final BedrockWindowType bedrockWindowType;
    protected final int size;

    protected BiMap<Integer, Integer> slotMappings = HashBiMap.create();

    static {
        CompoundTagBuilder root = CompoundTagBuilder.builder();
        CompoundTagBuilder display = CompoundTagBuilder.builder();

        display.stringTag("Name", "Unusuable inventory space");
        root.tag(display.build("display"));

        // Barrier block (248 update block)
        UNUSABLE_INVENTORY_SPACE_BLOCK = ItemData.of(-161, (short) 0, 1, root.buildRootTag());
    }

    public abstract void updateInventory(ProxySession session, CachedWindow window);

    public abstract void updateSlot(ProxySession session, CachedWindow window, int slot);

    public boolean isSlotValid(int slot) {
        return true;
    }
}
