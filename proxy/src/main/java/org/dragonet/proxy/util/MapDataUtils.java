package org.dragonet.proxy.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.steveice10.mc.protocol.data.game.world.map.MapData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Log4j2
public class MapDataUtils {
    private static List<MapColor> mapColors = new LinkedList<>();

    static {
        // https://minecraft.gamepedia.com/Map_item_format#1.12_Color_Table
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("map_colors.json");
        if(stream == null) {
            throw new RuntimeException("Failed to load map_colors.json");
        }

        try {
            DragonProxy.JSON_MAPPER.readValue(stream, new TypeReference<Map<String, MapColor>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MapColor getColor(int id) {
        try {
            return mapColors.get(id);
        } catch (IndexOutOfBoundsException ex) {
            return new MapColor(-1, -1, -1);
        }
    }

    @Getter
    @RequiredArgsConstructor
    @ToString
    @JsonDeserialize(using = MapDataDeserializer.class)
    public static class MapColor {
        private final int red;
        private final int green;
        private final int blue;

        /**
         * Copyright (C) 2006 The Android Open Source Project
         * Adapted from https://android.googlesource.com/platform/frameworks/base/+/aeb60fb/graphics/java/android/graphics/Color.java#107
         */
        public int argb() {
            int alpha = 255;
            if(red == -1 && green == -1 && blue == -1) {
                alpha = 0;
            }
            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }

    private static class MapDataDeserializer extends JsonDeserializer<List<MapColor>> {

        @Override
        public List<MapColor> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            if(jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    int red = jsonParser.getValueAsInt();
                    int green = jsonParser.nextIntValue(1);
                    int blue = jsonParser.nextIntValue(2);

                    mapColors.add(new MapColor(red, green, blue));
                }
            }
            return mapColors;
        }
    }
}
