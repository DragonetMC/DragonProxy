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
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.packet.TextPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PEPacketTranslator;

@PEPacketTranslator(packetClass = TextPacket.class)
public class PETextTranslator extends PacketTranslator<TextPacket> {

    @Override
    public void translate(ProxySession session, TextPacket packet) {
        // Hack for entering commands without the bedrock client suggestions showing up
        if(packet.getMessage().charAt(0) == '.' && packet.getMessage().charAt(1) == '/') {
            ClientChatPacket chatPacket = new ClientChatPacket(packet.getMessage().replace("./", "/"));
            session.sendRemotePacket(chatPacket);
            return;
        }

        ClientChatPacket chatPacket = new ClientChatPacket(packet.getMessage());
        session.sendRemotePacket(chatPacket);
    }
}
