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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.MoveEntityAbsolutePacket;


public class PCEntityHeadLookPacketTranslator implements IPCPacketTranslator<ServerEntityHeadLookPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityHeadLookPacket packet) {

        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                entity = session.getEntityCache().getClientEntity();
            } else {
                return null;
            }
        }

        entity.headYaw = packet.getHeadYaw();

        MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
        pk.rtid = entity.proxyEid;
        pk.yaw = (byte) (entity.yaw / (360d / 256d));
        pk.headYaw = (byte) (entity.headYaw / (360d / 256d));
        pk.pitch = (byte) (entity.pitch / (360d / 256d));
        pk.position = new Vector3F((float) entity.x, (float) entity.y + entity.peType.getOffset(), (float) entity.z);
        pk.onGround = true;
        return new PEPacket[]{pk};
    }
}
