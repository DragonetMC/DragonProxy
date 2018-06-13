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
package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.translators.IPEPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.InteractPacket;

public class PEInteractPacketTranslator implements IPEPacketTranslator<InteractPacket> {

    public Packet[] translate(IUpstreamSession session, InteractPacket packet) {

        ICachedEntity entity = session.getEntityCache().getByLocalEID(packet.targetRtid);
        if (entity == null) {
            return null;
        }

        switch (packet.type) {
            case InteractPacket.ACTION_RIGHT_CLICK:
                return new Packet[]{new ClientPlayerInteractEntityPacket((int) (entity.getEid()), InteractAction.ATTACK)};
            case InteractPacket.ACTION_LEFT_CLICK:
                return new Packet[]{new ClientPlayerInteractEntityPacket((int) (entity.getEid()), InteractAction.INTERACT)};
            case InteractPacket.ACTION_LEAVE_VEHICLE:
                return new Packet[]{new ClientPlayerStatePacket((int) (entity.getEid()), PlayerState.START_SNEAKING)};
        }

//        System.out.println("InteractPacket type : " + packet.type + " on " + packet.targetRtid);
        return null;
    }
}
