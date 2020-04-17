/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.configuration.lang;

import com.google.gson.stream.JsonReader;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Load the Minecraft language translations.
 */
@Log4j2
public class MinecraftLanguage {
    private static final Object2ObjectMap<String, String> language = new Object2ObjectOpenHashMap<>();

    static {
        // TODO: support for more languages
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("lang/en_us.json");
        if(stream == null) {
            throw new RuntimeException("Cannot find Minecraft language file: en_us.json");
        }

        try(JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
            reader.beginObject();

            while(reader.hasNext()) {
                language.put(reader.nextName(), reader.nextString());
            }

            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String translate(String key) {
        return language.get(key);
    }

    public static String translate(String key, Object[] args) {
        return String.format(language.get(key), args);
    }
}
