package org.dragonet.dragonproxy.proxy.configuration;

import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import ch.jalu.configme.resource.YamlFileResource;
import org.dragonet.dragonproxy.proxy.ProxyFolder;
import org.dragonet.dragonproxy.proxy.util.FileUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.nio.file.Path;

public class ConfigurationProvider implements Provider<DragonConfiguration> {

    @Inject
    @ProxyFolder
    private Path dataFolder;
    @Inject
    private ConfigurationMigrationService migrationService;

    ConfigurationProvider() {
    }

    @Override
    public DragonConfiguration get() {
        Path configFile = dataFolder.resolve("configuration.yml");
        FileUtils.createFileIfNotExists(configFile);
        return new DragonConfiguration(new YamlFileResource(configFile.toFile()),
            ConfigurationDataBuilder.createConfiguration(ConfigurationProperty.class), migrationService);
    }
}
