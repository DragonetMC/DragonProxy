package org.dragonet.dragonproxy.proxy.configuration;

import ch.jalu.configme.SettingsManagerImpl;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.PropertyResource;

import javax.annotation.Nullable;

public class DragonConfiguration extends SettingsManagerImpl {

    public DragonConfiguration(PropertyResource resource, ConfigurationData configurationData, @Nullable MigrationService migrationService) {
        super(resource, configurationData, migrationService);
    }
}
