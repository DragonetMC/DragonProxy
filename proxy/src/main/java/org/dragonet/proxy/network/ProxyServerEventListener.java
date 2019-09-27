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
package org.dragonet.proxy.network;

import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.nukkitx.protocol.bedrock.BedrockPong;
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.DragonConfiguration;
import org.dragonet.proxy.network.session.ProxySession;

import java.net.InetSocketAddress;

@AllArgsConstructor
public class ProxyServerEventListener implements BedrockServerEventHandler {

    public DragonProxy proxy;

    @Override
    public boolean onConnectionRequest(InetSocketAddress address) {
        return true;
    }

    @Override
    public BedrockPong onQuery(InetSocketAddress address) {
        DragonConfiguration config = proxy.getConfiguration();

        BedrockPong pong = new BedrockPong();
        pong.setEdition("MCPE");
        pong.setGameType("Default");
        pong.setNintendoLimited(false);
        pong.setProtocolVersion(DragonProxy.BEDROCK_CODEC.getProtocolVersion());
        pong.setIpv4Port(config.getBindPort());

        if (config.isPingPassthrough()) {
            ServerStatusInfo serverInfo = proxy.getPingPassthroughThread().getInfo();

            if (serverInfo != null) {
                pong.setMotd(serverInfo.getDescription().getText());
                pong.setSubMotd(config.getMotd2());
                pong.setPlayerCount(serverInfo.getPlayerInfo().getOnlinePlayers());
                pong.setMaximumPlayerCount(serverInfo.getPlayerInfo().getMaxPlayers());
            }
        } else {
            pong.setPlayerCount(0);
            pong.setMaximumPlayerCount(config.getMaxPlayers());
            pong.setMotd(config.getMotd());
            pong.setSubMotd(config.getMotd2());
        }

        return pong;
    }

    @Override
    public void onSessionCreation(BedrockServerSession session) {
        session.setLogging(true);
        session.setPacketHandler(new UpstreamPacketHandler(proxy, session));
    }
}
