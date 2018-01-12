/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy;

import co.aikar.timings.Timings;

public class TickerThread extends Thread {

    private final DragonProxy proxy;

    public TickerThread(DragonProxy proxy) {
        this.proxy = proxy;
        setDaemon(true);
    }

    @Override
    public void run() {
        long time;
        while (!proxy.isShuttingDown()) {
            time = System.currentTimeMillis();
            Timings.fullServerTickTimer.startTiming();
            proxy.onTick();
            Timings.fullServerTickTimer.stopTiming();
            time = System.currentTimeMillis() - time;
            if (time >= 50)
                continue;
            else
                try {
                    Thread.sleep(50 - time);
                } catch (InterruptedException ex) {
                    return;
                }
        }
    }
}
