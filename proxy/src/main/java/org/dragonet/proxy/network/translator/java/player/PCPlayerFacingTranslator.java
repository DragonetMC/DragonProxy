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
            log.warn("Target entity is null");
            return;
        }

        double d0 = player.getPosition().getX() - targetEntity.getPosition().getX();
        double d1 = player.getPosition().getY() - targetEntity.getPosition().getY();
        double d2 = player.getPosition().getZ() - targetEntity.getPosition().getZ();
        double d3 = (double)Math.sqrt(d0 * d0 + d2 * d2);
        double rotationPitch = wrapDegrees((float)(-(Math.atan2(d1, d3) * (double)(180F / (float)Math.PI))));
        double rotationYaw = wrapDegrees((float)(Math.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F);

        //player.rotate(session, Vector3f.from(rotationPitch, rotationYaw, rotationYaw));
    }

    public static double wrapDegrees(double value) {
        value = value % 360.0D;
        if (value >= 180.0D) {
            value -= 360.0D;
        }

        if (value < -180.0D) {
            value += 360.0D;
        }

        return value;
    }
}
