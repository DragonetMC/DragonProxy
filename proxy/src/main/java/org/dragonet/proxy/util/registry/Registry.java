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
package org.dragonet.proxy.util.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.util.FileUtils;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Registry {

    // TODO: make this not static
    public static void registerPath(String path, Class<? extends Annotation> annotationClass, BiConsumer<Annotation, Class> consumer) {
        for(Class clazz : new Reflections(path).getTypesAnnotatedWith(annotationClass)) {
            Annotation annotation = clazz.getAnnotation(annotationClass);

            consumer.accept(annotation, clazz);
        }
    }

    public static void registerMapping(String path, Class<? extends MappingEntry> type, Consumer<LinkedHashMap<String, MappingEntry>> consumer) {
        InputStream stream = FileUtils.getResource(path);
        if(stream == null) {
            throw new RuntimeException("Failed to load mappings: " + path);
        }

        LinkedHashMap<String, MappingEntry> entries;
        try {
            entries = DragonProxy.JSON_MAPPER.readValue(stream, DragonProxy.JSON_MAPPER.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, type));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse mappings: " + path, e);
        }

        consumer.accept(entries);
    }
}
