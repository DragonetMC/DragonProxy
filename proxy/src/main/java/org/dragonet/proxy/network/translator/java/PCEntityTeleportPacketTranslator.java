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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCEntityTeleportPacketTranslator implements PacketTranslator<ServerEntityTeleportPacket> {
    public static final PCEntityTeleportPacketTranslator INSTANCE = new PCEntityTeleportPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityTeleportPacket packet) {
        MoveEntityAbsolutePacket moveEntity = new MoveEntityAbsolutePacket();

        moveEntity.setRuntimeEntityId(packet.getEntityId());
        moveEntity.setPosition(new Vector3f(packet.getX(), packet.getY(), packet.getZ()));
        moveEntity.setOnGround(packet.isOnGround());
        moveEntity.setTeleported(true);

        session.getUpstream().sendPacket(moveEntity);
    }
}
