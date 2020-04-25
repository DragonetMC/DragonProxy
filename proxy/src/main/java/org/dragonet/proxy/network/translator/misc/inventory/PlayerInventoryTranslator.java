package org.dragonet.proxy.network.translator.misc.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.nukkitx.protocol.bedrock.data.ContainerId;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;

public class PlayerInventoryTranslator extends IInventoryTranslator {

    public PlayerInventoryTranslator(int size) {
        super(BedrockWindowType.CHEST, size); // TODO: add a player window type, but im lazy
    }

    @Override
    public void updateInventory(ProxySession session, CachedWindow window) {
        sendInventory(session, window);
        sendArmor(session, window);
        sendOffhand(session, window);
        sendCrafting(session, window);
    }

    @Override
    public void updateSlot(ProxySession session, CachedWindow window, int slot) {
        updateInventory(session, window); // TODO: Dont send entire inventory
    }

    public void sendInventory(ProxySession session, CachedWindow window) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(ContainerId.INVENTORY);
        ItemData[] contents = new ItemData[36];

        // Hotbar
        for(int i = 36; i < 45; i++) {
            contents[i - 36] = window.getItem(i);
        }

        // Inventory
        for(int i = 9; i < 36; i++) {
            if(session.getCachedEntity().getGameMode() == GameMode.SPECTATOR) {
                contents[i] = UNUSABLE_INVENTORY_SPACE_BLOCK;
            } else {
                contents[i] = window.getItem(i);
            }
        }

        inventoryContentPacket.setContents(contents);
        session.sendPacket(inventoryContentPacket);
    }

    public void sendArmor(ProxySession session, CachedWindow window) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(ContainerId.ARMOR);
        ItemData[] contents = new ItemData[4];

        for(int i = 5; i < 9; i++) {
            contents[i - 5] = window.getItem(i);
        }

        inventoryContentPacket.setContents(contents);
        session.sendPacket(inventoryContentPacket);
    }

    public void sendOffhand(ProxySession session, CachedWindow window) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(ContainerId.OFFHAND);
        inventoryContentPacket.setContents(new ItemData[]{window.getItem(45)});
        session.sendPacket(inventoryContentPacket);
    }

    public void sendCrafting(ProxySession session, CachedWindow window) {
        for(int i = 0; i < 5; i++) {
            InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
            inventorySlotPacket.setContainerId(ContainerId.CURSOR);
            inventorySlotPacket.setSlot(i + 27);

            if(session.getCachedEntity().getGameMode() == GameMode.CREATIVE || session.getCachedEntity().getGameMode() == GameMode.SPECTATOR) {
                inventorySlotPacket.setItem(UNUSABLE_INVENTORY_SPACE_BLOCK);
            } else {
                inventorySlotPacket.setItem(window.getItem(i));
            }
            session.sendPacket(inventorySlotPacket);
        }
    }

}
