package org.dragonet.proxy.network.translator.misc.inventory;

import com.nukkitx.protocol.bedrock.data.ContainerId;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.misc.ItemTranslator;

public class PlayerInventoryTranslator extends IInventoryTranslator {

    public PlayerInventoryTranslator(int size) {
        super(BedrockWindowType.CHEST, size); // TODO: add a player window type, but im lazy
    }

    @Override
    public void updateInventory(ProxySession session, CachedWindow window) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(ContainerId.INVENTORY);

        ItemData[] contents = new ItemData[40];

        // Hotbar
        for(int i = 36; i < 45; i++) {
            contents[i - 36] = ItemTranslator.translateSlotToBedrock(window.getItems()[i]);
        }

        // Inventory
        for(int i = 9; i < 36; i++) {
            contents[i] = ItemTranslator.translateSlotToBedrock(window.getItems()[i]);
        }

        // Armour
        for(int i = 5; i < 9; i++) {
            contents[i + 31] = ItemTranslator.translateSlotToBedrock(window.getItems()[i]);
        }

        inventoryContentPacket.setContents(contents);
        session.sendPacket(inventoryContentPacket);
    }

    @Override
    public void updateSlot(ProxySession session, CachedWindow window, int slot) {
        updateInventory(session, window); // TODO: Dont send entire inventory
    }
}
