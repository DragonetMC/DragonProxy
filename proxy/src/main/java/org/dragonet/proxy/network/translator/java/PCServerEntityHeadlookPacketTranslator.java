package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.translator.IPacketTranslator;

public class PCServerEntityHeadlookPacketTranslator implements IPacketTranslator<ServerEntityHeadLookPacket> {
    @Override
    public void translate(BedrockSession session, ServerEntityHeadLookPacket packet) {

    }
}
