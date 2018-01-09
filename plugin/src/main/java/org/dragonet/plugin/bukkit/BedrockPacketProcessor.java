package org.dragonet.plugin.bukkit;

import org.dragonet.protocol.PEPacket;

public interface BedrockPacketProcessor<P extends PEPacket> {

    void process(BedrockPlayer bedrockPlayer, P packet);


}
