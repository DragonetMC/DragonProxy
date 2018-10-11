package org.dragonet.dragonproxy.proxy.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public final class FileUtils {

    public static void createFileIfNotExists(Path path) {
        try {
            if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                return;
            }
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDirectoriesIfNotExist(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileUtils() {
    }
}
