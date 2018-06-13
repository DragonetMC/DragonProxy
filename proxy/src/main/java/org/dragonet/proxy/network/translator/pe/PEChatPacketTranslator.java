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

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.api.translators.IPEPacketTranslator;
import org.dragonet.protocol.packets.TextPacket;

public class PEChatPacketTranslator implements IPEPacketTranslator<TextPacket> {

    public Packet[] translate(UpstreamSession session, TextPacket packet) {
        /*if (session.getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null) {
            if (session.getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("email")) {
                if (!PatternChecker.matchEmail(packet.message.trim())) {
                    session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    return null;
                }
                session.getDataCache().put(CacheKey.AUTHENTICATION_EMAIL, packet.message.trim());
                session.getDataCache().put(CacheKey.AUTHENTICATION_STATE, "password");
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_PASSWORD));
            } else if (session.getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("password")) {
                if (session.getDataCache().get(CacheKey.AUTHENTICATION_EMAIL) == null || packet.message.equals(" ")) {
                    session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    return null;
                }
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_LOGGING_IN));
                session.getDataCache().remove(CacheKey.AUTHENTICATION_STATE);
                session.authenticate(packet.message); // We NEVER cache password for better security.
            }
            return null;
        }*/

        ClientChatPacket pk = new ClientChatPacket(packet.message);
        return new Packet[]{pk};
    }
}
