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
package org.dragonet.proxy.network.translator.java.entity;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataDictionary;
import com.nukkitx.protocol.bedrock.packet.AddEntityPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
public class PCSpawnMobTranslator implements PacketTranslator<ServerSpawnMobPacket> {
    public static final PCSpawnMobTranslator INSTANCE = new PCSpawnMobTranslator();

    @Override
    public void translate(ProxySession session, ServerSpawnMobPacket packet) {
        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setUniqueEntityId(packet.getEntityId());
        addEntityPacket.setRuntimeEntityId(packet.getEntityId());
        addEntityPacket.setIdentifier("minecraft:test");
        addEntityPacket.setPosition(new Vector3f(packet.getX(), packet.getY(), packet.getZ()));
        addEntityPacket.setMotion(new Vector3f(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ()));
        addEntityPacket.setRotation(Vector3f.ZERO);

        EntityDataDictionary metadata = addEntityPacket.getMetadata();
        metadata.put(EntityData.NAMETAG, "testing");
        metadata.put(EntityData.ENTITY_AGE, 0);
        metadata.put(EntityData.SCALE, 1f);
        metadata.put(EntityData.MAX_AIR, (short) 400);
        metadata.put(EntityData.AIR, (short) 0);

        //log.warn("SPAWN MOB");

        addEntityPacket.getMetadata().putAll(metadata);

        //session.getBedrockSession().sendPacket(addEntityPacket);
    }
}
