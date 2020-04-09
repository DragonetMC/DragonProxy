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
package org.dragonet.proxy.metrics;

import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.util.TextFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.UUID;

@Log4j2
public class MetricsManager {
    private DragonProxy proxy;

    public MetricsManager(DragonProxy proxy) {
        this.proxy = proxy;

        // Load configuration
        try {
            if(!new File(proxy.getDataFolder(), "metrics.properties").exists()) {
                Files.copy(getClass().getResourceAsStream("/metrics.properties"), new File(proxy.getDataFolder(), "metrics.properties").toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            FileInputStream input = new FileInputStream("metrics.properties");
            Properties config = new Properties();
            config.load(input);

            if(config.getProperty("server-uuid").equalsIgnoreCase("placeholder")) {
                config.setProperty("server-uuid", UUID.randomUUID().toString());
                config.store(new FileOutputStream("metrics.properties"), "DO NOT EDIT server-uuid OR IT WILL BREAK METRICS");
            }

            UUID serverId = UUID.fromString(config.getProperty("server-uuid"));
            if(config.getProperty("enable").equalsIgnoreCase("true")) {
                log.info(TextFormat.DARK_AQUA + "Metrics enabled: " + TextFormat.GRAY + serverId.toString());
                initMetrics(serverId);
            }

            input.close();
        } catch (IOException ex) {
            log.error("Failed to copy metrics config file: " + ex.getMessage());
        }
    }

    private void initMetrics(UUID serverId) {
        Metrics metrics = new Metrics("DragonProxy", serverId.toString(), 2094);
        metrics.addCustomChart(new Metrics.SingleLineChart("servers", () -> 1));
        metrics.addCustomChart(new Metrics.SimplePie("bedrock_versions", DragonProxy.BEDROCK_CODEC::getMinecraftVersion));
        //metrics.addCustomChart(new Metrics.SingleLineChart("players", () -> 1));

        metrics.addCustomChart(new Metrics.SimplePie("auth_type", () -> {
            String authType = proxy.getConfiguration().getRemoteAuthType().name().toLowerCase();
            return Character.toUpperCase(authType.charAt(0)) + authType.substring(1);
        }));

        metrics.addCustomChart(new Metrics.SimplePie("proxy_version", () -> proxy.getVersion()));
    }
}
