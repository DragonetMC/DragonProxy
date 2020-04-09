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
package org.dragonet.proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;

/**
 * This code is from the old version of DragonProxy.
 * It needs cleaning up in the future.
 * https://github.com/DragonetMC/DragonProxy/blob/old/proxy/src/main/java/org/dragonet/proxy/TickerThread.java
 */
@RequiredArgsConstructor
@Log4j2
public class TickerThread extends Thread {
    private final DragonProxy proxy;

    @Override
    public void run() {
        long time;
        while (proxy.isRunning()) {
            time = System.currentTimeMillis();

            // Tick sessions
            proxy.getSessionManager().getSessions().forEach(ProxySession::onTick);

            time = System.currentTimeMillis() - time;
            if (time >= 50) {
                continue;
            } else {
                try {
                    Thread.sleep(50 - time);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
    }
}
