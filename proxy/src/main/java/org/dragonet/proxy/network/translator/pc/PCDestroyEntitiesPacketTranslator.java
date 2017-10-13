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
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.bedrock137.play.RemoveEntity;
import sul.utils.Packet;

public class PCDestroyEntitiesPacketTranslator implements PCPacketTranslator<ServerEntityDestroyPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerEntityDestroyPacket packet) {
        Packet[] ret = new Packet[packet.getEntityIds().length];
        for (int i = 0; i < ret.length; i++) {
            CachedEntity e = session.getEntityCache().remove(packet.getEntityIds()[i]);
            if (e == null) {
                continue;
            }
            ret[i] = new RemoveEntity();
            ((RemoveEntity) ret[i]).entityId = e.eid;
        }
        return ret;
    }

}
