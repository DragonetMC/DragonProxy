package org.dragonet.dragonproxy.proxy.locale;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.migration.PlainMigrationService;
import ch.jalu.configme.resource.PropertyReader;

public class LocaleMigrationService extends PlainMigrationService {

    LocaleMigrationService() {
    }

    @Override
    protected boolean performMigrations(PropertyReader reader, ConfigurationData configurationData) {
        return NO_MIGRATION_NEEDED;
    }
}
