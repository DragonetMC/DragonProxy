package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.SetTitlePacket;

public class PCSetTitlePacketTranslator implements IPCPacketTranslator<ServerTitlePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerTitlePacket packet) {
        SetTitlePacket titlePacket = new SetTitlePacket();

        switch (packet.getAction()) {
            case ACTION_BAR:
                titlePacket.action = SetTitlePacket.SET_ACTIONBAR;
                titlePacket.text = packet.getActionBar().getFullText();
                break;
            case TITLE:
                titlePacket.action = SetTitlePacket.SET_TITLE;
                titlePacket.text = packet.getTitle().getFullText();
                break;
            case SUBTITLE:
                titlePacket.action = SetTitlePacket.SET_SUBTITLE;
                titlePacket.text = packet.getSubtitle().getFullText();
                break;
            case RESET:
            case CLEAR:
                titlePacket.action = SetTitlePacket.RESET;
                titlePacket.text = "";
                break;
            default:
                return null;
        }

        titlePacket.fadeIn = packet.getFadeIn();
        titlePacket.fadeOut = packet.getFadeOut();
        titlePacket.stay = packet.getStay();

        return new PEPacket[]{titlePacket};
    }

}
