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
