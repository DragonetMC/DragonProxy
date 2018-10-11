package org.dragonet.dragonproxy.proxy.locale;

import ch.jalu.configme.SettingsManagerImpl;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.PropertyResource;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DragonLocale extends SettingsManagerImpl {

    public DragonLocale(@NonNull PropertyResource resource, @NonNull ConfigurationData configurationData, MigrationService migrationService) {
        super(resource, configurationData, migrationService);
    }
}
