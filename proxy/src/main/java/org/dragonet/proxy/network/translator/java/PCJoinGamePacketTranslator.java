/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector3i;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.data.GamePublishSetting;
import com.nukkitx.protocol.bedrock.data.GameRule;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.v332.BedrockUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

import java.io.InputStream;
import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCJoinGamePacketTranslator implements PacketTranslator<ServerJoinGamePacket> {
    public static final PCJoinGamePacketTranslator INSTANCE = new PCJoinGamePacketTranslator();

    @Override
    public void translate(ProxySession session, ServerJoinGamePacket packet) {
        log.info("JoinGamePacketTranslator");
        StartGamePacket startGamePacket = new StartGamePacket();
        startGamePacket.setUniqueEntityId(packet.getEntityId());
        startGamePacket.setRuntimeEntityId(packet.getEntityId());
        startGamePacket.setPlayerGamemode(packet.getGameMode().ordinal());
        startGamePacket.setPlayerPosition(new Vector3f(-249, 67, -275));
        startGamePacket.setRotation(new Vector2f(1, 1));

        startGamePacket.setSeed(1111);
        startGamePacket.setDimensionId(0);
        startGamePacket.setGeneratorId(0);
        startGamePacket.setLevelGamemode(packet.getGameMode().ordinal());
        startGamePacket.setDifficulty(packet.getDifficulty().ordinal());
        startGamePacket.setDefaultSpawn(new Vector3i(-249, 67, -275));
        startGamePacket.setAcheivementsDisabled(true);
        startGamePacket.setTime(1300);
        startGamePacket.setEduLevel(false);
        startGamePacket.setEduFeaturesEnabled(false);
        startGamePacket.setRainLevel(0);
        startGamePacket.setLightningLevel(0);
        startGamePacket.setMultiplayerGame(false);
        startGamePacket.setBroadcastingToLan(true);
        startGamePacket.getGamerules().add((new GameRule("showcoordinates", true)));
        startGamePacket.setPlatformBroadcastMode(GamePublishSetting.FRIENDS_OF_FRIENDS);
        startGamePacket.setXblBroadcastMode(GamePublishSetting.FRIENDS_OF_FRIENDS);
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

        startGamePacket.setLevelId("oerjhii");
        startGamePacket.setWorldName("world");
        startGamePacket.setPremiumWorldTemplateId("00000000-0000-0000-0000-000000000000");
        startGamePacket.setCurrentTick(1);
        startGamePacket.setEnchantmentSeed(1);
        startGamePacket.setMultiplayerCorrelationId("");

        startGamePacket.setCachedPalette(DragonProxy.INSTANCE.getPaletteManager().getCachedPalette());

        // TODO: 01/04/2019 Add support for deserializing the chunk in the protocol library

        session.getUpstream().sendPacketImmediately(startGamePacket);

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
        session.getUpstream().sendPacketImmediately(playStatus);
    }

}
