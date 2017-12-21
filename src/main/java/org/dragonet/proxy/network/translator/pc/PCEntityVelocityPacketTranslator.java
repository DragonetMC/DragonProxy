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
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.SetEntityMotionPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCEntityVelocityPacketTranslator implements IPCPacketTranslator<ServerEntityVelocityPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityVelocityPacket packet) {
        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            return null;
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
