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

import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector3i;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.nukkitx.protocol.bedrock.packet.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCRespawnTranslator implements PacketTranslator<ServerRespawnPacket> {
    public static final PCRespawnTranslator INSTANCE = new PCRespawnTranslator();

    @Override
    public void translate(ProxySession session, ServerRespawnPacket packet) {
        if(packet.getDimension() != session.getCachedEntity().getDimension()) {
            // TODO: finish this
            ChangeDimensionPacket changeDimensionPacket = new ChangeDimensionPacket();
            changeDimensionPacket.setDimension(packet.getDimension());
            changeDimensionPacket.setPosition(session.getCachedEntity().getSpawnPosition());
            changeDimensionPacket.setRespawn(true);
            //session.sendPacket(changeDimensionPacket);

            PlayStatusPacket playStatusPacket = new PlayStatusPacket();
            playStatusPacket.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
            //session.sendPacket(playStatusPacket);

            session.getCachedEntity().setDimension(packet.getDimension());
        } else {
            RespawnPacket respawnPacket = new RespawnPacket();
            respawnPacket.setPosition(session.getCachedEntity().getSpawnPosition());
            session.sendPacket(respawnPacket);
        }

        SetPlayerGameTypePacket setPlayerGameTypePacket = new SetPlayerGameTypePacket();
        setPlayerGameTypePacket.setGamemode(packet.getGameMode().ordinal());
        session.sendPacket(setPlayerGameTypePacket);
    }
}
