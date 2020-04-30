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
package org.dragonet.proxy.network;

import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.nukkitx.protocol.bedrock.BedrockPong;
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.DragonConfiguration;
import sun.print.resources.serviceui;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;

/**
 * Handles the bedrock client raknet ping.
 */
@Log4j2
@RequiredArgsConstructor
public class ProxyServerEventListener implements BedrockServerEventHandler {
    private static final BedrockPong pong = new BedrockPong();

    private final DragonProxy proxy;

    static {
        pong.setEdition("MCPE");
        pong.setGameType("Default");
        pong.setNintendoLimited(false);
        pong.setProtocolVersion(DragonProxy.BEDROCK_CODEC.getProtocolVersion());
        pong.setVersion(null); //Do we really want this added to the MOTD?
        pong.setIpv4Port(DragonProxy.INSTANCE.getConfiguration().getBindPort());
    }

    @Override
    public boolean onConnectionRequest(@Nonnull InetSocketAddress address) {
        return true;
    }

    @Override
    public BedrockPong onQuery(@Nonnull InetSocketAddress address) {
        DragonConfiguration config = proxy.getConfiguration();

        ServerStatusInfo serverInfo = null;
        if (config.isPingPassthrough()) {
            serverInfo = proxy.getPingPassthroughThread().getStatusInfo();
        }

        if (serverInfo != null) {
            pong.setMaximumPlayerCount(serverInfo.getPlayerInfo().getMaxPlayers() + 1);
            pong.setPlayerCount(serverInfo.getPlayerInfo().getOnlinePlayers());
        } else {
            pong.setPlayerCount(proxy.getSessionManager().getPlayerCount());
            pong.setMaximumPlayerCount(config.getMaxPlayers());
        }

        // Java MOTD never look good on Bedrock. This should never passthrough
        pong.setMotd(config.getMotd());
        pong.setSubMotd(config.getMotd2());

        return pong;
    }

    @Override
    public void onSessionCreation(BedrockServerSession session) {
        session.setLogging(true);
        session.setPacketHandler(new UpstreamPacketHandler(proxy, proxy.getSessionManager().newSession(proxy, session)));
    }
}
