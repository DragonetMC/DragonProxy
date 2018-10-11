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
