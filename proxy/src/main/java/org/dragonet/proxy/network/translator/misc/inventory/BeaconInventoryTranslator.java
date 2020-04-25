package org.dragonet.proxy.network.translator.misc.inventory;

import com.nukkitx.protocol.bedrock.data.ContainerId;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;

public class BeaconInventoryTranslator extends ContainerInventoryTranslator {

    public BeaconInventoryTranslator(int size) {
        super(BedrockWindowType.BEACON, size);
    }

    @Override
    public void updateInventory(ProxySession session, CachedWindow window) {
        ItemData[] bedrockItems = new ItemData[size];


        InventoryContentPacket contentPacket = new InventoryContentPacket();
        contentPacket.setContainerId(window.getWindowId());
        contentPacket.setContents(bedrockItems);
        session.sendPacket(contentPacket);
    }

    @Override
    public boolean isSlotValid(int slot) {
        return true; // TODO
    }
}
