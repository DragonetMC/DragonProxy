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
import java.util.Iterator;

import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.data.entity.meta.EntityMetaData;
import org.dragonet.proxy.data.entity.meta.type.Vector3FMeta;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.SetEntityDataPacket;
import org.dragonet.proxy.protocol.packets.SetEntityLinkPacket;
import org.dragonet.proxy.utilities.DebugTools;
import org.dragonet.proxy.utilities.Vector3F;

public class PCEntitySetPassengerPacketTranslator implements IPCPacketTranslator<ServerEntitySetPassengersPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntitySetPassengersPacket packet) {

        CachedEntity vehicle = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (vehicle == null) {
            return null;
        }

        // process not passenger (dismount)
        Iterator<Long> itr = vehicle.passengers.iterator();
        while (itr.hasNext()) {
            long id = itr.next();
            CachedEntity rider = session.getEntityCache().getByLocalEID(id);

            if (rider == null) {
                continue;
            }
            if (!Arrays.asList(packet.getPassengerIds()).contains(rider.eid)) {

                SetEntityLinkPacket pk = new SetEntityLinkPacket();
                pk.riding = vehicle.proxyEid;
                pk.rider = rider.proxyEid;
                pk.type = SetEntityLinkPacket.TYPE_REMOVE;
                setRiding(session, rider, null);
                pk.unknownByte = 0x00;
                session.sendPacket(pk);

                itr.remove();
                rider.riding = 0;
//                System.out.println("DISMOUNT\n" + DebugTools.getAllFields(pk));
            }
        }

        //process mount action
        boolean piloteSet = false;
        for (int id : packet.getPassengerIds()) {

            CachedEntity rider = session.getEntityCache().getByRemoteEID(id);

            if (rider == null) {
                continue;
            }

            SetEntityLinkPacket pk = new SetEntityLinkPacket();
            pk.riding = vehicle.proxyEid;
            pk.rider = rider.proxyEid;

            if (!piloteSet) {
                piloteSet = true;
                pk.type = SetEntityLinkPacket.TYPE_RIDE;
                setRiding(session, rider, getSeatOffset(rider.peType, 1));
            } else {
                pk.type = SetEntityLinkPacket.TYPE_PASSENGER;
                setRiding(session, rider, getSeatOffset(rider.peType, 2));
            }

            pk.unknownByte = 0x00;
            session.sendPacket(pk);

            vehicle.passengers.add(rider.proxyEid);
            rider.riding = vehicle.proxyEid;
//            System.out.println("MOUNT\n" + DebugTools.getAllFields(pk));

        }
        return null;
    }

    //if offset is null, it's a dismount action
    private void setRiding(UpstreamSession session, CachedEntity rider, Vector3F offset) {
        EntityMetaData peMeta = EntityMetaTranslator.translateToPE(session, rider.pcMeta, rider.peType);
        peMeta.setGenericFlag(EntityMetaData.Constants.DATA_FLAG_RIDING, offset != null);
        if (offset != null) {
            peMeta.set(EntityMetaData.Constants.DATA_RIDER_SEAT_POSITION, new Vector3FMeta(offset));
        }

        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.rtid = rider.proxyEid;
        pk.meta = peMeta;
        session.sendPacket(pk);
    }

    private Vector3F getSeatOffset(EntityType peType, int seat) {
        if (peType != null) {
            switch (peType) {
                case BOAT:
                case MINECART:
                    if (seat > 2) {
                        return null; //DISMOUNT
                    }
                    return new Vector3F(0 + seat / 2, 0.35F, 0 + seat / 2);
                case HORSE:
                    if (seat > 1) {
                        return null; //DISMOUNT
                    }
                    return new Vector3F(0, peType.getOffset(), 0);
            }
        }
        return new Vector3F(0, 0.35F, 0); //default
    }
}
