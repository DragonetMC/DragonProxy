/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.packet.AddEntityPacket;
import com.nukkitx.protocol.bedrock.packet.BossEventPacket;
import com.nukkitx.protocol.bedrock.packet.RemoveEntityPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.UUID;

@Log4j2

@PacketRegisterInfo(packet = ServerBossBarPacket.class)
public class PCBossBarTranslator extends PacketTranslator<ServerBossBarPacket> {

    @Override
    public void translate(ProxySession session, ServerBossBarPacket packet) {
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setColor(1);
        bossEventPacket.setOverlay(1);
        bossEventPacket.setDarkenSky(1);
        bossEventPacket.setPlayerUniqueEntityId(session.getCachedEntity().getProxyEid());

        switch(packet.getAction()) {
            case ADD:
                // See the documentation for addFakeEntity() below
                addFakeEntity(session, packet.getUuid());

                bossEventPacket.setTitle(MessageTranslator.translate(packet.getTitle()));
                bossEventPacket.setAction(BossEventPacket.Action.SHOW);
                bossEventPacket.setHealthPercentage(packet.getHealth());
                break;
            case REMOVE:
                removeFakeEntity(session, packet.getUuid());

                bossEventPacket.setAction(BossEventPacket.Action.HIDE);
                break;
            case UPDATE_HEALTH:
                bossEventPacket.setAction(BossEventPacket.Action.HEALTH_PERCENTAGE);
                bossEventPacket.setHealthPercentage(packet.getHealth());
                break;
            case UPDATE_TITLE:
                bossEventPacket.setAction(BossEventPacket.Action.TITLE);
                bossEventPacket.setTitle(MessageTranslator.translate(packet.getTitle()));
                break;
            case UPDATE_STYLE:
                //bossEventPacket.setType(BossEventPacket.Type.OVERLAY);
                //bossEventPacket.setOverlay(0);
                break;
            default:
                log.info(TextFormat.GRAY + "(debug) Unhandled boss bar action: " + packet.getAction().name());
                break;
        }

        Long bossEid = session.getEntityCache().getBossbars().get(packet.getUuid());
        bossEventPacket.setBossUniqueEntityId(bossEid == null ? -1 : bossEid);

        session.sendPacket(bossEventPacket);
    }

    /**
     * This method sends a fake entity to the bedrock client as an entity is needed
     * for the bossbar to be displayed.
     */
    private void addFakeEntity(ProxySession session, UUID uuid) {
        long entityId = session.getEntityCache().newBossBar(uuid);

        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setUniqueEntityId(entityId);
        addEntityPacket.setRuntimeEntityId(entityId);
        addEntityPacket.setIdentifier("minecraft:slime");
        addEntityPacket.setPosition(session.getCachedEntity().getPosition().add(0, 10, 0));
        addEntityPacket.setMotion(Vector3f.ZERO);
        addEntityPacket.setRotation(Vector3f.ZERO);
        addEntityPacket.setEntityType(37);
        addEntityPacket.getMetadata().put(EntityData.SCALE, 0.01f);

        session.sendPacket(addEntityPacket);
    }

    private void removeFakeEntity(ProxySession session, UUID uuid) {
        RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
        removeEntityPacket.setUniqueEntityId(session.getEntityCache().removeBossBar(uuid));

        session.sendPacket(removeEntityPacket);
    }
}
