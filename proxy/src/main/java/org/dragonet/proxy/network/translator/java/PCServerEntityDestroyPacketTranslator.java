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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.nukkitx.protocol.bedrock.packet.RemoveEntityPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCServerEntityDestroyPacketTranslator implements PacketTranslator<ServerEntityDestroyPacket> {
    public static final PCServerEntityDestroyPacketTranslator INSTANCE = new PCServerEntityDestroyPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityDestroyPacket packet) {
        for(int entityId : packet.getEntityIds()) {
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.setUniqueEntityId(entityId);

            session.getBedrockSession().sendPacket(removeEntityPacket);
        }
    }
}
