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

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCEntityPositionPacketTranslator implements PacketTranslator<ServerEntityPositionPacket> {
    public static final PCEntityPositionPacketTranslator INSTANCE = new PCEntityPositionPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityPositionPacket packet) {
        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();

        moveEntityPacket.setRuntimeEntityId(packet.getEntityId());
        moveEntityPacket.setPosition(new Vector3f(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ()));
        moveEntityPacket.setRotation(new Vector3f(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ()));
        moveEntityPacket.setOnGround(packet.isOnGround());
        moveEntityPacket.setTeleported(false);

        session.getBedrockSession().sendPacket(moveEntityPacket);
    }
}
