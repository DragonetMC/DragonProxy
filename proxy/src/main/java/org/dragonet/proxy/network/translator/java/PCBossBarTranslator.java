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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataDictionary;
import com.nukkitx.protocol.bedrock.packet.AddEntityPacket;
import com.nukkitx.protocol.bedrock.packet.BossEventPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCBossBarTranslator implements PacketTranslator<ServerBossBarPacket> {
    public static final PCBossBarTranslator INSTANCE = new PCBossBarTranslator();

    @Override
    public void translate(ProxySession session, ServerBossBarPacket packet) {
        // NOTE: see https://github.com/DragonetMC/DragonProxy/issues/424
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.setColor(1);
        bossEventPacket.setOverlay(1);
        bossEventPacket.setDarkenSky(1);
        bossEventPacket.setPlayerUniqueEntityId(1); // player eid
        bossEventPacket.setBossUniqueEntityId(2); // TODO

        switch(packet.getAction()) {
            case ADD:
                // See the documentation for addFakeEntity() below
                addFakeEntity(session);

                bossEventPacket.setTitle(MessageTranslator.translate(packet.getTitle().getFullText()));
                bossEventPacket.setType(BossEventPacket.Type.SHOW);
                bossEventPacket.setHealthPercentage(packet.getHealth());
                break;
            case REMOVE:
                bossEventPacket.setType(BossEventPacket.Type.HIDE);
                break;
            case UPDATE_HEALTH:
                bossEventPacket.setType(BossEventPacket.Type.HEALTH_PERCENTAGE);
                bossEventPacket.setHealthPercentage(packet.getHealth());
                break;
            case UPDATE_TITLE:
                bossEventPacket.setType(BossEventPacket.Type.TITLE);
                bossEventPacket.setTitle(MessageTranslator.translate(packet.getTitle().getFullText()));
                break;
            case UPDATE_STYLE:
                //bossEventPacket.setType(BossEventPacket.Type.OVERLAY);
                //bossEventPacket.setOverlay(0);
                break;
            default:
                log.info(TextFormat.GRAY + "(debug) Unhandled boss bar action: " + packet.getAction().name());
        }

        session.sendPacket(bossEventPacket);
    }

    /**
     * This method sends a fake entity to the bedrock client as an entity is needed
     * for the bossbar to be displayed.
     *
     * In the future maybe this could be tied to a real entity, once the spawn mob translator
     * is finished.
     */
    private void addFakeEntity(ProxySession session) {
        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setUniqueEntityId(2);
        addEntityPacket.setRuntimeEntityId(2);
        addEntityPacket.setIdentifier("minecraft:slime");
        addEntityPacket.setPosition(new Vector3f(0, 60, 0));
        addEntityPacket.setMotion(new Vector3f(0, 0, 0));
        addEntityPacket.setRotation(Vector3f.ZERO);
        addEntityPacket.setEntityType(37);

        EntityDataDictionary metadata = new EntityDataDictionary();
        metadata.put(EntityData.SCALE, 0);
        addEntityPacket.getMetadata().putAll(metadata);

        session.sendPacket(addEntityPacket);
    }
}
