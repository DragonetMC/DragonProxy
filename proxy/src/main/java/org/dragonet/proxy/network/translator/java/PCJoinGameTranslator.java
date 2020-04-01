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
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTOutputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.data.GamePublishSetting;
import com.nukkitx.protocol.bedrock.data.GameRule;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.SetPlayerGameTypePacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.session.data.AuthState;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.types.BlockTranslator;
import org.dragonet.proxy.network.translator.types.ItemTranslator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Log4j2
@PCPacketTranslator(packetClass = ServerJoinGamePacket.class)
public class PCJoinGameTranslator extends PacketTranslator<ServerJoinGamePacket> {
    public static final PCJoinGameTranslator INSTANCE = new PCJoinGameTranslator();

    private static final CompoundTag EMPTY_TAG = CompoundTagBuilder.builder().buildRootTag();
    public static final byte[] EMPTY_LEVEL_CHUNK_DATA;

    static {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(new byte[258]); // Biomes + Border Size + Extra Data Size

            try (NBTOutputStream stream = NbtUtils.createNetworkWriter(outputStream)) {
                stream.write(EMPTY_TAG);
            }

            EMPTY_LEVEL_CHUNK_DATA = outputStream.toByteArray();
        }catch (IOException e) {
            throw new AssertionError("Unable to generate empty level chunk data");
        }
    }

    @Override
    public void translate(ProxySession session, ServerJoinGamePacket packet) {
        // Cache the player's entity id
        session.getDataCache().put("player_eid", packet.getEntityId());

        session.getEntityCache().clonePlayer(packet.getEntityId(), session.getCachedEntity());

        // TODO: Temporary
        SetPlayerGameTypePacket setPlayerGameTypePacket = new SetPlayerGameTypePacket();
        setPlayerGameTypePacket.setGamemode(packet.getGameMode().ordinal());
        session.sendPacket(setPlayerGameTypePacket);

            Vector3f pos = Vector3f.from(-23, 70, 0);
            int chunkX = pos.getFloorX() >> 4;
            int chunkZ = pos.getFloorX() >> 4;

            for (int x = -3; x < 3; x++) {
                for (int z = -3; z < 3; z++) {
                    LevelChunkPacket data = new LevelChunkPacket();
                    data.setChunkX(chunkX + x);
                    data.setChunkZ(chunkZ + z);
                    data.setSubChunksLength(0);
                    data.setData(EMPTY_LEVEL_CHUNK_DATA);
                    data.setCachingEnabled(false);
                    session.sendPacketImmediately(data);
                }
            }
        //}

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
        session.sendPacketImmediately(playStatus);

        // Add the player to the cache (still need to remove them, but thats a TODO)
        if(session.getDataCache().get("auth_state") != AuthState.AUTHENTICATED) {
            GameProfile profile = new GameProfile(session.getAuthData().getIdentity(), session.getAuthData().getDisplayName());
            CachedPlayer player1 = session.getEntityCache().newPlayer(1, profile);

            session.setCachedEntity(player1);
        }
    }

}
