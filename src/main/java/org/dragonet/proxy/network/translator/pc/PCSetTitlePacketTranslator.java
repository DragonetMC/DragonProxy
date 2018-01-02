package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.SetTitlePacket;

public class PCSetTitlePacketTranslator implements IPCPacketTranslator<ServerTitlePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerTitlePacket packet) {
        SetTitlePacket titlePacket = new SetTitlePacket();
        titlePacket.action = packet.getAction();

        switch (titlePacket.action) {
            case ACTION_BAR:
                titlePacket.text = packet.getActionBar().getFullText();
                break;
            case SUBTITLE:
                titlePacket.text = packet.getSubtitle().getFullText();
                break;
            default:
                titlePacket.text = packet.getTitle().getFullText();
                break;
        }

        titlePacket.fadeIn = packet.getFadeIn();
        titlePacket.fadeOut = packet.getFadeOut();
        titlePacket.stay = packet.getStay();

        return new PEPacket[]{titlePacket};
    }

}
