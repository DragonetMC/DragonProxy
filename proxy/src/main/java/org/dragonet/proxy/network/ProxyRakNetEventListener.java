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

import com.nukkitx.network.SessionManager;
import com.nukkitx.network.raknet.RakNetServerEventListener;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.DragonConfiguration;
import org.dragonet.proxy.network.session.ProxySession;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class ProxyRakNetEventListener implements RakNetServerEventListener {
    private final SessionManager<BedrockSession<ProxySession>> sessionManager;

    @Nonnull
    @Override
    public Action onConnectionRequest(InetSocketAddress address, int protocolVersion) {
        return Action.CONTINUE;
    }

    @Nonnull
    public Advertisement onQuery(InetSocketAddress address) {
        DragonConfiguration config = DragonProxy.INSTANCE.getConfiguration();
        return new Advertisement(
            "MCPE",
            config.getMotd(),
            DragonProxy.BEDROCK_CODEC.getProtocolVersion(),
            "",
            sessionManager.getCount(),
            config.getMaxPlayers(),
            config.getMotd2(),
            "SMP"
        );
    }
}
