package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.api.translators.IPEPacketTranslator;
import org.dragonet.protocol.packets.AnimatePacket;

public class PEAnimatePacketTranslator implements IPEPacketTranslator<AnimatePacket> {

    @Override
    public Packet[] translate(IUpstreamSession session, AnimatePacket packet) {

        switch (packet.action) {
            case AnimatePacket.ANIMATION_SWING_ARM:
                return new Packet[]{new ClientPlayerSwingArmPacket(Hand.MAIN_HAND)};
            default:
                return new Packet[]{};
        }

    }

}
