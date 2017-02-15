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

import org.dragonet.proxy.protocol.packet.ChatPacket;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.dragonet.proxy.utilities.PatternChecker;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.packet.Packet;

public class PEChatPacketTranslator implements PEPacketTranslator<ChatPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ChatPacket packet) {
        if (session.getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null) {
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
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_LOGGIN_IN));
                session.getDataCache().remove(CacheKey.AUTHENTICATION_STATE);
                session.authenticate(packet.message); //We NEVER cache password for better security. 
            }
            return null;
        }

        ClientChatPacket pk = new ClientChatPacket(packet.message);
        return new Packet[]{pk};
    }

}
