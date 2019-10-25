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
package org.dragonet.proxy.network.translator.java.entity;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import com.nukkitx.protocol.bedrock.packet.MobEffectPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.EntityEffectTranslator;

@Log4j2
public class PCEntityEffectTranslator implements PacketTranslator<ServerEntityEffectPacket> {
    public static final PCEntityEffectTranslator INSTANCE = new PCEntityEffectTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityEffectPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            //log.info("(debug) EntityEffectTranslator: Cached entity is null");
            return;
        }

        EntityEffectTranslator.BedrockEffect effect = EntityEffectTranslator.translateToBedrock(packet.getEffect());
        if(effect == null) {
            log.warn("Cannot translate effect: " + packet.getEffect().name());
            return;
        }

        MobEffectPacket mobEffectPacket = new MobEffectPacket();
        mobEffectPacket.setRuntimeEntityId(packet.getEntityId());
        mobEffectPacket.setEffectId(effect.ordinal() + 1); // We add 1 as enums begin at 0
        mobEffectPacket.setAmplifier(packet.getAmplifier());
        mobEffectPacket.setDuration(packet.getDuration());
        mobEffectPacket.setParticles(packet.isShowParticles());

        if(cachedEntity.getEffects().contains(effect)) {
            mobEffectPacket.setEvent(MobEffectPacket.Event.MODIFY);
        } else {
            mobEffectPacket.setEvent(MobEffectPacket.Event.ADD);
        }

        session.sendPacket(mobEffectPacket);

        // Add the effect to the cached entity so we can check it later
        cachedEntity.getEffects().add(effect);
    }
}
