package org.dragonet.proxy.network.translator.java;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.nukkitx.protocol.bedrock.packet.SetEntityMotionPacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCServerEntityVelocityPacketTranslator implements PacketTranslator<ServerEntityVelocityPacket> {
    public static final PCServerEntityVelocityPacketTranslator INSTANCE = new PCServerEntityVelocityPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityVelocityPacket packet) {
        SetEntityMotionPacket p = new SetEntityMotionPacket();

        p.setRuntimeEntityId(packet.getEntityId());

        p.setMotion(new Vector3f(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ()));

        session.getUpstream().sendPacket(p);

    }
}
