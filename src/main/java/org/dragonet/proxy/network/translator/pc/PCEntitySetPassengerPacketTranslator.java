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
import java.util.Arrays;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.SetEntityLinkPacket;
import org.dragonet.proxy.utilities.DebugTools;

public class PCEntitySetPassengerPacketTranslator implements IPCPacketTranslator<ServerEntitySetPassengersPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntitySetPassengersPacket packet) {
//        for (int id : packet.getPassengerIds()) {
//            System.out.println("ServerEntitySetPassengersPacket entity " + packet.getEntityId() + " rider " + id);
//        }

        CachedEntity vehicle = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (vehicle == null) {
            return null;
        }

        // process not passenger (dismount)
        for (long id : vehicle.passengers) {
            CachedEntity rider = session.getEntityCache().getByLocalEID(id);

            if (rider == null) {
                continue;
            }
            if (!Arrays.asList(packet.getPassengerIds()).contains(rider.eid)) {

                SetEntityLinkPacket pk = new SetEntityLinkPacket();
                pk.riding = vehicle.proxyEid;
                pk.rider = rider.proxyEid;
                pk.type = SetEntityLinkPacket.TYPE_REMOVE;
                pk.unknownByte = 0x00;
                session.sendPacket(pk);
                
                rider.riding = 0;
                System.out.println("DISMOUNT\n" + DebugTools.getAllFields(pk));
            }
        }

        //clean cache
        vehicle.passengers.clear();

        //process mount action
        boolean piloteSet = false;
        for (int id : packet.getPassengerIds()) {

            CachedEntity rider = session.getEntityCache().getByRemoteEID(id);
            if (id == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                rider = session.getEntityCache().getClientEntity();
            }

            if (rider == null) {
                continue;
            }

            SetEntityLinkPacket pk = new SetEntityLinkPacket();
            pk.riding = vehicle.proxyEid;
            pk.rider = rider.proxyEid;
            
            if (!piloteSet) {
                pk.type = SetEntityLinkPacket.TYPE_RIDE;
                piloteSet = true;
            } else {
                pk.type = SetEntityLinkPacket.TYPE_PASSENGER;
            }

            pk.unknownByte = 0x00;
            session.sendPacket(pk);
            
            vehicle.passengers.add(rider.proxyEid);
            rider.riding = vehicle.proxyEid;
            System.out.println("MOUNT\n" + DebugTools.getAllFields(pk));

        }
        return null;
    }
}
