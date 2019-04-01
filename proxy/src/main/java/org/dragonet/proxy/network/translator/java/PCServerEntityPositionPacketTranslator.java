package org.dragonet.proxy.network.translator.java;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCServerEntityPositionPacketTranslator implements PacketTranslator<ServerEntityPositionPacket> {
    public static final PCServerEntityPositionPacketTranslator INSTANCE = new PCServerEntityPositionPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityPositionPacket packet) {
        MoveEntityAbsolutePacket p = new MoveEntityAbsolutePacket();

        p.setRuntimeEntityId(packet.getEntityId());

        p.setPosition(new Vector3f(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ()));

        session.getUpstream().sendPacket(p);
    }
}
