package org.dragonet.proxy.network.translator.misc.inventory;

import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

public class ContainerInventoryTranslator extends IInventoryTranslator {

    public ContainerInventoryTranslator(BedrockWindowType windowType, int size) {
        super(windowType, size);
    }

    @Override
    public void updateInventory(ProxySession session, CachedWindow window) {
        ItemData[] bedrockItems = new ItemData[size];

        for (int i = 0; i < bedrockItems.length; i++) {
            bedrockItems[i] = window.getItem(i);
        }

        InventoryContentPacket contentPacket = new InventoryContentPacket();
        contentPacket.setContainerId(window.getWindowId());
        contentPacket.setContents(bedrockItems);
        session.sendPacket(contentPacket);
    }

    @Override
    public void updateSlot(ProxySession session, CachedWindow window, int slot) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setContainerId(window.getWindowId());
        inventorySlotPacket.setItem(window.getItem(slot));
        inventorySlotPacket.setSlot(slot);
        session.sendPacket(inventorySlotPacket);
    }
}
