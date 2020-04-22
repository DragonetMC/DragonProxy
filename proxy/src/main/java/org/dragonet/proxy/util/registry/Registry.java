package org.dragonet.proxy.util.registry;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.function.BiConsumer;

public abstract class Registry {
    private static Class<? extends Annotation> annotationClass;

    // TODO: make this not static
    public static void registerType(Class<? extends Annotation> annotation) {
        annotationClass = annotation;
    }

    // TODO: make this not static
    public static void registerPath(String path, BiConsumer<Annotation, Class> callback) {
        for(Class clazz : new Reflections(path).getTypesAnnotatedWith(annotationClass)) {
            Annotation annotation = clazz.getAnnotation(annotationClass);

            callback.accept(annotation, clazz);
        }
    }
}
