package org.dragonet.dragonproxy.proxy.locale;

import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import ch.jalu.configme.resource.YamlFileResource;
import org.dragonet.dragonproxy.proxy.ProxyFolder;
import org.dragonet.dragonproxy.proxy.configuration.ConfigurationProperty;
import org.dragonet.dragonproxy.proxy.configuration.DragonConfiguration;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

public class LocaleProvider implements Provider<DragonLocale> {

    @Inject
    @ProxyFolder
    private File dataFolder;
    @Inject
    private DragonConfiguration configuration;
    @Inject
    private LocaleMigrationService migrationService;

    LocaleProvider() {
    }

    @Override
    public DragonLocale get() {
        File configFile = new File(dataFolder, "locale_" + configuration.getProperty(ConfigurationProperty.LOCALE) + ".yml");
        ch.jalu.configme.utils.Utils.createFileIfNotExists(configFile);
        return new DragonLocale(new YamlFileResource(configFile),
            ConfigurationDataBuilder.createConfiguration(ConfigurationProperty.class), migrationService);
    }
}
