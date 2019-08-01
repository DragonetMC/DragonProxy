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
package org.dragonet.proxy.network.translator.java.player;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.AddPlayerPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
public class PCSpawnPlayerTranslator implements PacketTranslator<ServerSpawnPlayerPacket> {
    public static final PCSpawnPlayerTranslator INSTANCE = new PCSpawnPlayerTranslator();

    @Override
    public void translate(ProxySession session, ServerSpawnPlayerPacket packet) {
        AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
        addPlayerPacket.setUniqueEntityId(packet.getEntityId());
        addPlayerPacket.setRuntimeEntityId(packet.getEntityId());
        addPlayerPacket.setPosition(new Vector3f(packet.getX(), packet.getY(), packet.getZ()));
        addPlayerPacket.setRotation(new Vector3f(packet.getX(), packet.getY(), packet.getZ()));
        addPlayerPacket.setMotion(new Vector3f(packet.getX(), packet.getY(), packet.getZ()));
        addPlayerPacket.setUsername(session.getAuthData().getDisplayName());
        addPlayerPacket.setUuid(session.getAuthData().getIdentity());
        addPlayerPacket.setDeviceId("");
        addPlayerPacket.setHand(ItemData.AIR);
        addPlayerPacket.setCommandPermission(0);
        addPlayerPacket.setPlayerFlags(0);
        addPlayerPacket.setCustomFlags(0);
        addPlayerPacket.setWorldFlags(0);
        addPlayerPacket.setPlayerPermission(0);
        addPlayerPacket.setPlatformChatId("");

        // TODO: yaw and pitch

        log.warn("SPAWN PLAYER");
        //session.getBedrockSession().sendPacketImmediately(addPlayerPacket);
    }
}
