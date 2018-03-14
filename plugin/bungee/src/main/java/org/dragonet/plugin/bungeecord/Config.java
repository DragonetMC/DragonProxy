////////////////////////////////////////////////////////////////////////////////
//
//  MILLENIUM-STUDIO
//  Copyright 2015 Millenium-studio SARL
//  All Rights Reserved.
//
////////////////////////////////////////////////////////////////////////////////
package org.dragonet.plugin.bungeecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/* Default configuration manager, used in every plugin */
public class Config {

    private static Plugin plugin;

    private Configuration configuration;

    public Config(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Load the configuration Copy it from default if not exist
     *
     * @param fileConfiguration
     */
    public final void load(String fileConfiguration) {
        try {
            File ConfigurationFile = new File(this.plugin.getDataFolder(), fileConfiguration);

            if (!ConfigurationFile.exists())
                this.copyDefault(fileConfiguration);

            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ConfigurationFile);

            this.plugin.getLogger().info("Config loaded!");
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return the configuration
     */
    public Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Copy configuration from resource
     *
     * @param fileConfiguration
     */
    public void copyDefault(String fileConfiguration) {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();
        File configurationFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configurationFile.exists())
            try {
                configurationFile.createNewFile();
                InputStream in = plugin.getResourceAsStream("example_" + fileConfiguration);
                try {
                    java.io.OutputStream out = new java.io.FileOutputStream(configurationFile);
                    try {
                        com.google.common.io.ByteStreams.copy(in, out);
                    } catch (Throwable throwable) {
                        throw throwable;
                    } finally {
                    }
                } catch (Throwable throwable) {
                    throw throwable;
                } finally {
                    if (in != null)
                        try {
                            in.close();
                        } catch (Throwable throwable) {
                            throw throwable;
                        }
                }
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, null, ex);
            }
    }

    /**
     * Save the configuration to file
     *
     * @param fileConfiguration
     */
    public void save(String fileConfiguration) {
        try {
            File ConfigurationFile = new File(this.plugin.getDataFolder(), fileConfiguration);

            if (!ConfigurationFile.exists())
                this.copyDefault(fileConfiguration);

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, ConfigurationFile);

            this.plugin.getLogger().info("Config saved!");
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
    }
}
