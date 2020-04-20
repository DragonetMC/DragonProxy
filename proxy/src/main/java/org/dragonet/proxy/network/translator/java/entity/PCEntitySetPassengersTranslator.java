/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.entity;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import com.nukkitx.protocol.bedrock.data.EntityLink;
import com.nukkitx.protocol.bedrock.packet.SetEntityLinkPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

import java.util.Arrays;

@Log4j2
@PacketRegisterInfo(packet = ServerEntitySetPassengersPacket.class)
public class PCEntitySetPassengersTranslator extends PacketTranslator<ServerEntitySetPassengersPacket> {

    @Override
    public void translate(ProxySession session, ServerEntitySetPassengersPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            //log.warn("Cached entity is null");
            return;
        }

        // Mounting
        boolean riderFlag = false;
        for(int id : packet.getPassengerIds()) {
            CachedEntity passenger = session.getEntityCache().getByRemoteId(id);
            if(passenger == null) {
                continue;
            }

            EntityLink.Type type = riderFlag ? EntityLink.Type.PASSENGER : EntityLink.Type.RIDER;
            SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
            setEntityLinkPacket.setEntityLink(new EntityLink(cachedEntity.getProxyEid(), passenger.getProxyEid(), type, false));
            session.sendPacket(setEntityLinkPacket);

            // Update metadata and calculate offset
            passenger.setEntityFlag(EntityFlag.RIDING, true); // TODO?
            passenger.getMetadata().put(EntityData.RIDER_SEAT_POSITION, 0); // TODO?
            passenger.sendMetadata(session);

            // TODO: offset

            cachedEntity.getPassengers().add(passenger);
            passenger.setRiding(cachedEntity);

            riderFlag = true;
        }

        // Dismounting
        for(CachedEntity passenger : cachedEntity.getPassengers()) {
            if(Arrays.stream(packet.getPassengerIds()).noneMatch(i -> i == passenger.getProxyEid())) {
                continue;
            }

            SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
            setEntityLinkPacket.setEntityLink(new EntityLink(cachedEntity.getProxyEid(), passenger.getProxyEid(), EntityLink.Type.REMOVE, false));

            session.sendPacket(setEntityLinkPacket);

            cachedEntity.getPassengers().remove(passenger);
            passenger.setRiding(null);
        }
    }
}
