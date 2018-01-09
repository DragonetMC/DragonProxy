package org.dragonet.plugin.bukkit.processors;

import org.dragonet.plugin.bukkit.BedrockPacketProcessor;
import org.dragonet.plugin.bukkit.BedrockPlayer;
import org.dragonet.protocol.packets.ContainerClosePacket;

public class ContainerCloseProcessor implements BedrockPacketProcessor<ContainerClosePacket> {
    @Override
    public void process(BedrockPlayer bedrockPlayer, ContainerClosePacket packet) {
        bedrockPlayer.getPlayer().getOpenInventory().close();
    }
}
