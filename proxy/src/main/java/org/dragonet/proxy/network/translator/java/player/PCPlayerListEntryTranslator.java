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
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.nukkitx.protocol.bedrock.data.ImageData;
import com.nukkitx.protocol.bedrock.data.SerializedSkin;
import com.nukkitx.protocol.bedrock.packet.PlayerListPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.PlayerListInfo;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.PlayerListCache;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.util.SkinUtils;
import org.dragonet.proxy.util.TextFormat;
import sun.plugin2.message.Message;

import java.nio.charset.StandardCharsets;

@Log4j2
@PCPacketTranslator(packetClass = ServerPlayerListEntryPacket.class)
public class PCPlayerListEntryTranslator extends PacketTranslator<ServerPlayerListEntryPacket> {

    @Override
    public void translate(ProxySession session, ServerPlayerListEntryPacket packet) {
        PlayerListCache playerListCache = session.getPlayerListCache();
        PlayerListPacket playerListPacket = new PlayerListPacket();

        for(PlayerListEntry entry : packet.getEntries()) {
            PlayerListPacket.Entry bedrockEntry = new PlayerListPacket.Entry(entry.getProfile().getId());
            String displayName = entry.getDisplayName() != null ? MessageTranslator.translate(entry.getDisplayName()) : entry.getProfile().getName();

            playerListCache.getPlayerInfo().put(entry.getProfile().getId(), new PlayerListInfo(entry, displayName));

            switch(packet.getAction()) {
                case ADD_PLAYER:
                    if(entry.getProfile().getId().equals(session.getCachedEntity().getJavaUuid())) {
                        return;
                    }

                    long proxyEid = session.getEntityCache().getNextClientEntityId().getAndIncrement();
                    playerListCache.getPlayerEntityIds().put(entry.getProfile().getId(), proxyEid);

                    playerListPacket.setAction(PlayerListPacket.Action.ADD);

                    bedrockEntry.setEntityId(proxyEid);
                    bedrockEntry.setName(displayName);
                    bedrockEntry.setSkin(SkinUtils.createSkinEntry(SkinUtils.STEVE_SKIN, GameProfile.TextureModel.NORMAL, ImageData.EMPTY));
                    bedrockEntry.setXuid("");
                    bedrockEntry.setPlatformChatId("");

                    playerListPacket.getEntries().add(bedrockEntry);

                    session.sendPacket(playerListPacket);
                    break;

                case UPDATE_DISPLAY_NAME:
                    // TODO: sync the player list
                    playerListCache.updateDisplayName(entry.getProfile(), displayName);
                    break;

                case UPDATE_LATENCY:

                    break;

                case REMOVE_PLAYER:
                    playerListPacket.setAction(PlayerListPacket.Action.REMOVE);
                    playerListPacket.getEntries().add(bedrockEntry);

                    session.sendPacket(playerListPacket);

                    playerListCache.getPlayerInfo().remove(entry.getProfile().getId());
                    playerListCache.getPlayerEntityIds().removeLong(entry.getProfile().getId());
                    break;
            }
        }
    }
}
