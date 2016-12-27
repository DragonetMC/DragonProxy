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

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.MessageTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.TextPacket;

public class PCChatPacketTranslator implements PCPacketTranslator<ServerChatPacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerChatPacket packet) {
        TextPacket ret = new TextPacket();
        /*
         * Reset the chat message so we can parse the JSON again (if needed)
         */
        ret.source = "";
        ret.message = MessageTranslator.translate(packet.getMessage());
        switch (packet.getType()) {
        case CHAT:
            ret.type = TextPacket.TYPE_CHAT;
            break;
        case NOTIFICATION:
        case SYSTEM:
        default:
            ret.type = TextPacket.TYPE_CHAT;
            break;
        }
        return new DataPacket[]{ret};
    }
}
