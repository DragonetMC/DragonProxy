package org.dragonet.plugin.bukkit;

import org.dragonet.api.network.PEPacket;

public interface BedrockPacketProcessor<P extends PEPacket> {

    void process(BedrockPlayer bedrockPlayer, P packet);


}
