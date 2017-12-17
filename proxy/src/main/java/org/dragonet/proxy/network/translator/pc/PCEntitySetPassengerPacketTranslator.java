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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.SetEntityLinkPacket;
import org.dragonet.proxy.utilities.DebugTools;

public class PCEntitySetPassengerPacketTranslator implements IPCPacketTranslator<ServerEntitySetPassengersPacket> {

    public PCEntitySetPassengerPacketTranslator() {

    }

    public PEPacket[] translate(UpstreamSession session, ServerEntitySetPassengersPacket packet)
    {
        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID))
                entity = session.getEntityCache().getClientEntity();
            else
                return null;
        }

        SetEntityLinkPacket pk = new SetEntityLinkPacket();
        pk.rider = entity.proxyEid;

        if (packet.getPassengerIds().length == 0) //dismount
        {
            pk.riding = 0;
            pk.type = 0;
            pk.unknownByte = 0x00;
            session.sendPacket(pk);
        }
        else //mount
        {
            boolean piloteSet = false;
            for (int id : packet.getPassengerIds())
            {
                CachedEntity riding = session.getEntityCache().getByRemoteEID(id);

                if (riding == null) {
                    continue;
                }
                
                pk.riding = riding.proxyEid;
                
                if (!piloteSet)
                {
                    pk.type = 1;
                    piloteSet = true;
                }
                else
                {
                    pk.type = 2;
                }

                pk.unknownByte = 0x00;
                session.sendPacket(pk);
                
            }
            
            
        }
        return null;
    }
}
