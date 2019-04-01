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
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.ProxySession;

public class ProxySessionManager extends SessionManager<BedrockSession<ProxySession>> {

    @Override
    protected void onAddSession(BedrockSession<ProxySession> session) {
        //System.out.println("onAddSession called");
    }

    public void onTick() {
        for (BedrockSession session : sessions.values()) {
            executor.execute(session::onTick);
        }
    }
}
