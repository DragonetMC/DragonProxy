/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.common.data.PocketPotionEffect;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.MobEffectPacket;

public class PCEntityEffectPacketTranslator implements IPCPacketTranslator<ServerEntityEffectPacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerEntityEffectPacket packet) {

        ICachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                entity = session.getEntityCache().getClientEntity();
            } else {
                return null;
            }
        }

        int effectId = MagicValues.value(Integer.class, packet.getEffect());

        PocketPotionEffect effect = PocketPotionEffect.getByID(effectId);
        if (effect == null) {
            System.out.println("Unknown effect ID: " + effectId);
            return null;
        }

        MobEffectPacket eff = new MobEffectPacket();
        eff.rtid = entity.getProxyEid();
        eff.effectId = effect.getEffect();
        if (entity.getEffects().contains(effectId)) {
            eff.eventId = MobEffectPacket.EVENT_MODIFY;
        } else {
            eff.eventId = MobEffectPacket.EVENT_ADD;
            entity.getEffects().add(effectId);
        }
        eff.amplifier = packet.getAmplifier();
        eff.duration = packet.getDuration();
        eff.particles = packet.getShowParticles();
        return new PEPacket[]{eff};
    }
}
