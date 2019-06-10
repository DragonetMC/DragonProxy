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
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import com.nukkitx.protocol.bedrock.packet.DisconnectPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCDisconnectPacketTranslator implements PacketTranslator<ServerDisconnectPacket> {
    public static final PCDisconnectPacketTranslator INSTANCE = new PCDisconnectPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerDisconnectPacket packet) {
        session.disconnect(packet.getReason().getText());
    }
}

