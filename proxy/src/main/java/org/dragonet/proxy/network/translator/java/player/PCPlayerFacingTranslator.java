package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerFacingPacket;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@Log4j2
@PCPacketTranslator(packetClass = ServerPlayerFacingPacket.class)
public class PCPlayerFacingTranslator extends PacketTranslator<ServerPlayerFacingPacket> {

    @Override
    public void translate(ProxySession session, ServerPlayerFacingPacket packet) {
        CachedPlayer player = session.getCachedEntity();
        Vector3f position = Vector3f.from(packet.getX(), packet.getY(), packet.getZ());

        CachedEntity targetEntity = session.getEntityCache().getByRemoteId(packet.getTargetEntityId());
        if(targetEntity == null) {
//            log.warn("Target entity is null");
            return;
        }

        //player.rotate(session, Vector3f.from(rotationPitch, rotationYaw, rotationYaw));
    }
}
