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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.packets.FullChunkDataPacket;
import org.dragonet.protocol.type.chunk.ChunkData;

public class PCChunkDataPacketTranslator implements IPCPacketTranslator<ServerChunkDataPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerChunkDataPacket packet) {
        //update cache
        session.getChunkCache().update(packet.getColumn());

//        session.getProxy().getGeneralThreadPool().execute(() -> {
//            try {
//                FullChunkDataPacket pePacket = new FullChunkDataPacket();
//                pePacket.x = packet.getColumn().getX();
//                pePacket.z = packet.getColumn().getZ();
//
//                ChunkData chunk = session.getChunkCache().translateChunk(packet.getColumn().getX(), packet.getColumn().getZ());
//                if (chunk != null) {
//                    pePacket.payload = chunk.getBuffer();
//                    session.putCachePacket(pePacket);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
        return null;
    }
}
