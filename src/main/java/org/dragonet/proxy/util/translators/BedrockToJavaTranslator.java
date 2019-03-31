package org.dragonet.dragonproxy.proxy.util.translators;

import com.github.steveice10.packetlib.packet.Packet;
import com.nukkitx.protocol.MinecraftPacket;

public abstract class BedrockToJavaTranslator {
    /**
     * @param p the packet to convert
     * @return the java equivalent of p
     */
    public abstract Packet convert(MinecraftPacket p);
}
