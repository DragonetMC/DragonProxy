/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.net.packet.minecraft.ChatPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.MessageTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;

public class PCChatPacketTranslator implements PCPacketTranslator<ServerChatPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerChatPacket packet) {
        ChatPacket ret = new ChatPacket();
        /*
         * Reset the chat message so we can parse the JSON again (if needed)
         */
        ret.source = "";
        ret.message = MessageTranslator.translate(packet.getMessage());
        switch (packet.getType()) {
        case CHAT:
            ret.type = ChatPacket.TextType.CHAT;
            break;
        case NOTIFICATION:
        case SYSTEM:
        default:
            ret.type = ChatPacket.TextType.CHAT;
            break;
        }
        return new PEPacket[]{ret};
    }
}
