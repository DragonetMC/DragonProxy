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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.nukkitx.protocol.bedrock.packet.SetEntityMotionPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCEntityVelocityPacketTranslator implements PacketTranslator<ServerEntityVelocityPacket> {
    public static final PCEntityVelocityPacketTranslator INSTANCE = new PCEntityVelocityPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityVelocityPacket packet) {
        SetEntityMotionPacket entityMotion = new SetEntityMotionPacket();
        entityMotion.setRuntimeEntityId(packet.getEntityId());
        entityMotion.setMotion(new Vector3f(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ()));

        session.getUpstream().sendPacket(entityMotion);

    }
}
