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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.nukkitx.protocol.bedrock.packet.SetTimePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCUpdateTimePacketTranslator implements PacketTranslator<ServerUpdateTimePacket> {
    public static final PCUpdateTimePacketTranslator INSTANCE = new PCUpdateTimePacketTranslator();

    @Override
    public void translate(ProxySession session, ServerUpdateTimePacket packet) {
        SetTimePacket setTime = new SetTimePacket();
        setTime.setTime((int) packet.getTime());

        session.getBedrockSession().sendPacket(setTime);
    }
}
