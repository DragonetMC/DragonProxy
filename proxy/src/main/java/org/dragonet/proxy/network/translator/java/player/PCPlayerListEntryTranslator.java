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
package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.property.PropertyException;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.nukkitx.protocol.bedrock.packet.PlayerListPacket;
import com.nukkitx.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.data.ClientData;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.util.SkinUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class PCPlayerListEntryTranslator implements PacketTranslator<ServerPlayerListEntryPacket> {
    public static final PCPlayerListEntryTranslator INSTANCE = new PCPlayerListEntryTranslator();

    @Override
    public void translate(ProxySession session, ServerPlayerListEntryPacket packet) {
        PlayerListPacket playerListPacket = new PlayerListPacket();

        switch(packet.getAction()) {
            case ADD_PLAYER:
                playerListPacket.setType(PlayerListPacket.Type.ADD);
                break;
            case REMOVE_PLAYER:
                playerListPacket.setType(PlayerListPacket.Type.REMOVE);
                break;
            default:
                //log.warn("Unknown type: " + packet.getAction().name());
                return;
        }

        for(PlayerListEntry entry : packet.getEntries()) {
            PlayerListPacket.Entry bedrockEntry = new PlayerListPacket.Entry(entry.getProfile().getId());
            session.getPlayerInfoCache().put(entry.getProfile().getId(), entry);

            if (packet.getAction() == PlayerListEntryAction.ADD_PLAYER) {
                ClientData clientData = session.getClientData();
                long proxyEid = session.getEntityCache().getNextClientEntityId().getAndIncrement();

                SetLocalPlayerAsInitializedPacket initializePacket = new SetLocalPlayerAsInitializedPacket();
                initializePacket.setRuntimeEntityId(proxyEid);
                //session.getBedrockSession().sendPacket(initializePacket);

                // TODO: reduce duplicated code?
                if(session.getProxy().getConfiguration().isFetchPlayerSkins()) {
                    // Fetch skin data from Mojang
                    session.getProxy().getGeneralThreadPool().execute(() -> {
                        byte[] skinData = SkinUtils.fetchSkin(entry.getProfile());
                        if (skinData == null) {
                            skinData = clientData.getSkinData();
                        }

                        // Doesnt work currently
                        byte[] capeData = clientData.getCapeData(); //SkinUtils.fetchOptifineCape(entry.getProfile());
                        //if(capeData == null) {
                        //    capeData = clientData.getCapeData();
                        //}

                        bedrockEntry.setEntityId(proxyEid);
                        bedrockEntry.setName(entry.getProfile().getName());
                        bedrockEntry.setSkinId(clientData.getSkinId());
                        bedrockEntry.setSkinData(skinData);
                        bedrockEntry.setCapeData(capeData);
                        bedrockEntry.setGeometryName(clientData.getSkinGeometryName());
                        bedrockEntry.setGeometryData(new String(clientData.getSkinGeometry(), StandardCharsets.UTF_8));
                        bedrockEntry.setXuid("");
                        bedrockEntry.setPlatformChatId("");

                        playerListPacket.getEntries().add(bedrockEntry);

                        session.getBedrockSession().sendPacket(playerListPacket);
                    });
                } else {
                    bedrockEntry.setEntityId(proxyEid);
                    bedrockEntry.setName(entry.getProfile().getName());
                    bedrockEntry.setSkinId(clientData.getSkinId());
                    bedrockEntry.setSkinData(clientData.getSkinData());
                    bedrockEntry.setCapeData(clientData.getCapeData());
                    bedrockEntry.setGeometryName(clientData.getSkinGeometryName());
                    bedrockEntry.setGeometryData(new String(clientData.getSkinGeometry(), StandardCharsets.UTF_8));
                    bedrockEntry.setXuid("");
                    bedrockEntry.setPlatformChatId("");

                    playerListPacket.getEntries().add(bedrockEntry);

                    session.getBedrockSession().sendPacket(playerListPacket);
                }
            }
        }
    }
}
