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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.Text;
import sul.utils.Packet;

public class PCChatPacketTranslator implements PCPacketTranslator<ServerChatPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerChatPacket packet) throws java.lang.IllegalStateException{
        Packet ret;
        
        switch (packet.getType()) {
        case CHAT:
            Text.Raw pk1 = new Text().new Raw(packet.getMessage().getFullText());
            ret = pk1;
            break;
        case NOTIFICATION:
        	Text.Raw pk2 = new Text().new Raw(packet.getMessage().getFullText());
            ret = pk2;
            break;
        case SYSTEM:
        	Text.System pk3 = new Text().new System(packet.getMessage().getFullText());
            ret = pk3;
            break;
        default:
        	Text.Raw pk4 = new Text().new Raw(packet.getMessage().getFullText());
            ret = pk4;
            break;
        }
        
        return new RakNetPacket[]{new RakNetPacket(ret.encode())};
    }
}
