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

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.nukkitx.protocol.bedrock.packet.PlayerListPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.util.SkinUtils;

@Log4j2
public class PCPlayerListEntryTranslator implements PacketTranslator<ServerPlayerListEntryPacket> {
    public static final PCPlayerListEntryTranslator INSTANCE = new PCPlayerListEntryTranslator();

    @Override
    public void translate(ProxySession session, ServerPlayerListEntryPacket packet) {
        PlayerListPacket playerListPacket = new PlayerListPacket();

        for(PlayerListEntry entry : packet.getEntries()) {
            PlayerListPacket.Entry bedrockEntry = new PlayerListPacket.Entry(entry.getProfile().getId());
            session.getPlayerListCache().getPlayerInfo().put(entry.getProfile().getId(), entry);

            switch(packet.getAction()) {
                case ADD_PLAYER:
                    // Fetch our own skin
                    if(entry.getProfile().getName().equals(session.getUsername()) && session.getProxy().getConfiguration().isFetchPlayerSkins()) {
                        session.getProxy().getGeneralThreadPool().execute(() -> {
                            byte[] skinData = SkinUtils.fetchSkin(entry.getProfile());
                            if (skinData == null) {
                                return;
                            }
                            session.setPlayerSkin2(session.getAuthData().getIdentity(), skinData);
                        });
                        return;
                    }

                    long proxyEid = session.getEntityCache().getNextClientEntityId().getAndIncrement();

                    playerListPacket.setType(PlayerListPacket.Type.ADD);

                    bedrockEntry.setEntityId(proxyEid);
                    bedrockEntry.setName(entry.getProfile().getName());
                    bedrockEntry.setSkinId(entry.getProfile().getIdAsString());
                    bedrockEntry.setSkinData(SkinUtils.STEVE_SKIN);
                    bedrockEntry.setCapeData(new byte[0]);
                    bedrockEntry.setGeometryName("geometry.humanoid");
                    bedrockEntry.setGeometryData("");
                    bedrockEntry.setXuid("");
                    bedrockEntry.setPlatformChatId("");

                    playerListPacket.getEntries().add(bedrockEntry);

                    session.sendPacket(playerListPacket);
                    break;

                case UPDATE_LATENCY:

                    break;

                case REMOVE_PLAYER:
                    playerListPacket.setType(PlayerListPacket.Type.REMOVE);
                    playerListPacket.getEntries().add(bedrockEntry);

                    session.sendPacket(playerListPacket);

                    session.getPlayerListCache().getPlayerInfo().remove(entry.getProfile().getId());
                    break;
            }
        }
    }
}
