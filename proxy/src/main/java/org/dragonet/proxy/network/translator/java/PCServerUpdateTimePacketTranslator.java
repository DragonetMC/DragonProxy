package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.nukkitx.protocol.bedrock.packet.SetTimePacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.UpstreamSession;
import org.dragonet.proxy.network.translator.IPacketTranslator;

public class PCServerUpdateTimePacketTranslator implements IPacketTranslator<ServerUpdateTimePacket> {
    @Override
    public void translate(BedrockSession<UpstreamSession> session, ServerUpdateTimePacket packet) {
        SetTimePacket p = new SetTimePacket();

        p.setTime((int)packet.getTime());

        session.sendPacket(p);
    }
}
