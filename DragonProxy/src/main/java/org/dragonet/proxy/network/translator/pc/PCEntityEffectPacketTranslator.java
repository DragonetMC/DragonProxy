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

import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MobEffectPacket;

public class PCEntityEffectPacketTranslator implements PCPacketTranslator<ServerEntityEffectPacket> {

    @Override
    public DataPacket[] translate(ClientConnection session, ServerEntityEffectPacket packet) {
        CachedEntity entity = session.getEntityCache().get(packet.getEntityId());
        if (entity == null) {
            return null;
        }
        int effectId = MagicValues.value(Integer.class, packet.getEffect());

        MobEffectPacket eff = new MobEffectPacket();
        eff.eid = packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID) ? 0 : packet.getEntityId();
        eff.effectId = effectId;
        if (eff.effectId == -1) {// Is this the correct way to do this?
            return null; //Not supported
        }
        if (entity.effects.contains(effectId)) {
            eff.eventId = MobEffectPacket.EVENT_MODIFY;
        } else {
            eff.eventId = MobEffectPacket.EVENT_ADD;
            entity.effects.add(effectId);
        }
        eff.amplifier = packet.getAmplifier();
        eff.duration = packet.getDuration();
        eff.particles = packet.getShowParticles();
        return new DataPacket[]{eff};
    }

}
