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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.SpawnExperienceOrb;
import org.dragonet.proxy.utilities.Vector3F;

public class PCSpawnExpOrbPacketTranslator implements IPCPacketTranslator<ServerSpawnExpOrbPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerSpawnExpOrbPacket packet) {
        session.getEntityCache().newEntity(packet);

        SpawnExperienceOrb spawnXpOrb = new SpawnExperienceOrb();
        spawnXpOrb.position = new Vector3F((float) packet.getX(), (float) packet.getY(), (float) packet.getZ());
        spawnXpOrb.count = packet.getExp();

        return new PEPacket[]{spawnXpOrb};
    }

}
