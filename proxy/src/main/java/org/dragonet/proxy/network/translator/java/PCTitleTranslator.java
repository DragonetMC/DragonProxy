/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.nukkitx.protocol.bedrock.packet.SetTitlePacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCTitleTranslator implements PacketTranslator<ServerTitlePacket> {
    public static PCTitleTranslator INSTANCE = new PCTitleTranslator();

    @Override
    public void translate(ProxySession session, ServerTitlePacket packet) {
        SetTitlePacket bedrockPacket = new SetTitlePacket();
        bedrockPacket.setFadeInTime(packet.getFadeIn());
        bedrockPacket.setFadeOutTime(packet.getFadeOut());
        bedrockPacket.setStayTime(packet.getStay());

        switch(packet.getAction()) {
            case ACTION_BAR:
                bedrockPacket.setType(SetTitlePacket.Type.SET_ACTIONBAR_MESSAGE);
                bedrockPacket.setText(MessageTranslator.translate(packet.getTitle().getText()));
                break;
            case TITLE:
                bedrockPacket.setType(SetTitlePacket.Type.SET_TITLE);
                bedrockPacket.setText(MessageTranslator.translate(packet.getTitle().getText()));
                break;
            case SUBTITLE:
                bedrockPacket.setType(SetTitlePacket.Type.SET_SUBTITLE);
                bedrockPacket.setText(MessageTranslator.translate(packet.getTitle().getText()));
                break;
            case RESET:
                bedrockPacket.setType(SetTitlePacket.Type.RESET_TITLE);
                break;
            case CLEAR:
                bedrockPacket.setType(SetTitlePacket.Type.CLEAR_TITLE);
                break;
        }

        session.sendPacketImmediately(bedrockPacket);
    }
}
