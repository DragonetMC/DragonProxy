package org.dragonet.proxy.util;

import org.dragonet.proxy.DragonProxy;

import java.io.InputStream;

public class FileUtils {

    public static InputStream getResource(String path) {
        return DragonProxy.class.getClassLoader().getResourceAsStream(path);
    }
}
