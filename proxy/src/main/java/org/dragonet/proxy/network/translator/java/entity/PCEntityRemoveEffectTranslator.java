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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import com.nukkitx.protocol.bedrock.packet.MobEffectPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.EntityEffectTranslator;

@Log4j2
public class PCEntityRemoveEffectTranslator implements PacketTranslator<ServerEntityRemoveEffectPacket> {
    public static final PCEntityRemoveEffectTranslator INSTANCE = new PCEntityRemoveEffectTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityRemoveEffectPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getById(packet.getEntityId());
        if(cachedEntity == null) {
            log.warn("Cached entity is null");
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
        mobEffectPacket.setEvent(MobEffectPacket.Event.REMOVE);
        mobEffectPacket.setParticles(false);
        mobEffectPacket.setDuration(0);
        mobEffectPacket.setAmplifier(0);

        session.getBedrockSession().sendPacket(mobEffectPacket);

        // Remove the effect from the cached entity
        cachedEntity.getEffects().remove(effect);
    }
}
