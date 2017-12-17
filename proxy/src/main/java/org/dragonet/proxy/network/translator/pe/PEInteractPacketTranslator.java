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
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.protocol.packets.InteractPacket;

public class PEInteractPacketTranslator implements IPEPacketTranslator<InteractPacket> {

    public PEInteractPacketTranslator() {

    }

    public Packet[] translate(UpstreamSession session, InteractPacket packet) {
        CachedEntity e = session.getEntityCache().getByLocalEID(packet.targetRtid);
        if (e == null) {
            return null;
        }

        switch (packet.type) {
            case InteractPacket.ACTION_RIGHT_CLICK:
                return new Packet[]{new ClientPlayerInteractEntityPacket((int) (e.eid), InteractAction.ATTACK)};
            case InteractPacket.ACTION_LEFT_CLICK:
                return new Packet[]{new ClientPlayerInteractEntityPacket((int) (e.eid), InteractAction.INTERACT)};
        }

        System.out.println("InteractPacket type : " + packet.type + " on " + packet.targetRtid);

        return null;
    }
}
