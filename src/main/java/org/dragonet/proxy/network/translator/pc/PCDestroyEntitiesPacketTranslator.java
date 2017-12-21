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
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.RemoveEntityPacket;

public class PCDestroyEntitiesPacketTranslator implements IPCPacketTranslator<ServerEntityDestroyPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityDestroyPacket packet) {
        PEPacket[] ret = new PEPacket[packet.getEntityIds().length];
        for (int i = 0; i < ret.length; i++) {
            CachedEntity e = session.getEntityCache().removeByRemoteEID(packet.getEntityIds()[i]);
            if (e == null) {
                continue;
            }
            ret[i] = new RemoveEntityPacket();
            ((RemoveEntityPacket) ret[i]).eid = e.proxyEid;
        }
        return ret;
    }
}
