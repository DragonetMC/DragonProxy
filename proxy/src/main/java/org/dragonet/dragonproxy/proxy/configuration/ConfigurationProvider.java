package org.dragonet.dragonproxy.proxy.configuration;

import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import ch.jalu.configme.resource.YamlFileResource;
import org.dragonet.dragonproxy.proxy.ProxyFolder;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

public class ConfigurationProvider implements Provider<DragonConfiguration> {

    @Inject
    @ProxyFolder
    private File dataFolder;
    @Inject
    private ConfigurationMigrationService migrationService;

    ConfigurationProvider() {
    }

    @Override
    public DragonConfiguration get() {
        File configFile = new File(dataFolder, "config.yml");
        ch.jalu.configme.utils.Utils.createFileIfNotExists(configFile);
        return new DragonConfiguration(new YamlFileResource(configFile),
            ConfigurationDataBuilder.createConfiguration(ConfigurationProperty.class), migrationService);
    }
}
