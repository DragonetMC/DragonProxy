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
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.util.RemoteServer;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.net.InetSocketAddress;

public class UpstreamSession extends ProxySession {
    public static final SessionManager<BedrockSession<UpstreamSession>> MANAGER = new SessionManager<>();

    private DownstreamSession downstream;
    private RakNetSession raknetSession;

    public UpstreamSession(RakNetSession session) {
        raknetSession = session;
        //if(!(MANAGER.get(remote) == null)) {
        //    throw new RuntimeException();
        //}
        MANAGER.add(session.getLocalAddress(), new BedrockSession<>(session));
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
    public void setRemoteServer(RemoteServer server) {
        System.out.println("Begin connecting to remote server");
        if(downstream != null && !downstream.isClosed()) {
            System.out.println("Downstream close");
            downstream.close();
        }
        downstream = new DownstreamSession(new BedrockSession<UpstreamSession>(raknetSession), DragonProxy.INSTANCE);
        System.out.println("After construct downstream");
        downstream.setRemoteServer(server);
    }

    @Override
    public RemoteServer getRemoteServer() {
        return null;
    }

    public void postLogin() {
        System.out.println("Client connected");
    }

    public static UpstreamSession add(InetSocketAddress remote, InetSocketAddress local, RakNetSession session) {
        return new UpstreamSession(session);
    }

    public static UpstreamSession get(InetSocketAddress s) {
        return MANAGER.get(s).getPlayer();
    }

}
