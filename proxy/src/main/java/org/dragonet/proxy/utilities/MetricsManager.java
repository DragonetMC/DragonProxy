package org.dragonet.proxy.utilities;

import com.github.steveice10.mc.protocol.MinecraftConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bstats.Metrics;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.PropertiesConfig;

/**
 * @author Epic
 */
public class MetricsManager {
    private PropertiesConfig config;

    public MetricsManager(DragonProxy proxy) {
        // Get the config file
        try {
            config = new PropertiesConfig("/metrics.yml", "metrics.yml", false);
        } catch (IOException ex) {
            proxy.getLogger().severe("Failed to load configuration file! Make sure the file is writable.");
            ex.printStackTrace();
        }

        // Check if the config file exists
        if (!config.getConfig().contains("serverUuid") || config.getConfig().getProperty("serverUuid").equals("")) {

            // Add default values
            config.getConfig().setProperty("enabled", "true");
            // Every server gets it's unique random id.
            config.getConfig().setProperty("serverUuid", UUID.randomUUID().toString());
            // Should failed request be logged?
            config.getConfig().setProperty("logFailedRequests", "false");

            try {
                config.save();
            } catch (IOException ignored) {}
        }

        // Load the data
        String serverUUID = config.getConfig().getProperty("serverUuid");
        boolean logFailedRequests = config.getConfig().getProperty("logFailedRequests", "false").equals("true");
        // Only start Metrics, if it's enabled in the config
        if (config.getConfig().getProperty("enabled", "true").equals("true")) {
            Metrics metrics = new Metrics("DragonProxy", serverUUID, logFailedRequests, java.util.logging.Logger.getLogger("bStats"));

            metrics.addCustomChart(new Metrics.SimplePie("minecraft_version", () -> MinecraftConstants.GAME_VERSION));

            metrics.addCustomChart(new Metrics.SingleLineChart("players", () -> proxy.getSessionRegister().getAll().size()));
            metrics.addCustomChart(new Metrics.SimplePie("online_mode", () -> proxy.getAuthMode().equals("online") ? "online" : "offline"));

            metrics.addCustomChart(new Metrics.SimplePie("proxy_version", () -> proxy.getVersion()));

            metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                String javaVersion = System.getProperty("java.version");
                Map<String, Integer> entry = new HashMap<>();
                entry.put(javaVersion, 1);
                if (javaVersion.startsWith("1.7"))
                    map.put("Java 1.7", entry);
                else if (javaVersion.startsWith("1.8"))
                    map.put("Java 1.8", entry);
                else if (javaVersion.startsWith("1.9"))
                    map.put("Java 1.9", entry);
                else
                    map.put("Other", entry);
                return map;
            }));

        }
    }
}
