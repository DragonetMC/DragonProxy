/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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

import com.github.steveice10.mc.protocol.data.message.ChatColor;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.nukkitx.protocol.bedrock.packet.SetTitlePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;


@Log4j2
@PacketRegisterInfo(packet = ServerTitlePacket.class)
public class PCTitleTranslator extends PacketTranslator<ServerTitlePacket> {
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
                bedrockPacket.setText(MessageTranslator.translate(packet.getTitle()));
                break;
            case TITLE:
                //Otherwise title is black when no colour specified
                if(packet.getTitle().getStyle().getColor() == ChatColor.NONE) {
                    packet.getTitle().getStyle().setColor(ChatColor.WHITE);
                }

                //Change black to none other wise title background is also black
                if(packet.getTitle().getStyle().getColor() == ChatColor.BLACK) {
                    packet.getTitle().getStyle().setColor(ChatColor.NONE);
                }

                bedrockPacket.setType(SetTitlePacket.Type.SET_TITLE);
                bedrockPacket.setText(MessageTranslator.translate(packet.getTitle()));
                break;
            case SUBTITLE:
                bedrockPacket.setType(SetTitlePacket.Type.SET_SUBTITLE);
                bedrockPacket.setText(MessageTranslator.translate(packet.getTitle()));
                break;
            case RESET:
                bedrockPacket.setType(SetTitlePacket.Type.RESET_TITLE);
                break;
            case CLEAR:
                bedrockPacket.setType(SetTitlePacket.Type.CLEAR_TITLE);
                break;
        }

        session.sendPacket(bedrockPacket);
    }
}
