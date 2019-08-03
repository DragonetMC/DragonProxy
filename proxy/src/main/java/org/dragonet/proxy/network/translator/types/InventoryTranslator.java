package org.dragonet.proxy.network.translator.types;

import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.nukkitx.protocol.bedrock.data.ContainerId;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.session.ProxySession;

public class InventoryTranslator {

    /**
     * Sends the items from the players cached inventory to the client
     */
    public static void sendPlayerInventory(ProxySession session) {
        CachedWindow cachedWindow = session.getWindowCache().getPlayerInventory();

        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(ContainerId.INVENTORY);

        ItemData[] contents = new ItemData[40];

        // Hotbar
        for(int i = 36; i < 45; i++) {
            contents[i - 36] = ItemTranslator.translateSlotToBedrock(cachedWindow.getItems()[i]);
        }

        // Inventory
        for(int i = 9; i < 36; i++) {
            contents[i] = ItemTranslator.translateSlotToBedrock(cachedWindow.getItems()[i]);
        }

        // Armour
        for(int i = 5; i < 9; i++) {
            contents[i + 31] = ItemTranslator.translateSlotToBedrock(cachedWindow.getItems()[i]);
        }

        inventoryContentPacket.setContents(contents);
        session.getBedrockSession().sendPacket(inventoryContentPacket);
    }

    public static void updateSlot(ProxySession session, ServerSetSlotPacket packet) {

    }
}
