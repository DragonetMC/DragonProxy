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
package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.data.game.MessageType;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.packet.Packet;
import sul.protocol.pocket101.play.Text.Chat;

public class PEChatPacketTranslator implements PEPacketTranslator<Chat> {

    @Override
    public Packet[] translate(ClientConnection session, Chat packet) {
    	// What is packet.parameters for?
        ServerChatPacket pk = new ServerChatPacket(packet.message, MessageType.CHAT);
        return new Packet[]{pk};
    }

}
