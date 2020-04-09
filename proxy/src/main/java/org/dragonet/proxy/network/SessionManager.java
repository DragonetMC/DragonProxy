/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
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
