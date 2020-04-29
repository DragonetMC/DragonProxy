package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.data.game.world.WorldBorderAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
import com.nukkitx.math.vector.Vector2f;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.WorldBorder;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@Log4j2
@PacketRegisterInfo(packet = ServerWorldBorderPacket.class)
public class PCWorldBorderTranslator extends PacketTranslator<ServerWorldBorderPacket> {

    @Override
    public void translate(ProxySession session, ServerWorldBorderPacket packet) {
        WorldBorder worldBorder = session.getWorldCache().getWorldBorder();

        if(packet.getAction() != WorldBorderAction.INITIALIZE && worldBorder == null) {
            log.warn("World border (action " + packet.getAction().name() + ") is null");
            return;
        }

        switch(packet.getAction()) {
            case INITIALIZE:
                // should be getCenterZ()
                worldBorder = new WorldBorder(Vector2f.from(packet.getCenterX(), packet.getCenterY()), packet.getRadius(), packet.getOldRadius(), packet.getNewRadius(),
                    packet.getSpeed(), packet.getWarningTime(), packet.getWarningTime());

                session.getWorldCache().setWorldBorder(worldBorder);
                break;
            case SET_SIZE:
                worldBorder.setRadius(packet.getRadius());
                break;
            case LERP_SIZE:
                worldBorder.setOldRadius(packet.getOldRadius());
                worldBorder.setNewRadius(packet.getNewRadius());
                worldBorder.setSpeed(packet.getSpeed());
                break;
            case SET_CENTER:
                // should be getCenterZ()
                worldBorder.setCenter(Vector2f.from(packet.getCenterX(), packet.getCenterY()));
                break;
            case SET_WARNING_TIME:
                worldBorder.setWarningTime(packet.getWarningTime());
                return;
            case SET_WARNING_BLOCKS:
                worldBorder.setWarningBlocks(packet.getWarningBlocks());
                return;
        }

        worldBorder.update();
    }
}
