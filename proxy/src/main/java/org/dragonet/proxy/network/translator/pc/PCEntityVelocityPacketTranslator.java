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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.packets.SetEntityMotionPacket;


public class PCEntityVelocityPacketTranslator implements IPCPacketTranslator<ServerEntityVelocityPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityVelocityPacket packet) {

        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                entity = session.getEntityCache().getClientEntity();
            } else {
                return null;
            }
        }

        entity.motionX = packet.getMotionX();
        entity.motionY = packet.getMotionY();
        entity.motionZ = packet.getMotionZ();

        SetEntityMotionPacket pk = new SetEntityMotionPacket();
        pk.rtid = entity.proxyEid;
        pk.motion = new Vector3F((float) packet.getMotionX(), (float) packet.getMotionY(), (float) packet.getMotionZ());
        return new PEPacket[]{pk};
    }
}
