package me.blueslime.pixelmotd.utils;

import dev.mruniverse.slimelib.file.configuration.handlers.util.FileUtils;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

public class FileUtilities {

    public static void load(SlimeLogs logs, File directory, String name, String resourceName) {
        FileUtils.checkFileExistence(
                new File(directory, name),
                logs,
                FileUtilities.class.getResourceAsStream(resourceName)
        );
    }

}