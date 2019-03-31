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

import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.UpstreamSession;
import org.dragonet.proxy.util.RemoteServer;

/**
 * Respresents the connection between the mcpe client and the proxy.
 */
public class UpstreamPacketHandler implements BedrockPacketHandler {

    private BedrockSession<UpstreamSession> session;
    private DragonProxy proxy;

    public UpstreamPacketHandler(BedrockSession<UpstreamSession> session, DragonProxy proxy) {
        this.session = session;
        this.proxy = proxy;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        // TODO: move out of here? idk
        UpstreamSession session = new UpstreamSession(this.session.getConnection());
        session.setRemoteServer(new RemoteServer("local", proxy.getConfiguration().getRemoteAddress(), proxy.getConfiguration().getRemotePort()));
        return true;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        session.getPlayer().postLogin();
        return true;
    }
}
