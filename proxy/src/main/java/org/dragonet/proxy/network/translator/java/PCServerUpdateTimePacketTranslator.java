package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.nukkitx.protocol.bedrock.packet.SetTimePacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCServerUpdateTimePacketTranslator implements PacketTranslator<ServerUpdateTimePacket> {
    public static final PCServerUpdateTimePacketTranslator INSTANCE = new PCServerUpdateTimePacketTranslator();

    @Override
    public void translate(ProxySession session, ServerUpdateTimePacket packet) {
        SetTimePacket p = new SetTimePacket();

        p.setTime((int)packet.getTime());

        session.getUpstream().sendPacket(p);
    }
}
