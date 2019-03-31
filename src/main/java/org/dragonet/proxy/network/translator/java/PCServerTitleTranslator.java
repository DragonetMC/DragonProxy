/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.nukkitx.protocol.bedrock.packet.SetTitlePacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.UpstreamSession;
import org.dragonet.proxy.network.translator.IPacketTranslator;

public class PCServerTitleTranslator implements IPacketTranslator<ServerTitlePacket> {

    @Override
    public void translate(BedrockSession<UpstreamSession> session, ServerTitlePacket packet) {
        SetTitlePacket bedrockPacket = new SetTitlePacket();

        switch(packet.getAction()) {
            case ACTION_BAR:
                bedrockPacket.setType(SetTitlePacket.Type.SET_ACTIONBAR_MESSAGE);
                bedrockPacket.setText(packet.getActionBar().getText());
                break;
            case TITLE:
                bedrockPacket.setType(SetTitlePacket.Type.SET_TITLE);
                bedrockPacket.setText(packet.getTitle().getText());
                break;
            case SUBTITLE:
                bedrockPacket.setType(SetTitlePacket.Type.SET_SUBTITLE);
                bedrockPacket.setText(packet.getSubtitle().getText());
                break;
            case RESET:
                bedrockPacket.setType(SetTitlePacket.Type.RESET_TITLE);
                break;
            case CLEAR:
                bedrockPacket.setType(SetTitlePacket.Type.CLEAR_TITLE);
                break;
        }

        bedrockPacket.setFadeInTime(packet.getFadeIn());
        bedrockPacket.setFadeOutTime(packet.getFadeOut());
        bedrockPacket.setStayTime(packet.getStay());

        session.sendPacket(bedrockPacket);
    }
}
