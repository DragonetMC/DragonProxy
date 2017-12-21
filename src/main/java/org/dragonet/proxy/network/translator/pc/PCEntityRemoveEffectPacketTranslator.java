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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.MobEffectPacket;

public class PCEntityRemoveEffectPacketTranslator implements IPCPacketTranslator<ServerEntityRemoveEffectPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityRemoveEffectPacket packet) {
        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            return null;
        }
        int effectId = MagicValues.value(Integer.class, packet.getEffect());
        if (!entity.effects.contains(effectId)) {
            return null;
        }
        MobEffectPacket eff = new MobEffectPacket();
        eff.rtid = packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID) ? 1L
                : entity.proxyEid;
        eff.eventId = MobEffectPacket.EVENT_REMOVE;
        return new PEPacket[]{eff};
    }
}
