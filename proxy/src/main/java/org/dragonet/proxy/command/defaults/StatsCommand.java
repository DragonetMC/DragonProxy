package org.dragonet.proxy.command.defaults;

import com.github.steveice10.mc.protocol.data.game.statistic.*;
import org.dragonet.proxy.command.ProxyCommand;
import org.dragonet.proxy.form.CustomForm;
import org.dragonet.proxy.form.components.LabelComponent;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.data.stats.StatInfo;
import org.dragonet.proxy.data.stats.Statistics;
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
