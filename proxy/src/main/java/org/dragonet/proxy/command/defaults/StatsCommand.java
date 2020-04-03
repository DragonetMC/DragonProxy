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
package org.dragonet.proxy.command.defaults;

import com.github.steveice10.mc.protocol.data.game.statistic.GenericStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.Statistic;
import org.dragonet.proxy.command.ProxyCommand;
import org.dragonet.proxy.data.stats.StatInfo;
import org.dragonet.proxy.data.stats.Statistics;
import org.dragonet.proxy.form.CustomForm;
import org.dragonet.proxy.form.components.LabelComponent;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.util.TextFormat;

import java.util.Map;

public class StatsCommand extends ProxyCommand {

    public StatsCommand() {
        super("stats", "View your statistics on the remote server", "/stats");
    }

    @Override
    public void execute(ProxySession session, String[] arguments) {
        WorldCache worldCache = session.getWorldCache();

        session.sendMessage("Fetching statistics...");

        session.fetchStatistics().whenComplete((statistics, throwable) -> {
            // TODO: add support for viewing other types of statistics
            CustomForm form = new CustomForm("Statistics");

            Map<StatInfo, Integer> cachedStats = worldCache.getStatistics();

            for(Statistic statistic : statistics.keySet()) {
                int value = statistics.get(statistic);

                if(statistic instanceof GenericStatistic) {
                    StatInfo info = Statistics.getStatisticInfo(((GenericStatistic) statistic));

                    if(info != null) {
                        if(!cachedStats.containsKey(info)) {
                            cachedStats.put(info, value);
                        }
                        else if(!cachedStats.get(info).equals(value)) {
                            cachedStats.put(info, value);
                        } else {
                            session.sendMessage(info.getName() + " is cached"); // TODO: why is this never called?
                        }
                    }
                }
            }

            if(cachedStats.isEmpty()) {
                form.addComponent(new LabelComponent("No statistics available."));
            } else {
                for(Map.Entry<StatInfo, Integer> stats : cachedStats.entrySet()) {
                    form.addComponent(new LabelComponent(stats.getKey().getName() + ": " + TextFormat.GOLD + stats.getKey().format(cachedStats.get(stats.getKey()))));
                }
            }

            form.send(session);
        });
    }
}
