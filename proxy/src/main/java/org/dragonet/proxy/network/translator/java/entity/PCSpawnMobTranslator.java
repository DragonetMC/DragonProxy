/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
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
        addEntityPacket.setEntityType(37);

        EntityDataDictionary metadata = addEntityPacket.getMetadata();
        metadata.put(EntityData.NAMETAG, "testing");
        metadata.put(EntityData.ENTITY_AGE, 0);
        metadata.put(EntityData.SCALE, 1f);
        metadata.put(EntityData.MAX_AIR, (short) 400);
        metadata.put(EntityData.AIR, (short) 400);

        //log.warn("SPAWN MOB");

        addEntityPacket.getMetadata().putAll(metadata);

        //session.getBedrockSession().sendPacket(addEntityPacket);
    }
}
