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
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.type.object.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;

import net.marfgamer.jraknet.RakNetPacket;

public class PCSpawnObjectPacketTranslator implements PCPacketTranslator<ServerSpawnObjectPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerSpawnObjectPacket packet) {
        if(packet.getType() == ObjectType.ITEM){
            //Currently only handles item data
            session.getEntityCache().newObject(packet);
            //This crap needs entity meta to be completed so we have to wait. 
            return new RakNetPacket[0];
        }
        return new RakNetPacket[0];
    }

}
