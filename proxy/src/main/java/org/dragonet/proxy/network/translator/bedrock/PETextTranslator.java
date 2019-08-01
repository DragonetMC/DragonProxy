/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.packet.TextPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PETextTranslator implements PacketTranslator<TextPacket> {
    public static final PETextTranslator INSTANCE = new PETextTranslator();

    @Override
    public void translate(ProxySession session, TextPacket packet) {
        if(packet.getMessage().charAt(0) == '.' && packet.getMessage().charAt(1) == '/') {
            ClientChatPacket chatPacket = new ClientChatPacket(packet.getMessage().replace("./", "/"));
            session.getDownstream().getSession().send(chatPacket);
            return;
        }

        ClientChatPacket chatPacket = new ClientChatPacket(packet.getMessage());
        session.getDownstream().getSession().send(chatPacket);
    }
}
