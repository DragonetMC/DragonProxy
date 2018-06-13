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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.RemoveEntityPacket;

public class PCDestroyEntitiesPacketTranslator implements IPCPacketTranslator<ServerEntityDestroyPacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerEntityDestroyPacket packet) {
        PEPacket[] ret = new PEPacket[packet.getEntityIds().length];
        for (int i = 0; i < ret.length; i++) {
            ICachedEntity e = session.getEntityCache().removeByRemoteEID(packet.getEntityIds()[i]);
            if (e == null) {
                continue;
            }
            ret[i] = new RemoveEntityPacket();
            ((RemoveEntityPacket) ret[i]).eid = e.getProxyEid();
        }
        return ret;
    }
}
