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
import java.util.Arrays;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.packets.FullChunkDataPacket;
import org.dragonet.common.mcbedrock.protocol.type.chunk.ChunkData;
import org.dragonet.common.mcbedrock.protocol.type.chunk.Section;

public class PCUnloadChunkDataPacketTranslator implements IPCPacketTranslator<ServerUnloadChunkPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerUnloadChunkPacket packet) {
        ChunkData data = new ChunkData();
        data.sections = new Section[16];
        for (int cy = 0; cy < 16; cy++) {
            data.sections[cy] = new Section();
            if (cy < 6)
                Arrays.fill(data.sections[cy].blockIds, (byte) 0);
        }
        data.encode();
        return new PEPacket[]{new FullChunkDataPacket(packet.getX(), packet.getZ(), data.getBuffer())};
    }
}
