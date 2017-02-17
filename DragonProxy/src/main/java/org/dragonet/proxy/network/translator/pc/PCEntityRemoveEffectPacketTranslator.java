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
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.MobEffect;

public class PCEntityRemoveEffectPacketTranslator implements PCPacketTranslator<ServerEntityRemoveEffectPacket> {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, ServerEntityRemoveEffectPacket packet) {
        CachedEntity entity = session.getEntityCache().get(packet.getEntityId());
        if (entity == null) {
            return new sul.utils.Packet[0];
        }
        int effectId = MagicValues.value(Integer.class, packet.getEffect());
        if (!entity.effects.contains(effectId)) {
            return new sul.utils.Packet[0];
        }
        MobEffect eff = new MobEffect();
        eff.entityId = packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID) ? 0 : packet.getEntityId();
        eff.eventId = MobEffect.REMOVE;
        
        return new sul.utils.Packet[] {eff};
    }

}
