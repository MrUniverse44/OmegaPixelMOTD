package me.blueslime.pixelmotd.utils;

import me.blueslime.slimelib.file.configuration.handlers.util.FileUtils;
import me.blueslime.slimelib.logs.SlimeLogs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtilities {

    public static void load(SlimeLogs logs, File directory, String name, String resourceName) {
        FileUtils.checkFileExistence(
                new File(directory, name),
                logs,
                FileUtilities.class.getResourceAsStream(resourceName)
        );
    }

    public static void copy(File src, File dst) throws IOException {
        copy(src, dst, false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void copy(File src, File dst, boolean delete) throws IOException {
        if (src.isDirectory()) {
            if (!dst.exists()) {
                dst.mkdirs();
            } else if (!dst.isDirectory()) {
                throw new IllegalArgumentException("src is a directory, dst is not");
            }
            File[] sub = src.listFiles();
            if (sub != null) {
                for (File file : sub) {
                    copy(file, new File(dst, file.getName()));
                }
            }
            return;
        }

        if (dst.isDirectory()) {
            throw new IllegalArgumentException("dst is a directory, src is not");
        }

        Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);

        if (delete) {
            src.delete();
        }
    }

}