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

import com.github.steveice10.mc.protocol.data.message.TranslationMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.nukkitx.protocol.bedrock.packet.TextPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.configuration.lang.MinecraftLanguage;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;


@Log4j2
@PCPacketTranslator(packetClass = ServerChatPacket.class)
public class PCChatTranslator extends PacketTranslator<ServerChatPacket> {
    public static final PCChatTranslator INSTANCE = new PCChatTranslator();

    @Override
    public void translate(ProxySession session, ServerChatPacket packet) {
        TextPacket textPacket = new TextPacket();

        switch(packet.getType()) {
            case CHAT:
                textPacket.setType(TextPacket.Type.CHAT);
                break;
            case NOTIFICATION:
                textPacket.setType(TextPacket.Type.TIP);
                break;
            case SYSTEM:
                textPacket.setType(TextPacket.Type.SYSTEM);
                break;
            default:
                textPacket.setType(TextPacket.Type.RAW);
                break;
        }

        textPacket.setPlatformChatId("");
        textPacket.setSourceName("");
        textPacket.setXuid("");
        textPacket.setNeedsTranslation(false);

        if(packet.getMessage() instanceof TranslationMessage) {
            textPacket.setMessage(MessageTranslator.translate(((TranslationMessage) packet.getMessage())));
        } else {
            textPacket.setMessage(MessageTranslator.translate(packet.getMessage()));
        }

        session.sendPacket(textPacket);
    }
}
