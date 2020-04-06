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

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.nukkitx.protocol.bedrock.data.ImageData;
import com.nukkitx.protocol.bedrock.data.SerializedSkin;
import com.nukkitx.protocol.bedrock.packet.PlayerListPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.util.SkinUtils;

import java.nio.charset.StandardCharsets;

@Log4j2
@PCPacketTranslator(packetClass = ServerPlayerListEntryPacket.class)
public class PCPlayerListEntryTranslator extends PacketTranslator<ServerPlayerListEntryPacket> {

    @Override
    public void translate(ProxySession session, ServerPlayerListEntryPacket packet) {
        PlayerListPacket playerListPacket = new PlayerListPacket();

        for(PlayerListEntry entry : packet.getEntries()) {
            PlayerListPacket.Entry bedrockEntry = new PlayerListPacket.Entry(entry.getProfile().getId());
            session.getPlayerListCache().getPlayerInfo().put(entry.getProfile().getId(), entry);

            switch(packet.getAction()) {
                case ADD_PLAYER:
                    if(entry.getProfile().getName().equals(session.getUsername())) {
                        // Fetch our own skin
                        if(session.getProxy().getConfiguration().isFetchPlayerSkins()) {
                            session.getProxy().getGeneralThreadPool().execute(() -> {
                                byte[] skinData = SkinUtils.fetchSkin(entry.getProfile());
                                if (skinData == null) {
                                    return;
                                }
                                session.setPlayerSkin2(session.getAuthData().getIdentity(), skinData);
                            });
                            return;
                        }

                        // Set our own remote uuid
                        session.getCachedEntity().setJavaUuid(entry.getProfile().getId());
                    }

                    long proxyEid = session.getEntityCache().getNextClientEntityId().getAndIncrement();
                    session.getPlayerListCache().getPlayerEntityIds().put(entry.getProfile().getId(), proxyEid);

                    playerListPacket.setAction(PlayerListPacket.Action.ADD);

                    SerializedSkin skin = SerializedSkin.of(
                        entry.getProfile().getIdAsString(),
                        SkinUtils.STEVE_SKIN,
                        ImageData.EMPTY,
                        SkinUtils.getLegacyGeometryName("geometry.humanoid"),
                        new String(session.getClientData().getSkinGeometry(), StandardCharsets.UTF_8),
                        false);

                    bedrockEntry.setEntityId(proxyEid);
                    bedrockEntry.setName(entry.getProfile().getName());
                    bedrockEntry.setSkin(skin);
                    bedrockEntry.setXuid("");
                    bedrockEntry.setPlatformChatId("");

                    playerListPacket.getEntries().add(bedrockEntry);

                    session.sendPacket(playerListPacket);
                    break;

                case UPDATE_LATENCY:

                    break;

                case REMOVE_PLAYER:
                    playerListPacket.setAction(PlayerListPacket.Action.REMOVE);
                    playerListPacket.getEntries().add(bedrockEntry);

                    session.sendPacket(playerListPacket);

                    session.getPlayerListCache().getPlayerInfo().remove(entry.getProfile().getId());
                    session.getPlayerListCache().getPlayerEntityIds().removeLong(entry.getProfile().getId());
                    break;
            }
        }
    }
}
