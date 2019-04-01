package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCServerEntityHeadlookPacketTranslator implements PacketTranslator<ServerEntityHeadLookPacket> {
    public static final PCServerEntityHeadlookPacketTranslator INSTANCE = new PCServerEntityHeadlookPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityHeadLookPacket packet) {

    }
}
