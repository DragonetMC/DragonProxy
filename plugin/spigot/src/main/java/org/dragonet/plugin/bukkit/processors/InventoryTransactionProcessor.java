package org.dragonet.plugin.bukkit.processors;

import org.dragonet.plugin.bukkit.BedrockPacketProcessor;
import org.dragonet.plugin.bukkit.BedrockPlayer;
import org.dragonet.protocol.packets.InventoryTransactionPacket;

public class InventoryTransactionProcessor implements BedrockPacketProcessor<InventoryTransactionPacket> {
    @Override
    public void process(BedrockPlayer bedrockPlayer, InventoryTransactionPacket packet) {

    }
}
