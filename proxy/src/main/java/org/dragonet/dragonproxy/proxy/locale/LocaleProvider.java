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
package org.dragonet.dragonproxy.proxy.locale;

import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import ch.jalu.configme.resource.YamlFileResource;
import org.dragonet.dragonproxy.proxy.ProxyFolder;
import org.dragonet.dragonproxy.proxy.configuration.ConfigurationProperty;
import org.dragonet.dragonproxy.proxy.configuration.DragonConfiguration;
import org.dragonet.dragonproxy.proxy.util.FileUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocaleProvider implements Provider<DragonLocale> {

    @Inject
    @ProxyFolder
    private Path dataFolder;
    @Inject
    private DragonConfiguration configuration;
    @Inject
    private LocaleMigrationService migrationService;

    LocaleProvider() {
    }

    @Override
    public DragonLocale get() {
        Path localeFolder = dataFolder.resolve("locales");
        FileUtils.createDirectoriesIfNotExist(localeFolder);
        Path localeFile = localeFolder.resolve("locale_" + configuration.getProperty(ConfigurationProperty.LOCALE) + ".yml");
        FileUtils.createFileIfNotExists(localeFile);
        return new DragonLocale(new YamlFileResource(localeFile.toFile()),
            ConfigurationDataBuilder.createConfiguration(LocaleProperty.class), migrationService);
    }
}
