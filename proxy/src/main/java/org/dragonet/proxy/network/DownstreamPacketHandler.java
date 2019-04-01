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

import com.github.steveice10.packetlib.Client;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.UpstreamSession;

import java.net.InetSocketAddress;

/**
 * Respresents the connection between the proxy and the remote server.
 */
public class DownstreamPacketHandler {

    private BedrockSession<UpstreamSession> upstream;
    private Client remoteClient;
    private DragonProxy proxy;

    public DownstreamPacketHandler(BedrockSession<UpstreamSession> upstream, DragonProxy proxy) {
        this.upstream = upstream;
        this.proxy = proxy;
    }

    public void connectToServer(InetSocketAddress address) {
        //MinecraftProtocol protocol = new MinecraftProtocol();

    }
}
