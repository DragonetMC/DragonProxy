package org.dragonet.dragonproxy.proxy.locale;

import ch.jalu.configme.SettingsManagerImpl;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.PropertyResource;

import javax.annotation.Nullable;

public class DragonLocale extends SettingsManagerImpl {

    public DragonLocale(PropertyResource resource, ConfigurationData configurationData, @Nullable MigrationService migrationService) {
        super(resource, configurationData, migrationService);
    }
}
