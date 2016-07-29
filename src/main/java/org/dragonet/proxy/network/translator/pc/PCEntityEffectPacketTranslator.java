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

import org.dragonet.PocketPotionEffect;
import org.dragonet.net.packet.minecraft.MobEffectPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;

public class PCEntityEffectPacketTranslator implements PCPacketTranslator<ServerEntityEffectPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerEntityEffectPacket packet) {
        CachedEntity entity = session.getEntityCache().get(packet.getEntityId());
        if (entity == null) {
            return null;
        }
        int effectId = MagicValues.value(Integer.class, packet.getEffect());

        MobEffectPacket eff = new MobEffectPacket();
        eff.eid = packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID) ? 0 : packet.getEntityId();
        eff.effect = PocketPotionEffect.getByID(effectId);
        if (eff.effect == null) {
            return null; //Not supported
        }
        if (entity.effects.contains(effectId)) {
            eff.action = MobEffectPacket.EffectAction.MODIFY;
        } else {
            eff.action = MobEffectPacket.EffectAction.ADD;
            entity.effects.add(effectId);
        }
        eff.effect.setAmpilifier(packet.getAmplifier());
        eff.effect.setDuration(packet.getDuration());
        eff.effect.setParticles(!packet.getHideParticles());
        return new PEPacket[]{eff};
    }

}
