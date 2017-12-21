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

import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import org.dragonet.proxy.protocol.packets.PlayerInputPacket;
import org.dragonet.proxy.utilities.DebugTools;

public class PEPlayerInputPacketTranslator implements IPEPacketTranslator<PlayerInputPacket> {

    public Packet[] translate(UpstreamSession session, PlayerInputPacket packet) {

        //unused for now
        return null;
    }

}
