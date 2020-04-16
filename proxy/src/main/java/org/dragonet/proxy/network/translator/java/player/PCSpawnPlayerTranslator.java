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
package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.ImageData;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.util.SkinUtils;

@Log4j2
@PCPacketTranslator(packetClass = ServerSpawnPlayerPacket.class)
public class PCSpawnPlayerTranslator extends PacketTranslator<ServerSpawnPlayerPacket> {

    @Override
    public void translate(ProxySession session, ServerSpawnPlayerPacket packet) {
        PlayerListEntry playerListEntry = session.getPlayerListCache().getPlayerInfo().get(packet.getUuid());
        long cachedEntityId = session.getPlayerListCache().getPlayerEntityIds().getLong(packet.getUuid());

        CachedPlayer cachedPlayer = session.getEntityCache().newPlayer(packet.getEntityId(), cachedEntityId, playerListEntry.getProfile());
        cachedPlayer.setJavaUuid(packet.getUuid());
        cachedPlayer.setPosition(Vector3f.from(packet.getX(), packet.getY(), packet.getZ()));
        cachedPlayer.setRotation(Vector3f.from(packet.getYaw(), packet.getPitch(), 0));
        cachedPlayer.spawn(session);

        if(session.getProxy().getConfiguration().getPlayerConfig().isFetchSkin()) {
            session.getProxy().getGeneralThreadPool().execute(() -> {
                GameProfile profile = playerListEntry.getProfile();

                ImageData skinData = SkinUtils.fetchSkin(profile);
                if (skinData == null) return;

                ImageData capeData = SkinUtils.fetchUnofficialCape(profile);
                if(capeData == null) capeData = ImageData.EMPTY;

                session.setPlayerSkin(profile.getId(), cachedEntityId, skinData, capeData);
            });
        }
    }
}
