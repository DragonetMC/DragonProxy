/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.io.buffer.ByteBufferNetOutput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTOutputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.SetPlayerGameTypePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


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

        session.getCachedEntity().setGameMode(packet.getGameMode());

        if(packet.getGameMode() == GameMode.CREATIVE) {
            session.sendCreativeInventory();
        }

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
                    session.sendPacket(data);
                }
            }
        //}

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
        session.sendPacket(playStatus);

        // Send brand
        sendClientBrand(session);

        // Send player data
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerLogin");
        output.writeUTF(session.getAuthData().getDisplayName()); // Display name
        output.writeUTF(session.getAuthData().getXuid()); // XUID
        output.writeUTF(session.getAuthData().getIdentity().toString()); // UUID
        output.writeInt(session.getClientData().getDeviceOs().ordinal());
        output.writeInt(session.getClientData().getUiProfile().ordinal());
        output.writeUTF(session.getClientData().getDeviceModel());
        output.writeUTF(session.getClientData().getGameVersion());
        output.writeUTF(session.getClientData().getLanguageCode());

        session.sendRemotePacket(new ClientPluginMessagePacket("dragonproxy:main", output.toByteArray()));
    }

    /**
     * Send the brand name to the server.
     * This is a way to identify a DragonProxy client vs a vanilla client.
     */
    private void sendClientBrand(ProxySession session) {
        ByteBufferNetOutput brandOutput = new ByteBufferNetOutput(ByteBuffer.allocate(20));
        try {
            brandOutput.writeString("DragonProxy");
        } catch (IOException e) {
            log.warn("Failed to send client brand: " + e.getMessage());
        }
        session.sendRemotePacket(new ClientPluginMessagePacket("minecraft:brand", brandOutput.getByteBuffer().array()));
    }
}
