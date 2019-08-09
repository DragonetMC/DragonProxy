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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.world;

import com.flowpowered.math.vector.Vector2f;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.chunk.ChunkData;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCChunkDataTranslator implements PacketTranslator<ServerChunkDataPacket> {
    public static final PCChunkDataTranslator INSTANCE = new PCChunkDataTranslator();

    @Override
    public void translate(ProxySession session, ServerChunkDataPacket packet) {
        Column column = packet.getColumn();

        session.getChunkCache().getChunks().put(new Vector2f(column.getX(), column.getZ()), column);

        ChunkData chunkData = session.getChunkCache().translateChunk(column.getX(), column.getZ());
        if(chunkData != null) {
            LevelChunkPacket levelChunkPacket = chunkData.createChunkPacket();
            levelChunkPacket.setChunkX(column.getX());
            levelChunkPacket.setChunkZ(column.getZ());
            levelChunkPacket.setCachingEnabled(false);

            session.getBedrockSession().sendPacket(levelChunkPacket);
        } else {
            log.warn("ChunkData is null");
        }
    }
}
