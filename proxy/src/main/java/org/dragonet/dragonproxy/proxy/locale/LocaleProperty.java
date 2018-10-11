package org.dragonet.dragonproxy.proxy.locale;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class LocaleProperty implements SettingsHolder {

    public final static Property<String> LOCALE_LANGUAGE = newProperty("locale.language", "English");
    public final static Property<String> LOCALE_AUTHOR = newProperty("locale.author", "DragonetMC");
    public final static Property<String> LOCALE_DATE = newProperty("locale.date", "");

    private LocaleProperty() {
    }
}
