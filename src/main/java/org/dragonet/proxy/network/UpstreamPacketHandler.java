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
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.ProxyBedrockSession;

public class UpstreamPacketHandler implements BedrockPacketHandler {

    private BedrockSession<ProxyBedrockSession> session;
    private DragonProxy proxy;

    public UpstreamPacketHandler(BedrockSession<ProxyBedrockSession> session, DragonProxy proxy) {
        this.session = session;
        this.proxy = proxy;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        System.out.println("GOT LoginPacket");
        return true;
    }
}
