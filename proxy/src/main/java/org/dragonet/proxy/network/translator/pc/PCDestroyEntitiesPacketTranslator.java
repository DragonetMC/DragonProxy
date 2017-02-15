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

import org.dragonet.proxy.protocol.packet.PEPacket;
import org.dragonet.proxy.protocol.packet.RemoveEntityPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;

public class PCDestroyEntitiesPacketTranslator implements PCPacketTranslator<ServerDestroyEntitiesPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerDestroyEntitiesPacket packet) {
        PEPacket[] ret = new PEPacket[packet.getEntityIds().length];
        for (int i = 0; i < ret.length; i++) {
            CachedEntity e = session.getEntityCache().remove(packet.getEntityIds()[i]);
            if (e == null) {
                continue;
            }
            ret[i] = new RemoveEntityPacket();
            ((RemoveEntityPacket) ret[i]).eid = e.eid;
        }
        return ret;
    }

}
