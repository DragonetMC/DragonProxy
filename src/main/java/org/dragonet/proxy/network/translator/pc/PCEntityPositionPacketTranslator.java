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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.MoveEntityPacket;
import org.dragonet.proxy.utilities.Constants;
import org.dragonet.proxy.utilities.Vector3F;

public class PCEntityPositionPacketTranslator implements IPCPacketTranslator<ServerEntityPositionPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityPositionPacket packet) {
        CachedEntity e = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (e == null) {
            return null;
        }

        e.relativeMove(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ());

        MoveEntityPacket pk = new MoveEntityPacket();
        pk.rtid = e.proxyEid;
        pk.yaw = (byte) (e.yaw / (360d / 256d));
        pk.headYaw = (byte) (e.yaw / (360d / 256d));
        pk.pitch = (byte) (e.pitch / (360d / 256d));
        pk.position = new Vector3F((float) e.x, (float) e.y, (float) e.z);
        if (e.player) {
            pk.position.y += Constants.PLAYER_HEAD_OFFSET;
        }
        return new PEPacket[]{pk};
    }
}
