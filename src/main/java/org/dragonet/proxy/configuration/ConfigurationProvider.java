/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.configuration;

import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import ch.jalu.configme.resource.YamlFileResource;
import org.dragonet.proxy.ProxyFolder;
import org.dragonet.proxy.util.FileUtils;

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
