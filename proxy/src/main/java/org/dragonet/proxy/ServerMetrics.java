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

import java.io.File;
import java.io.IOException;

import org.dragonet.proxy.utilities.Versioning;
import org.mcstats.Metrics;

public class ServerMetrics extends Metrics {

    private final DragonProxy proxy;

    public ServerMetrics(DragonProxy proxy) throws IOException {
        super("DragonProxy", Versioning.RELEASE_VERSION);
        this.proxy = proxy;

        Metrics.Graph g = createGraph("Extra Data");
        g.addPlotter(new Plotter("OnlineMode") {
            @Override
            public int getValue() {
                if(proxy.getAuthMode().equals("cls")){
                    return 0;
                }else if(proxy.getAuthMode().equals("online")){
                    return 1;
                }else{
                    return 2;
                }
            }
        });
    }

    @Override
    public String getFullServerVersion() {
        return Versioning.RELEASE_VERSION;
    }

    @Override
    public int getPlayersOnline() {
        return proxy.getSessionRegister().getOnlineCount();
    }

    @Override
    public File getConfigFile() {
        return new File("statistic.properties");
    }

}
