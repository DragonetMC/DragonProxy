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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCEntityPositionRotationPacketTranslator implements PacketTranslator<ServerEntityPositionRotationPacket> {
    public static final PCEntityPositionRotationPacketTranslator INSTANCE = new PCEntityPositionRotationPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityPositionRotationPacket packet) {
        MoveEntityAbsolutePacket p = new MoveEntityAbsolutePacket();

        p.setRuntimeEntityId(packet.getEntityId());

        p.setPosition(new Vector3f(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ()));

        session.getUpstream().sendPacket(p);
    }
}
