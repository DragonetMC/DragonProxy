package org.dragonet.proxy.configuration.lang;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Load the Minecraft language translations.
 */
public class MinecraftLanguage {
    private static final Map<String, String> language = new HashMap<>();

    static {
        // TODO: support for more languages
        String path = DragonProxy.class.getClassLoader().getResource("en_us.json").getPath();

        try(JsonReader reader = new JsonReader(new FileReader(path))) {
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
