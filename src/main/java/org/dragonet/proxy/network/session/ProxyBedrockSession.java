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
package org.dragonet.proxy.network.session;

import com.nukkitx.network.SessionManager;
import com.nukkitx.network.raknet.session.RakNetSession;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.util.RemoteServer;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;

public class ProxyBedrockSession extends ProxySession {
    public static final SessionManager<BedrockSession<ProxyBedrockSession>> MANAGER = new SessionManager<>();

    private ProxyBedrockSession(InetSocketAddress remote, InetSocketAddress local, RakNetSession s) {
        if(!(MANAGER.get(remote) == null)) {
            throw new RuntimeException();
        }
        MANAGER.add(remote, new BedrockSession<>(s));
    }

    @Override
    public void onDisconnect(@Nonnull DisconnectReason disconnectReason) {

    }

    @Override
    public void onDisconnect(@Nonnull String s) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }


    @Override
    public void setRemoteServer(RemoteServer s) {

    }

    @Override
    public RemoteServer getRemoteServer() {
        return null;
    }

    public static ProxyBedrockSession add(InetSocketAddress remote, InetSocketAddress local, RakNetSession session) {
        return new ProxyBedrockSession(remote, local, session);
    }

    public static ProxyBedrockSession get(InetSocketAddress s) {
        return MANAGER.get(s).getPlayer();
    }

}
