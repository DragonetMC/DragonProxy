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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.RemoveEntity;

public class PCDestroyEntitiesPacketTranslator implements PCPacketTranslator<ServerEntityDestroyPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerEntityDestroyPacket packet) {
    	RakNetPacket[] ret = new RakNetPacket[packet.getEntityIds().length];
        for (int i = 0; i < ret.length; i++) {
            CachedEntity e = session.getEntityCache().remove(packet.getEntityIds()[i]);
            if (e == null) {
                continue;
            }
            RemoveEntity re = new RemoveEntity();
            re.entityId = e.eid;
            
            ret[i] = new RakNetPacket(re.encode());
        }
        return ret;
    }

}
