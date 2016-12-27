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

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;

import cn.nukkit.network.protocol.DataPacket;

public class PCSpawnObjectPacketTranslator implements PCPacketTranslator<ServerSpawnObjectPacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerSpawnObjectPacket packet) {
        if(packet.getType() == ObjectType.ITEM){
            //Currently only handles item data
            CachedEntity futureEntity = session.getEntityCache().newObject(packet);
            //This crap needs entity meta to be completed so we have to wait. 
            return null;
        }
        return null;
    }

}
