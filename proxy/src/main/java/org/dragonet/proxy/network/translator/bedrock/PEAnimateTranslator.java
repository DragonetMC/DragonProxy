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

import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.nukkitx.protocol.bedrock.packet.AnimatePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PEAnimateTranslator implements PacketTranslator<AnimatePacket> {
    public static final PEAnimateTranslator INSTANCE = new PEAnimateTranslator();

    @Override
    public void translate(ProxySession session, AnimatePacket packet) {
        switch(packet.getAction()) {
            case SWING_ARM:
                ClientPlayerSwingArmPacket swingArmPacket = new ClientPlayerSwingArmPacket(Hand.MAIN_HAND);
                session.getDownstream().getSession().send(swingArmPacket);
        }
    }
}
