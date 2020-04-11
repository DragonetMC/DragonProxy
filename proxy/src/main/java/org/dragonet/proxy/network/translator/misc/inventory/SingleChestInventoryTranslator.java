package org.dragonet.proxy.network.translator.misc.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.misc.ItemTranslator;

@Log4j2
public class SingleChestInventoryTranslator extends IInventoryTranslator {

    public SingleChestInventoryTranslator(int size) {
        super(BedrockWindowType.CHEST, size);
    }

    @Override
    public void updateInventory(ProxySession session, CachedWindow window) {
        ItemData[] bedrockItems = new ItemData[size];
        for (int i = 0; i < bedrockItems.length; i++) {
            ItemStack item = window.getItems()[i];
            bedrockItems[i] = ItemTranslator.translateSlotToBedrock(item);
        }

        InventoryContentPacket contentPacket = new InventoryContentPacket();
        contentPacket.setContainerId(window.getWindowId());
        contentPacket.setContents(bedrockItems);
        session.sendPacket(contentPacket);

//        log.warn("update inventory");
    }

    @Override
    public void updateSlot(ProxySession session, CachedWindow window, int slot) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setContainerId(window.getWindowId());
        inventorySlotPacket.setItem(ItemTranslator.translateSlotToBedrock(window.getItems()[slot]));
        inventorySlotPacket.setSlot(slot);
        session.sendPacket(inventorySlotPacket);

//        log.warn("update slot");
    }
}
