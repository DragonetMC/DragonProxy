package org.dragonet.proxy.network;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import lombok.Getter;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.ProxySession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class SessionManager {
    private final Set<ProxySession> sessions = new HashSet<>();

    public ProxySession newSession(DragonProxy proxy, BedrockServerSession session) {
        ProxySession proxySession = new ProxySession(proxy, session);
        sessions.add(proxySession);

        return proxySession;
    }

    public ProxySession getSession(String name) {
        return null;
    }

    public int getPlayerCount() {
        return sessions.size();
    }
}
