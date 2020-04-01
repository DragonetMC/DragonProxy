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

import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

import java.util.concurrent.TimeUnit;


@Log4j2
@PCPacketTranslator(packetClass = ServerRespawnPacket.class)
public class PCRespawnTranslator extends PacketTranslator<ServerRespawnPacket> {
    public static final PCRespawnTranslator INSTANCE = new PCRespawnTranslator();

    @Override
    public void translate(ProxySession session, ServerRespawnPacket packet) {
        CachedPlayer cachedPlayer = session.getCachedEntity();

        // Set the players gamemode
        SetPlayerGameTypePacket setPlayerGameTypePacket = new SetPlayerGameTypePacket();
        setPlayerGameTypePacket.setGamemode(packet.getGamemode().ordinal());
        session.sendPacket(setPlayerGameTypePacket);

        // Respawn the player
        RespawnPacket respawnPacket = new RespawnPacket();
        respawnPacket.setRuntimeEntityId(cachedPlayer.getProxyEid());
        respawnPacket.setPosition(cachedPlayer.getSpawnPosition());
        respawnPacket.setState(RespawnPacket.State.SERVER_READY);
        session.sendPacket(respawnPacket);
    }
}
