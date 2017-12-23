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
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.MoveEntityPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCEntityHeadLookPacketTranslator implements IPCPacketTranslator<ServerEntityHeadLookPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityHeadLookPacket packet) {

        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            return null;
        }

        entity.headYaw = packet.getHeadYaw();

        MoveEntityPacket pk = new MoveEntityPacket();
        pk.rtid = entity.proxyEid;
        pk.yaw = (byte) (entity.yaw / (360d / 256d));
        pk.headYaw = (byte) (entity.headYaw / (360d / 256d));
        pk.pitch = (byte) (entity.pitch / (360d / 256d));
        pk.position = new Vector3F((float) entity.x, (float) entity.y + entity.peType.getOffset(), (float) entity.z);
        pk.onGround = true;
        return new PEPacket[]{pk};
    }
}
