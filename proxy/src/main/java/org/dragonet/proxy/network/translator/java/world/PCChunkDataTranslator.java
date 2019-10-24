/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.chunk.ChunkData;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCChunkDataTranslator implements PacketTranslator<ServerChunkDataPacket> {
    public static final PCChunkDataTranslator INSTANCE = new PCChunkDataTranslator();

    @Override
    public void translate(ProxySession session, ServerChunkDataPacket packet) {
        Column column = packet.getColumn();

        session.getChunkCache().getChunks().put(Vector2f.from(column.getX(), column.getZ()), column);

        NetworkChunkPublisherUpdatePacket chunkPublisherUpdatePacket = new NetworkChunkPublisherUpdatePacket();
        chunkPublisherUpdatePacket.setPosition(session.getCachedEntity().getPosition().toInt());
        chunkPublisherUpdatePacket.setRadius(8 << 4);
        session.sendPacket(chunkPublisherUpdatePacket);

        ChunkData chunkData = session.getChunkCache().translateChunk(column.getX(), column.getZ());
        if(chunkData != null) {
            LevelChunkPacket levelChunkPacket = chunkData.createChunkPacket();
            levelChunkPacket.setCachingEnabled(false);

            session.sendPacket(levelChunkPacket);
        } else {
            log.warn("ChunkData is null");
        }
    }
}
