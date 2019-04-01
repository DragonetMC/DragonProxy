package org.dragonet.proxy.network.translator.java;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.nukkitx.protocol.bedrock.packet.EntityEventPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket;
import com.nukkitx.protocol.bedrock.packet.SetEntityMotionPacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.UpstreamSession;
import org.dragonet.proxy.network.translator.IPacketTranslator;
import org.dragonet.proxy.util.Converter;

public class PCServerEntityTeleportPacketTranslator implements IPacketTranslator<ServerEntityTeleportPacket> {
    @Override
    public void translate(BedrockSession<UpstreamSession> session, ServerEntityTeleportPacket packet) {
        MoveEntityAbsolutePacket p = new MoveEntityAbsolutePacket();

        p.setRuntimeEntityId(packet.getEntityId());

        p.setPosition(new Vector3f(packet.getX(), packet.getY(), packet.getZ()));

        session.sendPacket(p);
    }
}
