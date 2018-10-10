package org.dragonet.dragonproxy.proxy.configuration;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigurationProperty implements SettingsHolder {

    public final static Property<String> LOCALE = newProperty("locale", "EN");

    private ConfigurationProperty() {
    }
}
