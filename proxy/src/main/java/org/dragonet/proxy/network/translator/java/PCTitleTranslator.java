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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCTitleTranslator implements PacketTranslator<ServerTitlePacket> {
    public static PCTitleTranslator INSTANCE = new PCTitleTranslator();

    @Override
    public void translate(ProxySession session, ServerTitlePacket packet) {
        SetTitlePacket bedrockPacket = new SetTitlePacket();

        switch(packet.getAction()) {
            case ACTION_BAR:
                bedrockPacket.setType(SetTitlePacket.Type.SET_ACTIONBAR_MESSAGE);
                bedrockPacket.setText(MessageTranslator.translate(packet.getActionBar().getText()));
                break;
            case TITLE:
                bedrockPacket.setType(SetTitlePacket.Type.SET_TITLE);
                bedrockPacket.setText(MessageTranslator.translate(packet.getTitle().getText()));
                break;
            case SUBTITLE:
                bedrockPacket.setType(SetTitlePacket.Type.SET_SUBTITLE);
                bedrockPacket.setText(MessageTranslator.translate(packet.getSubtitle().getText()));
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

        session.getBedrockSession().sendPacketImmediately(bedrockPacket);
    }
}
