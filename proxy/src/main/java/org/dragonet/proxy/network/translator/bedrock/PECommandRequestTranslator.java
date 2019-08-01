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
import com.nukkitx.protocol.bedrock.packet.CommandRequestPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PECommandRequestTranslator implements PacketTranslator<CommandRequestPacket> {
    public static final PECommandRequestTranslator INSTANCE = new PECommandRequestTranslator();

    @Override
    public void translate(ProxySession session, CommandRequestPacket packet) {
        ClientChatPacket chatPacket = new ClientChatPacket(packet.getCommand());
        session.getDownstream().getSession().send(chatPacket);
    }
}
