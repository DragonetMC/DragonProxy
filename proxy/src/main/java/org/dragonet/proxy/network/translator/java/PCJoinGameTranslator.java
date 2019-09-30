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
package org.dragonet.proxy.network.translator.java;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector3i;
import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.nbt.stream.NBTOutputStream;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.data.GamePublishSetting;
import com.nukkitx.protocol.bedrock.data.GameRule;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.session.data.AuthState;
import org.dragonet.proxy.network.translator.PacketTranslator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCJoinGameTranslator implements PacketTranslator<ServerJoinGamePacket> {
    public static final PCJoinGameTranslator INSTANCE = new PCJoinGameTranslator();

    private static final CompoundTag EMPTY_TAG = CompoundTagBuilder.builder().buildRootTag();
    private static final byte[] EMPTY_LEVEL_CHUNK_DATA;

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

        //if(session.getDataCache().get("auth_state") != AuthState.AUTHENTICATED) {
            StartGamePacket startGamePacket = new StartGamePacket();
            startGamePacket.setUniqueEntityId(packet.getEntityId());
            startGamePacket.setRuntimeEntityId(packet.getEntityId());
            startGamePacket.setPlayerGamemode(packet.getGameMode().ordinal());
            startGamePacket.setPlayerPosition(new Vector3f(-23, 70, 0)); // Hypixel bedwars lobby spawn
            startGamePacket.setRotation(new Vector2f(1, 1));

            startGamePacket.setSeed(1111);
            startGamePacket.setDimensionId(0);
            startGamePacket.setGeneratorId(0);
            startGamePacket.setLevelGamemode(packet.getGameMode().ordinal());
            startGamePacket.setDifficulty(0);
            startGamePacket.setDefaultSpawn(new Vector3i(-23, 70, 0));
            startGamePacket.setAcheivementsDisabled(true);
            startGamePacket.setTime(0);
            startGamePacket.setEduLevel(false);
            startGamePacket.setEduFeaturesEnabled(false);
            startGamePacket.setRainLevel(0);
            startGamePacket.setLightningLevel(0);
            startGamePacket.setMultiplayerGame(true);
            startGamePacket.setBroadcastingToLan(true);
            startGamePacket.getGamerules().add((new GameRule<>("showcoordinates", true)));
            startGamePacket.setPlatformBroadcastMode(GamePublishSetting.PUBLIC);
            startGamePacket.setXblBroadcastMode(GamePublishSetting.PUBLIC);
            startGamePacket.setCommandsEnabled(true);
            startGamePacket.setTexturePacksRequired(false);
            startGamePacket.setBonusChestEnabled(false);
            startGamePacket.setStartingWithMap(false);
            startGamePacket.setTrustingPlayers(true);
            startGamePacket.setDefaultPlayerPermission(1);
            startGamePacket.setServerChunkTickRange(4);
            startGamePacket.setBehaviorPackLocked(false);
            startGamePacket.setResourcePackLocked(false);
            startGamePacket.setFromLockedWorldTemplate(false);
            startGamePacket.setUsingMsaGamertagsOnly(false);
            startGamePacket.setFromWorldTemplate(false);
            startGamePacket.setWorldTemplateOptionLocked(false);

            startGamePacket.setLevelId("world test");
            startGamePacket.setWorldName("world");
            startGamePacket.setPremiumWorldTemplateId("00000000-0000-0000-0000-000000000000");
            startGamePacket.setCurrentTick(0);
            startGamePacket.setEnchantmentSeed(0);
            startGamePacket.setMultiplayerCorrelationId("");

            startGamePacket.setCachedPalette(DragonProxy.INSTANCE.getPaletteManager().getCachedPalette());
            startGamePacket.setItemEntries(DragonProxy.INSTANCE.getPaletteManager().getItemEntries());

            // TODO: 01/04/2019 Add support for deserializing the chunk in the protocol library

            session.sendPacketImmediately(startGamePacket);

            Vector3f pos = new Vector3f(-23, 70, 0);
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
            CachedPlayer player = session.getEntityCache().newPlayer(1, profile);

            session.setCachedEntity(player);
            session.getEntityCache().getEntities().put((long) 1, player);
            //session.spawn(1);
            //session.getCachedEntity().setPosition(new Vector3f(-23, 70, 0));
        }
    }

}
