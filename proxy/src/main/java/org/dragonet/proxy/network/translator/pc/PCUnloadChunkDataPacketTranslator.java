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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;

public class PCUnloadChunkDataPacketTranslator implements IPCPacketTranslator<ServerUnloadChunkPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerUnloadChunkPacket packet) {
        session.getChunkCache().remove(packet.getX(), packet.getZ());
        return null;
    }
}
