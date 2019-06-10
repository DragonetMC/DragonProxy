/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network;

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
        DragonConfiguration config = DragonProxy.INSTANCE.getConfiguration();

        BedrockPong pong = new BedrockPong();
        pong.setEdition("MCPE");
        pong.setMotd(config.getMotd());
        pong.setPlayerCount(0);
        pong.setMaximumPlayerCount(config.getMaxPlayers());
        pong.setSubMotd(config.getMotd2());
        pong.setGameType("Default");
        pong.setNintendoLimited(false);
        pong.setProtocolVersion(DragonProxy.BEDROCK_CODEC.getProtocolVersion());

        return pong;
    }

    @Override
    public void onSessionCreation(BedrockServerSession session) {
        session.setLogging(true);
        session.setPacketHandler(new UpstreamPacketHandler(proxy, session));
    }
}
