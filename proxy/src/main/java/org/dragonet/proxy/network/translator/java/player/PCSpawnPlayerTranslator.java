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
package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.nukkitx.math.vector.Vector3f;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.EntityMetaTranslator;
import org.dragonet.proxy.util.SkinUtils;

@Log4j2
public class PCSpawnPlayerTranslator implements PacketTranslator<ServerSpawnPlayerPacket> {
    public static final PCSpawnPlayerTranslator INSTANCE = new PCSpawnPlayerTranslator();

    @Override
    public void translate(ProxySession session, ServerSpawnPlayerPacket packet) {
        PlayerListEntry playerListEntry = session.getPlayerListCache().getPlayerInfo().get(packet.getUuid());

        CachedPlayer cachedPlayer = session.getEntityCache().newPlayer(packet.getEntityId(), playerListEntry.getProfile());
        cachedPlayer.setPosition(Vector3f.from(packet.getX(), packet.getY(), packet.getZ()));
        cachedPlayer.setRotation(Vector3f.from(packet.getYaw(), packet.getPitch(), 0));
        cachedPlayer.getMetadata().putAll(EntityMetaTranslator.translateToBedrock(cachedPlayer, packet.getMetadata()));
        cachedPlayer.spawn(session);

        if(session.getProxy().getConfiguration().isFetchPlayerSkins()) {
            session.getProxy().getGeneralThreadPool().execute(() -> {
                GameProfile profile = session.getPlayerListCache().getPlayerInfo().get(packet.getUuid()).getProfile();

                byte[] skinData = SkinUtils.fetchSkin(profile);
                if (skinData == null) {
                    return;
                }

//                byte[] capeData = SkinUtils.fetchOptifineCape(profile);
//                if(capeData == null) {
//                    capeData = clientData.getCapeData();
//                }

                session.setPlayerSkin(profile.getId(), skinData);
            });
        }
    }
}
