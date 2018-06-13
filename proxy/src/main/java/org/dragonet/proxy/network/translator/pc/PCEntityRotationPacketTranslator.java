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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.MoveEntityPacket;


public class PCEntityRotationPacketTranslator implements IPCPacketTranslator<ServerEntityRotationPacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerEntityRotationPacket packet) {

        ICachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                entity = session.getEntityCache().getClientEntity();
            } else {
                return null;
            }
        }

        entity.relativeMove(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ(), packet.getYaw(), packet.getPitch());

        if (entity.isShouldMove()) {
            MoveEntityPacket pk = new MoveEntityPacket();
            pk.rtid = entity.getProxyEid();
            pk.yaw = (byte) (entity.getYaw() / (360d / 256d));
            pk.headYaw = (byte) (entity.getHeadYaw() / (360d / 256d));
            pk.pitch = (byte) (entity.getPitch() / (360d / 256d));
            pk.position = new Vector3F((float) entity.getX(), (float) entity.getY() + entity.getPeType().getOffset(), (float) entity.getZ());
            pk.onGround = packet.isOnGround();
            entity.setShouldMove(false);
            return new PEPacket[]{pk};
        }
        return null;
    }
}
